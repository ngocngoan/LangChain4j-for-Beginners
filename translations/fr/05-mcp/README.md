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
  - [Agent superviseur](../../../05-mcp)
    - [Exécution de la démo](../../../05-mcp)
    - [Comment fonctionne le superviseur](../../../05-mcp)
    - [Stratégies de réponse](../../../05-mcp)
    - [Comprendre la sortie](../../../05-mcp)
    - [Explication des fonctionnalités du module agentique](../../../05-mcp)
- [Concepts clés](../../../05-mcp)
- [Félicitations !](../../../05-mcp)
  - [Et après ?](../../../05-mcp)

## Ce que vous allez apprendre

Vous avez construit une IA conversationnelle, maîtrisé les prompts, ancré les réponses dans des documents, et créé des agents avec des outils. Mais tous ces outils étaient faits sur mesure pour votre application spécifique. Et si vous pouviez donner à votre IA accès à un écosystème standardisé d'outils que tout le monde peut créer et partager ? Dans ce module, vous apprendrez justement à faire cela avec le Protocole de Contexte de Modèle (MCP) et le module agentique de LangChain4j. Nous présentons d’abord un simple lecteur de fichiers MCP, puis montrons comment il s’intègre facilement dans des workflows agentiques avancés utilisant le pattern Agent Superviseur.

## Qu'est-ce que le MCP ?

Le Protocole de Contexte de Modèle (MCP) offre exactement cela — une façon standard pour les applications IA de découvrir et d’utiliser des outils externes. Au lieu d’écrire des intégrations personnalisées pour chaque source de données ou service, vous vous connectez à des serveurs MCP qui exposent leurs fonctionnalités dans un format cohérent. Votre agent IA peut alors découvrir et utiliser ces outils automatiquement.

Le schéma ci-dessous montre la différence — sans MCP, chaque intégration nécessite un câblage point-à-point personnalisé ; avec MCP, un seul protocole connecte votre appli à n’importe quel outil :

<img src="../../../translated_images/fr/mcp-comparison.9129a881ecf10ff5.webp" alt="Comparaison MCP" width="800"/>

*Avant MCP : Intégrations point-à-point complexes. Après MCP : Un protocole, des possibilités infinies.*

MCP résout un problème fondamental du développement en IA : chaque intégration est personnalisée. Vous voulez accéder à GitHub ? Code personnalisé. Lire des fichiers ? Code personnalisé. Interroger une base de données ? Code personnalisé. Et aucune de ces intégrations ne fonctionne avec d’autres applications IA.

MCP standardise tout cela. Un serveur MCP expose des outils avec des descriptions claires et des schémas. N’importe quel client MCP peut se connecter, découvrir les outils disponibles, et les utiliser. Construisez une fois, utilisez partout.

Le schéma ci-dessous illustre cette architecture — un seul client MCP (votre application IA) se connecte à plusieurs serveurs MCP, chacun exposant son propre ensemble d’outils via le protocole standard :

<img src="../../../translated_images/fr/mcp-architecture.b3156d787a4ceac9.webp" alt="Architecture MCP" width="800"/>

*Architecture du Protocole de Contexte de Modèle - découverte et exécution d’outils standardisées*

## Comment fonctionne le MCP

En interne, MCP utilise une architecture en couches. Votre application Java (le client MCP) découvre les outils disponibles, envoie des requêtes JSON-RPC via une couche de transport (Stdio ou HTTP), et le serveur MCP exécute les opérations et retourne les résultats. Le schéma suivant détaille chaque couche de ce protocole :

<img src="../../../translated_images/fr/mcp-protocol-detail.01204e056f45308b.webp" alt="Détail du protocole MCP" width="800"/>

*Comment fonctionne MCP en interne — les clients découvrent les outils, échangent des messages JSON-RPC, et exécutent des opérations via une couche de transport.*

**Architecture Client-Serveur**

MCP utilise un modèle client-serveur. Les serveurs fournissent des outils — lecture de fichiers, interrogation de bases de données, appel d’API. Les clients (votre application IA) se connectent aux serveurs et utilisent leurs outils.

Pour utiliser MCP avec LangChain4j, ajoutez cette dépendance Maven :

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Découverte d'outils**

Quand votre client se connecte à un serveur MCP, il demande "Quels outils avez-vous ?" Le serveur répond avec une liste d’outils disponibles, chacun avec descriptions et schémas des paramètres. Votre agent IA peut alors décider quels outils utiliser selon les demandes utilisateur. Le schéma ci-dessous montre cette poignée de main — le client envoie une requête `tools/list` et le serveur retourne ses outils disponibles avec descriptions et schémas des paramètres :

