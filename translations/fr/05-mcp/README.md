# Module 05 : Protocole de Contexte du Modèle (MCP)

## Table des matières

- [Ce que vous apprendrez](../../../05-mcp)
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
  - [Et ensuite ?](../../../05-mcp)

## Ce que vous apprendrez

Vous avez construit une IA conversationnelle, maîtrisé les prompts, ancré les réponses dans des documents, et créé des agents avec des outils. Mais tous ces outils étaient conçus sur mesure pour votre application spécifique. Que se passerait-il si vous pouviez donner à votre IA un accès à un écosystème standardisé d’outils que n’importe qui peut créer et partager ? Dans ce module, vous apprendrez à faire exactement cela avec le Protocole de Contexte du Modèle (MCP) et le module agentique de LangChain4j. Nous présentons d’abord un lecteur de fichiers MCP simple puis montrons comment il s’intègre facilement dans des flux de travail agentiques avancés en utilisant le modèle d’Agent superviseur.

## Qu'est-ce que le MCP ?

Le Protocole de Contexte du Modèle (MCP) offre exactement cela : une méthode standard pour que les applications d’IA découvrent et utilisent des outils externes. Au lieu d’écrire des intégrations personnalisées pour chaque source de données ou service, vous vous connectez à des serveurs MCP qui exposent leurs capacités dans un format cohérent. Votre agent d’IA peut alors découvrir et utiliser ces outils automatiquement.

<img src="../../../translated_images/fr/mcp-comparison.9129a881ecf10ff5.webp" alt="Comparaison MCP" width="800"/>

*Avant MCP : Intégrations complexes point à point. Après MCP : Un protocole, des possibilités infinies.*

MCP résout un problème fondamental dans le développement d’IA : chaque intégration est personnalisée. Vous voulez accéder à GitHub ? Code personnalisé. Vous voulez lire des fichiers ? Code personnalisé. Vous voulez interroger une base de données ? Code personnalisé. Et aucune de ces intégrations ne fonctionne avec d’autres applications d’IA.

MCP standardise cela. Un serveur MCP expose des outils avec des descriptions claires et des schémas. Tout client MCP peut se connecter, découvrir les outils disponibles et les utiliser. Construisez une fois, utilisez partout.

<img src="../../../translated_images/fr/mcp-architecture.b3156d787a4ceac9.webp" alt="Architecture MCP" width="800"/>

*Architecture du Protocole de Contexte du Modèle – découverte et exécution d’outils standardisées*

## Comment fonctionne le MCP

<img src="../../../translated_images/fr/mcp-protocol-detail.01204e056f45308b.webp" alt="Détail du protocole MCP" width="800"/>

*Comment fonctionne le MCP en coulisses — les clients découvrent les outils, échangent des messages JSON-RPC, et exécutent les opérations via une couche de transport.*

**Architecture Client-Serveur**

MCP utilise un modèle client-serveur. Les serveurs fournissent des outils - lecture de fichiers, interrogation de bases de données, appels d’API. Les clients (votre application IA) se connectent aux serveurs et utilisent leurs outils.

Pour utiliser MCP avec LangChain4j, ajoutez cette dépendance Maven :

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Découverte des outils**

Lorsque votre client se connecte à un serveur MCP, il demande : "Quels outils avez-vous ?" Le serveur répond avec une liste d’outils disponibles, chacun avec des descriptions et des schémas de paramètres. Votre agent d’IA peut alors décider quels outils utiliser en fonction des demandes des utilisateurs.

<img src="../../../translated_images/fr/tool-discovery.07760a8a301a7832.webp" alt="Découverte d'outils MCP" width="800"/>

*L’IA découvre les outils disponibles au démarrage — elle sait désormais quelles capacités sont à disposition et peut décider lesquelles utiliser.*

**Mécanismes de transport**

MCP supporte différents mécanismes de transport. Ce module démontre le transport Stdio pour les processus locaux :

<img src="../../../translated_images/fr/transport-mechanisms.2791ba7ee93cf020.webp" alt="Mécanismes de transport" width="800"/>

*Mécanismes de transport MCP : HTTP pour serveurs distants, Stdio pour processus locaux*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Pour les processus locaux. Votre application lance un serveur en tant que sous-processus et communique via l'entrée/sortie standard. Utile pour l’accès au système de fichiers ou les outils en ligne de commande.

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

<img src="../../../translated_images/fr/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Flux de transport Stdio" width="800"/>

*Transport Stdio en action — votre application lance le serveur MCP en tant que processus enfant et communique via les canaux stdin/stdout.*

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) et demandez :
> - "Comment fonctionne le transport Stdio et quand devrais-je l’utiliser plutôt que HTTP ?"
> - "Comment LangChain4j gère-t-il le cycle de vie des processus du serveur MCP lancés ?"
> - "Quelles sont les implications de sécurité lorsqu’on donne à l’IA l’accès au système de fichiers ?"

