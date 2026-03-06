# Module 05 : Protocole de Contexte de Modèle (MCP)

## Table des matières

- [Ce que vous allez apprendre](../../../05-mcp)
- [Qu'est-ce que le MCP ?](../../../05-mcp)
- [Comment fonctionne le MCP](../../../05-mcp)
- [Le module agentique](../../../05-mcp)
- [Exécution des exemples](../../../05-mcp)
  - [Prérequis](../../../05-mcp)
- [Démarrage rapide](../../../05-mcp)
  - [Opérations sur fichiers (Stdio)](../../../05-mcp)
  - [Agent Superviseur](../../../05-mcp)
    - [Exécution de la démo](../../../05-mcp)
    - [Comment fonctionne le superviseur](../../../05-mcp)
    - [Comment FileAgent découvre les outils MCP à l'exécution](../../../05-mcp)
    - [Stratégies de réponse](../../../05-mcp)
    - [Comprendre la sortie](../../../05-mcp)
    - [Explication des fonctionnalités du module agentique](../../../05-mcp)
- [Concepts clés](../../../05-mcp)
- [Félicitations !](../../../05-mcp)
  - [Et ensuite ?](../../../05-mcp)

## Ce que vous allez apprendre

Vous avez construit une IA conversationnelle, maîtrisé les invites, ancré des réponses dans des documents, et créé des agents avec des outils. Mais tous ces outils étaient conçus sur mesure pour votre application spécifique. Et si vous pouviez donner à votre IA accès à un écosystème standardisé d'outils que tout le monde peut créer et partager ? Dans ce module, vous apprendrez justement comment faire cela avec le Protocole de Contexte de Modèle (MCP) et le module agentique de LangChain4j. Nous montrons d'abord un simple lecteur de fichiers MCP, puis comment il s'intègre facilement dans des flux de travail agentiques avancés utilisant le modèle d'agent superviseur.

## Qu'est-ce que le MCP ?

Le Protocole de Contexte de Modèle (MCP) offre exactement cela : une méthode standard pour que les applications d'IA découvrent et utilisent des outils externes. Au lieu d'écrire des intégrations personnalisées pour chaque source de données ou service, vous vous connectez à des serveurs MCP qui exposent leurs capacités dans un format cohérent. Votre agent IA peut alors découvrir et utiliser ces outils automatiquement.

Le schéma ci-dessous montre la différence — sans MCP, chaque intégration nécessite un câblage point à point personnalisé ; avec le MCP, un seul protocole connecte votre application à n'importe quel outil :

<img src="../../../translated_images/fr/mcp-comparison.9129a881ecf10ff5.webp" alt="Comparaison MCP" width="800"/>

*Avant MCP : Intégrations complexes point à point. Après MCP : Un protocole, des possibilités infinies.*

Le MCP résout un problème fondamental dans le développement de l'IA : chaque intégration est personnalisée. Vous voulez accéder à GitHub ? Code personnalisé. Vous voulez lire des fichiers ? Code personnalisé. Vous voulez interroger une base de données ? Code personnalisé. Et aucune de ces intégrations ne fonctionne avec d'autres applications d'IA.

Le MCP standardise cela. Un serveur MCP expose des outils avec des descriptions claires et des schémas. Tout client MCP peut se connecter, découvrir les outils disponibles, et les utiliser. Construisez une fois, utilisez partout.

Le schéma ci-dessous illustre cette architecture — un seul client MCP (votre application IA) se connecte à plusieurs serveurs MCP, chacun exposant son propre ensemble d'outils via le protocole standard :

<img src="../../../translated_images/fr/mcp-architecture.b3156d787a4ceac9.webp" alt="Architecture MCP" width="800"/>

*Architecture du Protocole de Contexte de Modèle - découverte et exécution d'outils standardisées*

## Comment fonctionne le MCP

Sous le capot, le MCP utilise une architecture en couches. Votre application Java (le client MCP) découvre les outils disponibles, envoie des requêtes JSON-RPC via une couche de transport (Stdio ou HTTP), et le serveur MCP exécute les opérations et renvoie les résultats. Le diagramme suivant détaille chaque couche de ce protocole :

<img src="../../../translated_images/fr/mcp-protocol-detail.01204e056f45308b.webp" alt="Détail du protocole MCP" width="800"/>

*Comment fonctionne le MCP en interne — les clients découvrent les outils, échangent des messages JSON-RPC, et exécutent des opérations via une couche de transport.*

**Architecture Serveur-Client**

Le MCP utilise un modèle client-serveur. Les serveurs fournissent les outils — lecture de fichiers, interrogation de bases de données, appels d'API. Les clients (votre application IA) se connectent aux serveurs et utilisent leurs outils.

Pour utiliser le MCP avec LangChain4j, ajoutez cette dépendance Maven :

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Découverte d'Outils**

Lorsque votre client se connecte à un serveur MCP, il demande "Quels outils avez-vous ?" Le serveur répond avec une liste d'outils disponibles, chacun avec des descriptions et des schémas de paramètres. Votre agent IA peut alors décider quels outils utiliser selon les requêtes utilisateur. Le schéma ci-dessous montre cette phase de négociation — le client envoie une requête `tools/list` et le serveur renvoie ses outils disponibles avec descriptions et schémas des paramètres :

<img src="../../../translated_images/fr/tool-discovery.07760a8a301a7832.webp" alt="Découverte d'outils MCP" width="800"/>

*L'IA découvre les outils disponibles au démarrage — elle sait désormais quelles capacités sont disponibles et peut décider lesquelles utiliser.*

**Mécanismes de Transport**

Le MCP supporte différents mécanismes de transport. Les deux options sont Stdio (pour la communication avec des sous-processus locaux) et HTTP Streamable (pour des serveurs distants). Ce module démontre le transport Stdio :

<img src="../../../translated_images/fr/transport-mechanisms.2791ba7ee93cf020.webp" alt="Mécanismes de transport" width="800"/>

*Mécanismes de transport MCP : HTTP pour les serveurs distants, Stdio pour les processus locaux*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Pour les processus locaux. Votre application lance un serveur en sous-processus et communique via les entrées/sorties standards. Utile pour l'accès au système de fichiers ou les outils en ligne de commande.

```java
McpTransport stdioTransport = new StdioMcpTransport.Builder()
    .command(List.of(
        npmCmd, "exec",
        "@modelcontextprotocol/server-filesystem@2025.12.18",
        resourcesDir
    ))
    .logEvents(false)
    .build();
```

Le serveur `@modelcontextprotocol/server-filesystem` expose les outils suivants, tous confinés aux répertoires que vous spécifiez :

| Outil | Description |
|-------|-------------|
| `read_file` | Lire le contenu d'un seul fichier |
| `read_multiple_files` | Lire plusieurs fichiers en une seule requête |
| `write_file` | Créer ou écraser un fichier |
| `edit_file` | Faire des modifications ciblées par recherche-remplacement |
| `list_directory` | Lister les fichiers et répertoires d'un chemin |
| `search_files` | Rechercher récursivement des fichiers correspondant à un motif |
| `get_file_info` | Obtenir les métadonnées d'un fichier (taille, horodatages, permissions) |
| `create_directory` | Créer un répertoire (y compris les répertoires parents) |
| `move_file` | Déplacer ou renommer un fichier ou un répertoire |

Le schéma suivant montre comment fonctionne le transport Stdio à l'exécution — votre application Java lance le serveur MCP comme un processus enfant et ils communiquent via des tubes stdin/stdout, sans réseau ni HTTP impliqué :

<img src="../../../translated_images/fr/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Flux du transport Stdio" width="800"/>

*Transport Stdio en action — votre application lance le serveur MCP en processus enfant et communique via des tubes stdin/stdout.*

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) et demandez :
> - « Comment fonctionne le transport Stdio et quand l'utiliser par rapport à HTTP ? »
> - « Comment LangChain4j gère-t-il le cycle de vie des processus serveurs MCP lancés ? »
> - « Quelles sont les implications de sécurité de donner accès à l'IA au système de fichiers ? »

## Le module agentique

Tandis que le MCP fournit des outils standardisés, le module **agentique** de LangChain4j offre une manière déclarative de construire des agents orchestrant ces outils. L'annotation `@Agent` et `AgenticServices` vous permettent de définir le comportement d'un agent via des interfaces plutôt que du code impératif.

Dans ce module, vous explorerez le modèle d'agent **Superviseur** — une approche agentique avancée où un agent « superviseur » décide dynamiquement quels sous-agents invoquer selon les requêtes utilisateur. Nous combinons ces deux concepts en donnant à un de nos sous-agents des capacités d'accès fichier propulsées par MCP.

Pour utiliser le module agentique, ajoutez cette dépendance Maven :

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Note :** Le module `langchain4j-agentic` utilise une propriété de version distincte (`langchain4j.mcp.version`) car il est publié selon un calendrier différent des bibliothèques LangChain4j de base.

> **⚠️ Expérimental :** Le module `langchain4j-agentic` est **expérimental** et sujet à changement. La façon stable de construire des assistants AI reste `langchain4j-core` avec des outils personnalisés (Module 04).

## Exécution des exemples

### Prérequis

- Avoir complété le [Module 04 - Outils](../04-tools/README.md) (ce module s’appuie sur les concepts d’outils personnalisés et les compare avec les outils MCP)
- Fichier `.env` à la racine contenant les identifiants Azure (créé par `azd up` dans le Module 01)
- Java 21+, Maven 3.9+
- Node.js 16+ et npm (pour les serveurs MCP)

> **Note :** Si vous n’avez pas encore configuré vos variables d’environnement, voir [Module 01 - Introduction](../01-introduction/README.md) pour les instructions de déploiement (`azd up` crée automatiquement le fichier `.env`), ou copiez `.env.example` vers `.env` à la racine et remplissez vos valeurs.

## Démarrage rapide

**Avec VS Code :** Cliquez droit sur n’importe quel fichier de démo dans l’Explorateur et sélectionnez **« Run Java »**, ou utilisez les configurations de lancement dans le panneau Run and Debug (assurez-vous d’avoir configuré votre fichier `.env` avec les identifiants Azure au préalable).

**Avec Maven :** Vous pouvez aussi lancer les exemples en ligne de commande avec les exemples ci-dessous.

### Opérations sur fichiers (Stdio)

Cela démontre les outils locaux basés sur des sous-processus.

**✅ Aucun prérequis requis** - le serveur MCP est lancé automatiquement.

**Utilisation des scripts de démarrage (Recommandé) :**

Les scripts de démarrage chargent automatiquement les variables d’environnement du fichier `.env` à la racine :

**Bash :**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**PowerShell :**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**Avec VS Code :** Cliquez droit sur `StdioTransportDemo.java` et choisissez **« Run Java »** (assurez-vous que votre fichier `.env` est configuré).

L’application lance automatiquement un serveur MCP système de fichiers et lit un fichier local. Notez comment la gestion du sous-processus est prise en charge pour vous.

**Sortie attendue :**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agent Superviseur

Le modèle **Agent Superviseur** est une forme **flexible** d’IA agentique. Un superviseur utilise un LLM pour décider de manière autonome quels agents invoquer selon la requête utilisateur. Dans l’exemple suivant, nous combinons l’accès fichier MCP avec un agent LLM pour créer un flux supervisé lecture → rapport.

Dans la démo, `FileAgent` lit un fichier en utilisant les outils système de fichiers MCP, et `ReportAgent` génère un rapport structuré avec un résumé exécutif (1 phrase), 3 points clés, et des recommandations. Le superviseur orchestre ce flux automatiquement :

<img src="../../../translated_images/fr/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Modèle d’Agent Superviseur" width="800"/>

*Le superviseur utilise son LLM pour décider quels agents invoquer et dans quel ordre — aucun routage codé en dur nécessaire.*

Voici à quoi ressemble le workflow concret pour notre pipeline fichier → rapport :

<img src="../../../translated_images/fr/file-report-workflow.649bb7a896800de9.webp" alt="Workflow Fichier vers Rapport" width="800"/>

*FileAgent lit le fichier via les outils MCP, puis ReportAgent transforme le contenu brut en rapport structuré.*

Le diagramme de séquence suivant retrace toute l’orchestration du superviseur — du lancement du serveur MCP, à la sélection autonome des agents par le superviseur, aux appels d’outils via stdio et au rapport final :

<img src="../../../translated_images/fr/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Diagramme de séquence Agent Superviseur" width="800"/>

*Le superviseur invoque automatiquement FileAgent (qui appelle le serveur MCP via stdio pour lire le fichier), puis invoque ReportAgent pour générer un rapport structuré — chaque agent stocke sa sortie dans la portée agentique partagée.*

Chaque agent stocke sa sortie dans la **Portée Agentique** (mémoire partagée), permettant aux agents en aval d’accéder aux résultats précédents. Cela montre comment les outils MCP s’intègrent parfaitement dans les workflows agentiques — le superviseur n’a pas besoin de savoir *comment* les fichiers sont lus, seulement que `FileAgent` peut le faire.

#### Exécution de la démo

Les scripts de démarrage chargent automatiquement les variables d’environnement du fichier `.env` à la racine :

**Bash :**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**PowerShell :**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**Avec VS Code :** Cliquez droit sur `SupervisorAgentDemo.java` et choisissez **« Run Java »** (assurez-vous que votre fichier `.env` est configuré).

#### Comment fonctionne le superviseur

Avant de construire les agents, vous devez connecter le transport MCP à un client et l’envelopper en tant que `ToolProvider`. Voici comment les outils du serveur MCP deviennent disponibles pour vos agents :

```java
// Créez un client MCP à partir du transport
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Enveloppez le client en tant que ToolProvider — cela fait le lien entre les outils MCP et LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Vous pouvez maintenant injecter `mcpToolProvider` dans n’importe quel agent nécessitant des outils MCP :

```java
// Étape 1 : FileAgent lit les fichiers en utilisant les outils MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Possède des outils MCP pour les opérations sur les fichiers
        .build();

// Étape 2 : ReportAgent génère des rapports structurés
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Le superviseur orchestre le flux de travail fichier → rapport
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Retourner le rapport final
        .build();

// Le superviseur décide quels agents invoquer en fonction de la demande
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Comment FileAgent découvre les outils MCP à l’exécution

Vous vous demandez peut-être : **comment `FileAgent` sait-il utiliser les outils npm du système de fichiers ?** La réponse est qu’il ne le sait pas — c’est le **LLM** qui le découvre à l’exécution via les schémas des outils.

L’interface `FileAgent` est juste une **définition d’invite**. Elle ne connaît pas en dur `read_file`, `list_directory` ni aucun autre outil MCP. Voici ce qui se passe de bout en bout :
1. **Démarrage du serveur :** `StdioMcpTransport` lance le paquet npm `@modelcontextprotocol/server-filesystem` en tant que processus enfant  
2. **Découverte des outils :** Le `McpClient` envoie une requête JSON-RPC `tools/list` au serveur, qui répond avec les noms des outils, les descriptions et les schémas des paramètres (par exemple, `read_file` — *"Lire le contenu complet d'un fichier"* — `{ path: string }`)  
3. **Injection des schémas :** `McpToolProvider` englobe ces schémas découverts et les rend disponibles pour LangChain4j  
4. **Décision du LLM :** Lorsque `FileAgent.readFile(path)` est appelé, LangChain4j envoie le message système, le message utilisateur, **et la liste des schémas des outils** au LLM. Le LLM lit les descriptions des outils et génère un appel d’outil (par exemple, `read_file(path="/some/file.txt")`)  
5. **Exécution :** LangChain4j intercepte l’appel d’outil, le redirige via le client MCP vers le sous-processus Node.js, récupère le résultat, et le transmet au LLM  

Il s’agit du même mécanisme de [Découverte d’outils](../../../05-mcp) décrit plus haut, mais appliqué spécifiquement au flux de travail de l’agent. Les annotations `@SystemMessage` et `@UserMessage` guident le comportement du LLM, tandis que le `ToolProvider` injecté lui fournit les **capacités** — le LLM fait le lien entre les deux à l’exécution.

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) et demandez :  
> - "Comment cet agent sait-il quel outil MCP appeler ?"  
> - "Que se passerait-il si je supprimais le ToolProvider du constructeur de l’agent ?"  
> - "Comment les schémas des outils sont-ils transmis au LLM ?"  