<img src="../../../translated_images/fr/tool-discovery.07760a8a301a7832.webp" alt="Découverte d'outils MCP" width="800"/>

*L’IA découvre les outils disponibles au démarrage — elle connaît maintenant les capacités accessibles et peut décider lesquels utiliser.*

**Mécanismes de transport**

MCP supporte différents mécanismes de transport. Les deux options sont Stdio (pour communication locale avec sous-processus) et HTTP streamable (pour serveurs distants). Ce module présente le transport Stdio :

<img src="../../../translated_images/fr/transport-mechanisms.2791ba7ee93cf020.webp" alt="Mécanismes de transport" width="800"/>

*Transports MCP : HTTP pour serveurs distants, Stdio pour processus locaux*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Pour les processus locaux. Votre application lance un serveur en sous-processus et communique via l’entrée/sortie standard. Utile pour l’accès au système de fichiers ou les outils en ligne de commande.

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

Le serveur `@modelcontextprotocol/server-filesystem` expose les outils suivants, tous sandboxés dans les répertoires spécifiés :

| Outil | Description |
|------|-------------|
| `read_file` | Lire le contenu d’un fichier unique |
| `read_multiple_files` | Lire plusieurs fichiers en un appel |
| `write_file` | Créer ou écraser un fichier |
| `edit_file` | Effectuer des remplacements ciblés (rechercher-remplacer) |
| `list_directory` | Lister fichiers et répertoires dans un chemin |
| `search_files` | Rechercher récursivement des fichiers selon un motif |
| `get_file_info` | Obtenir des métadonnées du fichier (taille, dates, permissions) |
| `create_directory` | Créer un répertoire (y compris parents) |
| `move_file` | Déplacer ou renommer un fichier ou répertoire |

Le schéma suivant montre comment le transport Stdio fonctionne à l’exécution — votre application Java lance le serveur MCP comme un sous-processus et ils communiquent via des tubes stdin/stdout, sans réseau ni HTTP impliqués :

<img src="../../../translated_images/fr/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Flux de transport Stdio" width="800"/>

*Transport Stdio en action — votre application lance le serveur MCP comme sous-processus et communique via tubes stdin/stdout.*

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) et demandez :
> - "Comment fonctionne le transport Stdio et quand dois-je l’utiliser plutôt que HTTP ?"
> - "Comment LangChain4j gère-t-il le cycle de vie des processus serveurs MCP lancés ?"
> - "Quelles sont les implications de sécurité de donner à l’IA l’accès au système de fichiers ?"

## Le module agentique

Alors que MCP fournit des outils standardisés, le module **agentique** de LangChain4j offre un moyen déclaratif de construire des agents qui orchestrent ces outils. L’annotation `@Agent` et `AgenticServices` vous permettent de définir le comportement des agents via des interfaces plutôt que du code impératif.

Dans ce module, vous explorerez le pattern **Agent Superviseur** — une approche agentique avancée où un agent "superviseur" décide dynamiquement quels sous-agents invoquer selon les requêtes utilisateurs. Nous combinons ces deux concepts en donnant à un de nos sous-agents des capacités d’accès aux fichiers propulsées par MCP.

Pour utiliser le module agentique, ajoutez cette dépendance Maven :

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Note :** Le module `langchain4j-agentic` utilise une propriété de version distincte (`langchain4j.mcp.version`) car il est publié selon un calendrier différent des bibliothèques principales LangChain4j.

> **⚠️ Expérimental :** Le module `langchain4j-agentic` est **expérimental** et susceptible d’évoluer. La façon stable de construire des assistants IA reste `langchain4j-core` avec des outils personnalisés (Module 04).

## Exécution des exemples

### Prérequis

- Avoir complété le [Module 04 - Outils](../04-tools/README.md) (ce module s’appuie sur les concepts d’outils personnalisés et les compare avec les outils MCP)
- Fichier `.env` à la racine avec les identifiants Azure (créé par `azd up` dans le Module 01)
- Java 21+, Maven 3.9+
- Node.js 16+ et npm (pour les serveurs MCP)

> **Note :** Si vous n’avez pas encore configuré vos variables d’environnement, consultez le [Module 01 - Introduction](../01-introduction/README.md) pour les instructions de déploiement (`azd up` crée automatiquement le fichier `.env`), ou copiez `.env.example` en `.env` à la racine et remplissez vos valeurs.

## Démarrage rapide

**Avec VS Code :** Faites un clic droit sur n’importe quel fichier demo dans l’Explorateur et sélectionnez **"Run Java"**, ou utilisez les configurations de lancement depuis le panneau Exécuter et Déboguer (assurez-vous d’abord que votre fichier `.env` contient les identifiants Azure).