## Le module agentique

Alors que MCP fournit des outils standardisés, le module **agentique** de LangChain4j offre une manière déclarative de construire des agents qui orchestrent ces outils. L’annotation `@Agent` et `AgenticServices` vous permettent de définir le comportement des agents via des interfaces plutôt que du code impératif.

Dans ce module, vous explorerez le modèle **Agent superviseur** — une approche agentique avancée où un agent "superviseur" décide dynamiquement quels sous-agents invoquer selon les demandes de l’utilisateur. Nous combinerons les deux concepts en donnant à l’un de nos sous-agents des capacités d’accès aux fichiers via MCP.

Pour utiliser le module agentique, ajoutez cette dépendance Maven :

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ Expérimental :** Le module `langchain4j-agentic` est **expérimental** et sujet à des changements. La façon stable de construire des assistants IA reste `langchain4j-core` avec des outils personnalisés (Module 04).

## Exécution des exemples

### Prérequis

- Java 21+, Maven 3.9+
- Node.js 16+ et npm (pour les serveurs MCP)
- Variables d’environnement configurées dans le fichier `.env` (depuis le répertoire racine) :
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (identiques aux Modules 01-04)

> **Note :** Si vous n’avez pas encore configuré vos variables d’environnement, consultez [Module 00 - Démarrage rapide](../00-quick-start/README.md) pour les instructions, ou copiez `.env.example` en `.env` dans le répertoire racine et remplissez vos valeurs.

## Démarrage rapide

**Avec VS Code :** Il suffit de faire un clic droit sur n’importe quel fichier de démo dans l’Explorateur et de sélectionner **"Run Java"**, ou d’utiliser les configurations de lancement depuis le panneau Exécuter et Déboguer (assurez-vous d’avoir ajouté votre token dans le fichier `.env`).

**Avec Maven :** Vous pouvez aussi exécuter depuis la ligne de commande avec les exemples ci-dessous.

### Opérations sur fichiers (Stdio)

Ceci démontre des outils basés sur des sous-processus locaux.

**✅ Pas de prérequis nécessaires** — le serveur MCP est lancé automatiquement.

**Utilisation des scripts de démarrage (recommandé) :**

Les scripts de démarrage chargent automatiquement les variables d’environnement depuis le fichier `.env` racine :

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

**Avec VS Code :** Faites un clic droit sur `StdioTransportDemo.java` et sélectionnez **"Run Java"** (assurez-vous que votre fichier `.env` est configuré).

L’application lance automatiquement un serveur MCP système de fichiers et lit un fichier local. Remarquez la gestion automatique des sous-processus.

**Sortie attendue :**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agent superviseur

Le modèle **Agent superviseur** est une forme **flexible** de l’IA agentique. Un Superviseur utilise un LLM pour décider de manière autonome quels agents invoquer en fonction de la demande utilisateur. Dans l’exemple suivant, nous combinons l’accès aux fichiers via MCP avec un agent LLM pour créer un flux supervisé lecture de fichier → rapport.

Dans la démo, `FileAgent` lit un fichier en utilisant les outils système MCP, et `ReportAgent` génère un rapport structuré avec un résumé exécutif (1 phrase), 3 points clés, et des recommandations. Le Superviseur orchestre ce flux automatiquement :

<img src="../../../translated_images/fr/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Modèle Agent superviseur" width="800"/>

*Le Superviseur utilise son LLM pour décider quels agents invoquer et dans quel ordre — aucun routage codé en dur nécessaire.*

Voici à quoi ressemble concrètement le workflow pour notre pipeline fichier → rapport :

<img src="../../../translated_images/fr/file-report-workflow.649bb7a896800de9.webp" alt="Workflow fichier vers rapport" width="800"/>

*FileAgent lit le fichier via les outils MCP, puis ReportAgent transforme le contenu brut en rapport structuré.*

Chaque agent stocke son résultat dans la **Portée Agentique** (mémoire partagée), ce qui permet aux agents en aval d’accéder aux résultats précédents. Cela montre comment les outils MCP s’intègrent parfaitement dans des flux agentiques — le Superviseur n’a pas besoin de savoir *comment* les fichiers sont lus, seulement que `FileAgent` peut le faire.

#### Exécution de la démo

Les scripts de démarrage chargent automatiquement les variables d’environnement depuis le fichier `.env` racine :

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

**Avec VS Code :** Faites un clic droit sur `SupervisorAgentDemo.java` et sélectionnez **"Run Java"** (assurez-vous que votre fichier `.env` est configuré).