#### Stratégies de réponse

Lorsque vous configurez un `SupervisorAgent`, vous spécifiez comment il doit formuler sa réponse finale à l’utilisateur une fois que les sous-agents ont terminé leurs tâches. Le schéma ci-dessous montre les trois stratégies disponibles — LAST renvoie directement la sortie finale de l’agent, SUMMARY synthétise toutes les sorties via un LLM, et SCORED choisit celle qui obtient le meilleur score par rapport à la requête d’origine :

<img src="../../../translated_images/fr/response-strategies.3d0cea19d096bdf9.webp" alt="Stratégies de réponse" width="800"/>

*Trois stratégies pour la formulation de la réponse finale par le Supervisor — choisissez selon que vous souhaitez la sortie du dernier agent, un résumé synthétisé, ou l’option la mieux notée.*

Les stratégies disponibles sont :

| Stratégie | Description |
|----------|-------------|
| **LAST** | Le superviseur renvoie la sortie du dernier sous-agent ou outil appelé. Utile lorsque l’agent final du flux de travail est spécialement conçu pour produire la réponse complète finale (exemple : un « Agent Résumé » dans un pipeline de recherche). |
| **SUMMARY** | Le superviseur utilise son propre modèle de langage interne (LLM) pour synthétiser un résumé de l’ensemble de l’interaction et de toutes les sorties des sous-agents, puis renvoie ce résumé comme réponse finale. Cela fournit une réponse agrégée claire à l’utilisateur. |
| **SCORED** | Le système utilise un LLM interne pour noter à la fois la réponse LAST et le résumé SUMMARY de l’interaction par rapport à la requête originale de l’utilisateur, renvoyant la sortie ayant obtenu le meilleur score. |