**Avec Maven :** Vous pouvez aussi lancer depuis la ligne de commande avec les exemples ci-dessous.

### Opérations sur fichiers (Stdio)

Cela démontre des outils basés sur des sous-processus locaux.

**✅ Pas de prérequis nécessaires** — le serveur MCP est lancé automatiquement.

**Utilisation des scripts de démarrage (recommandé) :**

Les scripts de démarrage chargent automatiquement les variables d’environnement depuis le fichier `.env` à la racine :

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

**Avec VS Code :** Clic droit sur `StdioTransportDemo.java` et sélectionnez **"Run Java"** (vérifiez que `.env` est configuré).

L’application lance automatiquement un serveur MCP système de fichiers et lit un fichier local. Notez comment la gestion du sous-processus est prise en charge pour vous.

**Sortie attendue :**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agent superviseur

Le **pattern Agent Superviseur** est une forme **flexible** d’IA agentique. Un superviseur utilise un LLM pour décider de manière autonome quels agents invoquer selon la requête utilisateur. Dans l’exemple suivant, nous combinons l’accès aux fichiers propulsé par MCP avec un agent LLM pour créer un workflow supervisé lecture de fichier → rapport.

Dans la démo, `FileAgent` lit un fichier utilisant les outils MCP système de fichiers, et `ReportAgent` génère un rapport structuré avec un résumé exécutif (1 phrase), 3 points clés, et des recommandations. Le superviseur orchestre ce flux automatiquement :

<img src="../../../translated_images/fr/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Pattern Agent Superviseur" width="800"/>

*Le superviseur utilise son LLM pour décider quels agents invoquer et dans quel ordre — aucun routage codé en dur nécessaire.*

Voici à quoi ressemble concrètement le workflow pour notre pipeline fichier → rapport :

<img src="../../../translated_images/fr/file-report-workflow.649bb7a896800de9.webp" alt="Workflow fichier vers rapport" width="800"/>

*FileAgent lit le fichier via les outils MCP, puis ReportAgent transforme le contenu brut en rapport structuré.*

Chaque agent stocke sa sortie dans le **Scope Agentique** (mémoire partagée), permettant aux agents en aval d'accéder aux résultats précédents. Cela montre comment les outils MCP s’intègrent parfaitement aux workflows agentiques — le superviseur n’a pas besoin de savoir *comment* les fichiers sont lus, seulement que `FileAgent` peut le faire.

#### Exécution de la démo

Les scripts de démarrage chargent automatiquement les variables d’environnement depuis le fichier `.env` à la racine :

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

**Avec VS Code :** Clic droit sur `SupervisorAgentDemo.java` et sélectionnez **"Run Java"** (vérifiez que `.env` est configuré).

#### Comment fonctionne le superviseur

Avant de construire les agents, vous devez connecter le transport MCP à un client et l’encapsuler comme un `ToolProvider`. C’est ainsi que les outils du serveur MCP deviennent accessibles à vos agents :

```java
// Créez un client MCP à partir du transport
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Enveloppez le client en tant que ToolProvider — cela fait le pont entre les outils MCP et LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Vous pouvez maintenant injecter `mcpToolProvider` dans n’importe quel agent qui a besoin des outils MCP :

```java
// Étape 1 : FileAgent lit les fichiers en utilisant les outils MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Dispose des outils MCP pour les opérations sur les fichiers
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

#### Stratégies de réponse

Lors de la configuration d’un `SupervisorAgent`, vous spécifiez comment il doit formuler sa réponse finale à l’utilisateur après que les sous-agents ont accompli leurs tâches. Le schéma ci-dessous présente les trois stratégies disponibles — LAST renvoie directement la sortie du dernier agent, SUMMARY synthétise toutes les sorties via un LLM, et SCORED choisit celle qui obtient le meilleur score par rapport à la requête initiale :

<img src="../../../translated_images/fr/response-strategies.3d0cea19d096bdf9.webp" alt="Stratégies de réponse" width="800"/>

*Trois stratégies pour la formulation de la réponse finale par le superviseur — choisissez selon que vous souhaitez la sortie du dernier agent, un résumé synthétisé, ou l’option la mieux notée.*

Les stratégies disponibles sont :