#### Comment fonctionne le superviseur

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

// Le Superviseur orchestre le flux de fichiers → rapports
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Retourner le rapport final
        .build();

// Le Superviseur décide quels agents invoquer en fonction de la demande
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Stratégies de réponse

Lorsque vous configurez un `SupervisorAgent`, vous spécifiez comment il doit formuler sa réponse finale à l’utilisateur après que les sous-agents ont accompli leurs tâches.

<img src="../../../translated_images/fr/response-strategies.3d0cea19d096bdf9.webp" alt="Stratégies de réponse" width="800"/>

*Trois stratégies pour la formulation de la réponse finale par le Superviseur — choisissez selon que vous souhaitez la sortie du dernier agent, un résumé synthétisé, ou l’option la mieux notée.*

Les stratégies disponibles sont :

| Stratégie | Description |
|-----------|-------------|
| **LAST**  | Le superviseur retourne la sortie du dernier sous-agent ou outil appelé. Utile lorsque l’agent final du workflow est spécifiquement conçu pour produire la réponse complète finale (par ex. un "Agent Résumé" dans un pipeline de recherche). |
| **SUMMARY** | Le superviseur utilise son propre modèle de langage interne (LLM) pour synthétiser un résumé de l’interaction entière et de toutes les sorties des sous-agents, puis retourne ce résumé comme réponse finale. Cela fournit une réponse claire et agrégée à l’utilisateur. |
| **SCORED** | Le système utilise un LLM interne pour noter à la fois la dernière réponse (LAST) et le résumé (SUMMARY) par rapport à la demande originale de l’utilisateur, et retourne la sortie ayant la meilleure note. |

Voir [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) pour l’implémentation complète.

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) et demandez :
> - "Comment le Superviseur décide-t-il quels agents invoquer ?"
> - "Quelle est la différence entre les modèles Superviseur et Workflow Séquentiel ?"
> - "Comment puis-je personnaliser le comportement de planification du Superviseur ?"

#### Comprendre la sortie

Lorsque vous exécutez la démo, vous verrez un déroulé structuré montrant comment le Superviseur orchestre plusieurs agents. Voici ce que chaque section signifie :

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**L’en-tête** présente le concept de workflow : un pipeline ciblé de la lecture de fichier à la génération de rapport.

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

**Diagramme du workflow** montre le flux de données entre agents. Chaque agent a un rôle spécifique :
- **FileAgent** lit les fichiers via les outils MCP et stocke le contenu brut dans `fileContent`
- **ReportAgent** consomme ce contenu et produit un rapport structuré dans `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Demande utilisateur** montre la tâche. Le Superviseur l’analyse et décide d’invoquer FileAgent puis ReportAgent.

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

Le Superviseur a pris ces décisions **autonomement** au regard de la demande utilisateur.

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

#### Explication des fonctionnalités du module agentique

L’exemple montre plusieurs fonctionnalités avancées du module agentique. Regardons de plus près la Portée Agentique et les Écouteurs d’agents.

**La Portée Agentique** montre la mémoire partagée où les agents ont stocké leurs résultats avec `@Agent(outputKey="...")`. Cela permet :
- Aux agents ultérieurs d’accéder aux sorties des agents précédents
- Au Superviseur de synthétiser une réponse finale
- À vous d’inspecter ce que chaque agent a produit

<img src="../../../translated_images/fr/agentic-scope.95ef488b6c1d02ef.webp" alt="Mémoire partagée Portée Agentique" width="800"/>

*La Portée Agentique agit comme une mémoire partagée — FileAgent écrit `fileContent`, ReportAgent le lit et écrit `report`, et votre code lit le résultat final.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Données brutes du fichier de FileAgent
String report = scope.readState("report");            // Rapport structuré de ReportAgent
```

**Les Écouteurs d’agents** permettent de surveiller et déboguer l’exécution des agents. La sortie pas-à-pas que vous voyez dans la démo provient d’un AgentListener qui intercepte chaque invocation d’agent :
- **beforeAgentInvocation** - Appelé lorsque le Superviseur sélectionne un agent, vous permettant de voir quel agent a été choisi et pourquoi  
- **afterAgentInvocation** - Appelé lorsqu'un agent termine, montrant son résultat  
- **inheritedBySubagents** - Lorsque vrai, le listener surveille tous les agents dans la hiérarchie  

<img src="../../../translated_images/fr/agent-listeners.784bfc403c80ea13.webp" alt="Cycle de vie des Agent Listeners" width="800"/>

*Les Agent Listeners s'intègrent au cycle d'exécution — ils surveillent quand les agents démarrent, terminent ou rencontrent des erreurs.*

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
  