Voir [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) pour l’implémentation complète.

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) et demandez :  
> - "Comment le Supervisor décide-t-il quels agents invoquer ?"  
> - "Quelle est la différence entre les patterns Supervisor et Sequential ?"  
> - "Comment puis-je personnaliser le comportement de planification du Supervisor ?"  

#### Comprendre la sortie

Lors de l’exécution de la démonstration, vous verrez un parcours structuré montrant comment le Supervisor orchestre plusieurs agents. Voici la signification de chaque section :

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Le titre** présente le concept de flux de travail : un pipeline ciblé de la lecture de fichiers à la génération de rapport.

```
--- WORKFLOW ---------------------------------------------------------
  ┌─────────────┐      ┌──────────────┐
  │  FileAgent  │ ───▶ │ ReportAgent  │
  │ (MCP tools) │      │  (pure LLM)  │
  └─────────────┘      └──────────────┘
   outputKey:           outputKey:
   'fileContent'        'report'

--- AVAILABLE AGENTS -------------------------------------------------
  [FILE]   FileAgent   - Reads files via MCP → stores in 'fileContent'
  [REPORT] ReportAgent - Generates structured report → stores in 'report'
```
  
**Schéma du flux de travail** montre le flux de données entre les agents. Chaque agent a un rôle spécifique :  
- **FileAgent** lit les fichiers en utilisant les outils MCP et stocke le contenu brut dans `fileContent`  
- **ReportAgent** consomme ce contenu et produit un rapport structuré dans `report`  

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Requête utilisateur** montre la tâche. Le Supervisor l’analyse et décide d’invoquer FileAgent → ReportAgent.