| Stratégie | Description |
|----------|-------------|
| **LAST** | Le superviseur renvoie la sortie du dernier sous-agent ou outil appelé. Utile lorsque le dernier agent dans le workflow est spécifiquement conçu pour produire la réponse finale complète (exemple : un "Agent Résumé" dans un pipeline de recherche). |
| **SUMMARY** | Le superviseur utilise son propre modèle de langage (LLM) interne pour synthétiser un résumé de toute l’interaction et de toutes les sorties des sous-agents, puis renvoie ce résumé comme réponse finale. Cela fournit une réponse propre et agrégée à l’utilisateur. |
| **SCORED** | Le système utilise un LLM interne pour évaluer à la fois la réponse LAST et le SUMMARY de l’interaction par rapport à la requête utilisateur originale, puis renvoie celle qui obtient le meilleur score. |
Voir [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) pour l’implémentation complète.

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) et demandez :
> - « Comment le Superviseur décide-t-il quels agents invoquer ? »
> - « Quelle est la différence entre les modèles de flux de travail Supervisor et Sequential ? »
> - « Comment puis-je personnaliser le comportement de planification du Superviseur ? »

#### Comprendre la sortie

Lorsque vous exécutez la démonstration, vous verrez un parcours structuré montrant comment le Superviseur orchestre plusieurs agents. Voici ce que signifie chaque section :

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**L’en-tête** présente le concept de flux de travail : une chaîne focalisée allant de la lecture de fichiers à la génération de rapports.

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
  
**Diagramme du flux de travail** montre le flux de données entre les agents. Chaque agent a un rôle spécifique :  
- **FileAgent** lit les fichiers en utilisant les outils MCP et stocke le contenu brut dans `fileContent`  
- **ReportAgent** consomme ce contenu et produit un rapport structuré dans `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Demande utilisateur** montre la tâche. Le Superviseur l’analyse et décide d’invoquer FileAgent → ReportAgent.

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
  
**Orchestration du Superviseur** montre le flux en 2 étapes en action :  
1. **FileAgent** lit le fichier via MCP et stocke le contenu  
2. **ReportAgent** reçoit le contenu et génère un rapport structuré

Le Superviseur a pris ces décisions **de manière autonome** selon la requête de l’utilisateur.

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
  
#### Explication des fonctionnalités du module agentic

L’exemple démontre plusieurs fonctionnalités avancées du module agentic. Regardons de plus près Agentic Scope et les Agent Listeners.

**Agentic Scope** montre la mémoire partagée où les agents stockent leurs résultats en utilisant `@Agent(outputKey="...")`. Cela permet :  
- aux agents ultérieurs d’accéder aux sorties des agents précédents  
- au Superviseur de synthétiser une réponse finale  
- à vous d’inspecter ce que chaque agent a produit

Le diagramme ci-dessous montre comment Agentic Scope fonctionne comme mémoire partagée dans le flux fichier-vers-rapport — FileAgent écrit sa sortie sous la clé `fileContent`, ReportAgent la lit et écrit sa propre sortie sous `report` :

<img src="../../../translated_images/fr/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope agit comme une mémoire partagée — FileAgent écrit `fileContent`, ReportAgent la lit puis écrit `report`, et votre code lit le résultat final.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Données brutes du fichier provenant de FileAgent
String report = scope.readState("report");            // Rapport structuré de ReportAgent
```
  
**Agent Listeners** permettent la surveillance et le débogage de l’exécution des agents. La sortie étape par étape que vous voyez dans la démo provient d’un AgentListener qui s’accroche à chaque invocation d’agent :  
- **beforeAgentInvocation** - Appelé lorsque le Superviseur sélectionne un agent, vous permettant de voir quel agent a été choisi et pourquoi  
- **afterAgentInvocation** - Appelé lorsqu’un agent termine, affichant son résultat  
- **inheritedBySubagents** - Lorsque vrai, le listener surveille tous les agents de la hiérarchie

Le diagramme suivant montre le cycle de vie complet de l’Agent Listener, incluant comment `onError` gère les échecs durant l’exécution d’un agent :

<img src="../../../translated_images/fr/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Les Agent Listeners s’intègrent au cycle de vie de l’exécution — ils surveillent quand les agents démarrent, terminent ou rencontrent des erreurs.*

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
  
Au-delà du modèle Superviseur, le module `langchain4j-agentic` fournit plusieurs modèles puissants de flux de travail. Le diagramme ci-dessous montre les cinq — des pipelines simples séquentiels jusqu’aux flux de travail d’approbation avec intervention humaine :

<img src="../../../translated_images/fr/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Cinq modèles de flux de travail pour orchestrer les agents — des pipelines séquentiels simples aux flux d’approbation avec intervention humaine.*