Au-delà du pattern Superviseur, le module `langchain4j-agentic` propose plusieurs patterns de workflow puissants et des fonctionnalités :  

<img src="../../../translated_images/fr/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Patterns de Workflow des Agents" width="800"/>

*Cinq patterns de workflow pour orchestrer les agents — des pipelines séquentiels simples aux workflows d'approbation avec intervention humaine.*

| Pattern | Description | Cas d’usage |
|---------|-------------|-------------|
| **Séquentiel** | Exécute les agents dans l’ordre, la sortie alimente le suivant | Pipelines : recherche → analyse → rapport |
| **Parallèle** | Exécute les agents simultanément | Tâches indépendantes : météo + actualités + actions |
| **Boucle** | Itère jusqu'à ce que la condition soit remplie | Évaluation qualité : affiner jusqu’à un score ≥ 0.8 |
| **Conditionnel** | Oriente selon des conditions | Classifier → route vers agent spécialiste |
| **Humain dans la boucle** | Ajoute des points de contrôle humains | Workflows d’approbation, relecture de contenu |

## Concepts clés

Maintenant que vous avez exploré MCP et le module agentic en action, résumons quand utiliser chaque approche.

<img src="../../../translated_images/fr/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="Écosystème MCP" width="800"/>

*MCP crée un écosystème de protocoles universels — tout serveur compatible MCP fonctionne avec tout client compatible MCP, permettant le partage d’outils entre applications.*

**MCP** est idéal lorsque vous souhaitez exploiter des écosystèmes d’outils existants, créer des outils pouvant être partagés entre plusieurs applications, intégrer des services tiers via des protocoles standards, ou changer les implémentations d’outils sans modifier le code.

**Le Module Agentic** est le mieux adapté lorsque vous souhaitez des définitions d’agents déclaratives avec les annotations `@Agent`, avez besoin d’orchestration de workflow (séquentiel, boucle, parallèle), préférez la conception d’agents basée sur des interfaces plutôt que sur du code impératif, ou combinez plusieurs agents partageant leurs sorties via `outputKey`.

**Le pattern Agent Superviseur** est pertinent lorsque le workflow n’est pas prévisible à l’avance et que vous voulez que le LLM décide, lorsque vous avez plusieurs agents spécialisés nécessitant une orchestration dynamique, lors de la création de systèmes conversationnels qui redirigent vers différentes capacités, ou lorsque vous souhaitez le comportement d’agent le plus flexible et adaptatif.

<img src="../../../translated_images/fr/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Outils personnalisés vs Outils MCP" width="800"/>

*Quand utiliser des méthodes @Tool personnalisées vs des outils MCP — outils personnalisés pour la logique spécifique à l’application avec une sécurité de type complète, outils MCP pour des intégrations standardisées qui fonctionnent à travers plusieurs applications.*

## Félicitations !

<img src="../../../translated_images/fr/course-completion.48cd201f60ac7570.webp" alt="Fin du cours" width="800"/>

*Votre parcours d’apprentissage à travers les cinq modules — du chat basique aux systèmes agentic propulsés par MCP.*

Vous avez terminé le cours LangChain4j pour débutants. Vous avez appris :

- Comment construire une IA conversationnelle avec mémoire (Module 01)  
- Les patterns de prompt engineering pour diverses tâches (Module 02)  
- Comment ancrer les réponses dans vos documents avec RAG (Module 03)  
- Créer des agents d’IA basiques (assistants) avec des outils personnalisés (Module 04)  
- Intégrer des outils standardisés avec les modules LangChain4j MCP et Agentic (Module 05)  

### Et après ?

Après avoir terminé les modules, explorez le [Guide de Test](../docs/TESTING.md) pour voir les concepts de tests LangChain4j en action.

**Ressources officielles :**  
- [Documentation LangChain4j](https://docs.langchain4j.dev/) - Guides complets et référence API  
- [LangChain4j sur GitHub](https://github.com/langchain4j/langchain4j) - Code source et exemples  
- [Tutoriels LangChain4j](https://docs.langchain4j.dev/tutorials/) - Tutoriels pas à pas pour divers cas d’usage  

Merci d’avoir suivi ce cours !

---

**Navigation :** [← Précédent : Module 04 - Outils](../04-tools/README.md) | [Retour à l’accueil](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :  
Ce document a été traduit à l’aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforcions d’assurer l’exactitude, veuillez noter que les traductions automatiques peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue d’origine doit être considéré comme la source faisant foi. Pour les informations cruciales, il est recommandé de recourir à une traduction professionnelle réalisée par un humain. Nous déclinons toute responsabilité en cas de malentendus ou d’interprétations erronées résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->