```
--- SUPERVISOR ORCHESTRATION -----------------------------------------
  The Supervisor decides which agents to invoke and passes data between them...

  +-- STEP 1: Supervisor chose -> FileAgent (reading file via MCP)
  |
  |   Input: .../file.txt
  |
  |   Result: LangChain4j is an open-source, provider-agnostic Java framework for building LLM...
  +-- [OK] FileAgent (reading file via MCP) completed

  +-- STEP 2: Supervisor chose -> ReportAgent (generating structured report)
  |
  |   Input: LangChain4j is an open-source, provider-agnostic Java framew...
  |
  |   Result: Executive Summary...
  +-- [OK] ReportAgent (generating structured report) completed
```
  
**Orchestration du Supervisor** montre le flux en 2 étapes :  
1. **FileAgent** lit le fichier via MCP et stocke le contenu  
2. **ReportAgent** reçoit le contenu et génère un rapport structuré  

Le Supervisor a pris ces décisions **autonomiquement** selon la requête de l’utilisateur.

```
--- FINAL RESPONSE ---------------------------------------------------
Executive Summary
...

Key Points
...

Recommendations
...

--- AGENTIC SCOPE (Data Flow) ----------------------------------------
  Each agent stores its output for downstream agents to consume:
  * fileContent: LangChain4j is an open-source, provider-agnostic Java framework...
  * report: Executive Summary...
```
  