| Modèle                | Description                         | Cas d’usage                              |
|-----------------------|-----------------------------------|----------------------------------------|
| **Séquentiel**         | Exécute les agents dans l’ordre, la sortie est transmise au suivant | Pipelines : rechercher → analyser → rapport |
| **Parallèle**          | Exécute les agents simultanément  | Tâches indépendantes : météo + actualités + bourse |
| **Boucle**             | Itère jusqu’à ce qu’une condition soit remplie | Évaluation de qualité : affiner jusqu’à score ≥ 0.8 |
| **Conditionnel**       | Oriente en fonction des conditions | Classifier → orienter vers un agent spécialiste |
| **Intervention humaine** | Ajoute des points de contrôle humains | Processus d’approbation, revue de contenu |

## Concepts clés

Maintenant que vous avez exploré MCP et le module agentic en action, résumons quand utiliser chaque approche.

L’un des plus grands avantages de MCP est son écosystème en expansion. Le diagramme ci-dessous montre comment un protocole universel unique connecte votre application IA à une grande variété de serveurs MCP — de l’accès au système de fichiers et base de données à GitHub, email, scraping web, et plus :

<img src="../../../translated_images/fr/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP crée un écosystème de protocole universel — tout serveur compatible MCP fonctionne avec tout client compatible MCP, permettant le partage d’outils entre applications.*

**MCP** est idéal lorsque vous souhaitez tirer parti d’écosystèmes d’outils existants, construire des outils que plusieurs applications peuvent partager, intégrer des services tiers via des protocoles standard, ou échanger des implémentations d’outils sans changer le code.

**Le module agentic** est le mieux adapté lorsque vous souhaitez des définitions d’agents déclaratives avec les annotations `@Agent`, avez besoin d’orchestration de flux de travail (séquentiel, boucles, parallèle), préférez la conception d’agents basée sur interfaces plutôt que du code impératif, ou combinez plusieurs agents partageant des sorties via `outputKey`.

**Le modèle d’Agent Superviseur** brille quand le flux de travail n’est pas prévisible à l’avance et que vous voulez que le LLM décide, quand vous avez plusieurs agents spécialisés nécessitant une orchestration dynamique, pour construire des systèmes conversationnels qui orientent vers différentes capacités, ou lorsque vous voulez un comportement d’agent le plus flexible et adaptatif.

Pour vous aider à choisir entre les méthodes personnalisées `@Tool` du Module 04 et les outils MCP de ce module, la comparaison suivante met en lumière les principaux compromis — les outils personnalisés offrent un couplage étroit et une sécurité de type complète pour la logique spécifique à l’application, tandis que les outils MCP offrent des intégrations standardisées et réutilisables :

<img src="../../../translated_images/fr/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Quand utiliser les méthodes personnalisées @Tool vs les outils MCP — outils personnalisés pour la logique spécifique à l’application avec sécurité de type complète, outils MCP pour des intégrations standardisées fonctionnant entre applications.*

## Félicitations !

Vous avez terminé les cinq modules du cours LangChain4j pour débutants ! Voici un aperçu du parcours d’apprentissage complet que vous avez suivi — du chat basique jusqu’aux systèmes agentic propulsés par MCP :

<img src="../../../translated_images/fr/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Votre parcours d’apprentissage à travers les cinq modules — du chat basique aux systèmes agentic propulsés par MCP.*

Vous avez terminé le cours LangChain4j pour débutants. Vous avez appris :

- Comment construire une IA conversationnelle avec mémoire (Module 01)  
- Les modèles d’ingénierie de prompt pour différentes tâches (Module 02)  
- Ancrer les réponses dans vos documents avec RAG (Module 03)  
- Créer des agents IA basiques (assistants) avec des outils personnalisés (Module 04)  
- Intégrer des outils standardisés avec LangChain4j MCP et les modules Agentic (Module 05)

### Et après ?

Après avoir terminé les modules, explorez le [Guide de test](../docs/TESTING.md) pour voir les concepts de test LangChain4j en action.

**Ressources officielles :**  
- [Documentation LangChain4j](https://docs.langchain4j.dev/) – Guides complets et référence API  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) – Code source et exemples  
- [Tutoriels LangChain4j](https://docs.langchain4j.dev/tutorials/) – Tutoriels pas à pas pour divers cas d’usage

Merci d’avoir suivi ce cours !

---

**Navigation :** [← Précédent : Module 04 - Outils](../04-tools/README.md) | [Retour au début](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :  
Ce document a été traduit à l’aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforcions d’assurer l’exactitude, veuillez noter que les traductions automatiques peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue native doit être considéré comme la source faisant foi. Pour toute information critique, il est recommandé de recourir à une traduction professionnelle humaine. Nous déclinons toute responsabilité en cas de malentendus ou d’interprétations erronées résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->