#### Explication des fonctionnalités du module Agentic

L’exemple démontre plusieurs fonctionnalités avancées du module agentic. Regardons de plus près Agentic Scope et les Agent Listeners.

**Agentic Scope** montre la mémoire partagée où les agents ont stocké leurs résultats avec `@Agent(outputKey="...")`. Cela permet :  
- Aux agents suivants d’accéder aux sorties des agents précédents  
- Au Supervisor de synthétiser une réponse finale  
- De vérifier ce que chaque agent a produit  

Le schéma ci-dessous montre comment Agentic Scope fonctionne comme mémoire partagée dans le flux fichier-vers-rapport — FileAgent écrit sa sortie sous la clé `fileContent`, ReportAgent la lit et écrit sa propre sortie sous `report` :

<img src="../../../translated_images/fr/agentic-scope.95ef488b6c1d02ef.webp" alt="Mémoire partagée Agentic Scope" width="800"/>

*Agentic Scope agit comme mémoire partagée — FileAgent écrit `fileContent`, ReportAgent le lit et écrit `report`, et votre code lit le résultat final.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Données brutes du fichier de FileAgent
String report = scope.readState("report");            // Rapport structuré de ReportAgent
```
  
**Agent Listeners** permettent de surveiller et déboguer l’exécution des agents. La sortie pas à pas que vous voyez dans la démo provient d’un AgentListener qui s’accroche à chaque invocation d’agent :  
- **beforeAgentInvocation** - Appelé lorsque le Supervisor sélectionne un agent, vous permet de voir quel agent a été choisi et pourquoi  
- **afterAgentInvocation** - Appelé lorsqu’un agent termine, montrant son résultat  
- **inheritedBySubagents** - Quand vrai, le listener surveille tous les agents de la hiérarchie  

Le schéma suivant montre le cycle de vie complet de l’Agent Listener, y compris comment `onError` gère les échecs durant l’exécution d’un agent :

<img src="../../../translated_images/fr/agent-listeners.784bfc403c80ea13.webp" alt="Cycle de vie des Agent Listeners" width="800"/>

*Agent Listeners s’intègrent au cycle de vie d’exécution — surveillez quand les agents démarrent, terminent ou rencontrent des erreurs.*

```java
AgentListener monitor = new AgentListener() {
    private int step = 0;
    
    @Override
    public void beforeAgentInvocation(AgentRequest request) {
        step++;
        System.out.println("  +-- STEP " + step + ": " + request.agentName());
    }
    
    @Override
    public void afterAgentInvocation(AgentResponse response) {
        System.out.println("  +-- [OK] " + response.agentName() + " completed");
    }
    
    @Override
    public boolean inheritedBySubagents() {
        return true; // Propager à tous les sous-agents
    }
};
```
  
Au-delà du pattern Supervisor, le module `langchain4j-agentic` fournit plusieurs puissants patterns de workflows. Le schéma ci-dessous montre les cinq — des pipelines séquentiels simples aux workflows d’approbation avec intervention humaine :

<img src="../../../translated_images/fr/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Patterns de flux de travail des agents" width="800"/>

*Cinq patterns de workflow pour orchestrer des agents — des pipelines séquentiels simples aux workflows d’approbation avec intervention humaine.*

| Pattern | Description | Cas d’usage |
|---------|-------------|-------------|
| **Sequential** | Exécute les agents dans l’ordre, la sortie alimente le suivant | Pipelines : recherche → analyse → rapport |
| **Parallel** | Exécute les agents simultanément | Tâches indépendantes : météo + actualités + bourse |
| **Loop** | Itère jusqu’à ce que la condition soit remplie | Évaluation qualité : affiner jusqu’à score ≥ 0.8 |
| **Conditional** | Oriente selon des conditions | Classification → routage vers agent spécialiste |
| **Human-in-the-Loop** | Ajoute des points de contrôle humains | Workflows d’approbation, relecture de contenu |

## Concepts clés

Maintenant que vous avez exploré MCP et le module agentic en action, résumons quand utiliser chaque approche.

L’un des plus grands avantages de MCP est son écosystème en pleine expansion. Le schéma ci-dessous montre comment un protocole universel unique connecte votre application IA à une grande variété de serveurs MCP — accès système de fichiers, base de données, GitHub, email, scraping web, et plus :

<img src="../../../translated_images/fr/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="Écosystème MCP" width="800"/>

*MCP crée un écosystème de protocole universel — tout serveur compatible MCP fonctionne avec tout client compatible MCP, permettant le partage d’outils entre applications.*

**MCP** est idéal si vous souhaitez tirer parti d’écosystèmes d’outils existants, construire des outils partageables par plusieurs applications, intégrer des services tiers avec des protocoles standards, ou remplacer les implémentations d’outils sans modifier le code.

**Le module Agentic** est préférable si vous voulez des définitions d’agents déclaratives via annotations `@Agent`, avez besoin d’orchestration de workflow (séquentiel, boucle, parallèle), préférez une conception d’agents basée sur interface plutôt que du code impératif, ou combinez plusieurs agents partageant leurs sorties via `outputKey`.

**Le pattern Supervisor Agent** est pertinent lorsque le flux de travail n’est pas prévisible à l’avance et que vous voulez que le LLM décide, lorsque vous avez plusieurs agents spécialisés nécessitant une orchestration dynamique, lorsque vous construisez des systèmes conversationnels orientés vers différentes capacités, ou lorsque vous voulez le comportement d’agent le plus flexible et adaptatif.

Pour vous aider à choisir entre les méthodes personnalisées `@Tool` du Module 04 et les outils MCP de ce module, la comparaison suivante met en évidence les compromis clés — les outils personnalisés offrent un couplage étroit et une sécurité de type complète pour la logique spécifique à l’application, tandis que les outils MCP proposent des intégrations standardisées et réutilisables :

<img src="../../../translated_images/fr/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Outils personnalisés vs Outils MCP" width="800"/>

*Quand utiliser les méthodes custom @Tool vs les outils MCP — outils personnalisés pour la logique spécifique avec sécurité de type complète, outils MCP pour des intégrations standardisées qui fonctionnent à travers les applications.*

## Félicitations !

Vous avez terminé les cinq modules du cours LangChain4j pour débutants ! Voici un aperçu de votre parcours complet — du chat basique aux systèmes agentic propulsés par MCP :

<img src="../../../translated_images/fr/course-completion.48cd201f60ac7570.webp" alt="Achèvement du cours" width="800"/>

*Votre parcours d’apprentissage à travers les cinq modules — du chat basique aux systèmes agentic propulsés par MCP.*

Vous avez appris :

- Comment construire une IA conversationnelle avec mémoire (Module 01)  
- Les patterns de prompt engineering pour différentes tâches (Module 02)  
- Comment ancrer les réponses dans vos documents avec RAG (Module 03)  
- Créer des agents IA basiques (assistants) avec des outils personnalisés (Module 04)  
- Intégrer des outils standardisés avec les modules LangChain4j MCP et Agentic (Module 05)  

### Et après ?

Après avoir complété les modules, consultez le [Guide de test](../docs/TESTING.md) pour voir les concepts de tests LangChain4j en action.

**Ressources officielles :**  
- [Documentation LangChain4j](https://docs.langchain4j.dev/) - Guides complets et référence API  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Code source et exemples  
- [Tutoriels LangChain4j](https://docs.langchain4j.dev/tutorials/) - Tutoriels étape par étape pour divers cas d’usage  

Merci d’avoir suivi ce cours !

---

**Navigation :** [← Précédent : Module 04 - Outils](../04-tools/README.md) | [Retour à l’accueil](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :  
Ce document a été traduit à l’aide du service de traduction automatisée [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforçons d’assurer l’exactitude, veuillez noter que les traductions automatiques peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue d’origine doit être considéré comme la source faisant foi. Pour les informations critiques, une traduction professionnelle réalisée par un humain est recommandée. Nous déclinons toute responsabilité en cas de malentendus ou de mauvaises interprétations résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->