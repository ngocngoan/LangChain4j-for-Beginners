# Glossaire LangChain4j

## Table des matières

- [Concepts de base](../../../docs)
- [Composants LangChain4j](../../../docs)
- [Concepts AI/ML](../../../docs)
- [Garde-fous](../../../docs)
- [Ingénierie de prompt](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agents et Outils](../../../docs)
- [Module Agentique](../../../docs)
- [Protocole de Contexte du Modèle (MCP)](../../../docs)
- [Services Azure](../../../docs)
- [Tests et Développement](../../../docs)

Référence rapide des termes et concepts utilisés tout au long du cours.

## Concepts de base

**Agent IA** - Système qui utilise l'IA pour raisonner et agir de manière autonome. [Module 04](../04-tools/README.md)

**Chaîne** - Séquence d'opérations où la sortie alimente l'étape suivante.

**Découpage** - Fractionnement des documents en morceaux plus petits. Typique : 300-500 tokens avec chevauchement. [Module 03](../03-rag/README.md)

**Fenêtre de contexte** - Nombre maximal de tokens qu'un modèle peut traiter. GPT-5.2 : 400K tokens.

**Embeddings** - Vecteurs numériques représentant le sens du texte. [Module 03](../03-rag/README.md)

**Appel de fonction** - Le modèle génère des requêtes structurées pour appeler des fonctions externes. [Module 04](../04-tools/README.md)

**Hallucination** - Lorsque les modèles génèrent des informations incorrectes mais plausibles.

**Prompt** - Texte d'entrée pour un modèle de langage. [Module 02](../02-prompt-engineering/README.md)

**Recherche sémantique** - Recherche par sens utilisant des embeddings, pas des mots-clés. [Module 03](../03-rag/README.md)

**Avec état vs sans état** - Sans état : pas de mémoire. Avec état : conserve l'historique de la conversation. [Module 01](../01-introduction/README.md)

**Tokens** - Unités de base de texte que les modèles traitent. Impacte les coûts et limites. [Module 01](../01-introduction/README.md)

**Chaînage d’outils** - Exécution séquentielle d’outils où la sortie informe l’appel suivant. [Module 04](../04-tools/README.md)

## Composants LangChain4j

**AiServices** - Crée des interfaces de services IA typées.

**OpenAiOfficialChatModel** - Client unifié pour les modèles OpenAI et Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Crée des embeddings avec le client officiel OpenAI (prend en charge OpenAI et Azure OpenAI).

**ChatModel** - Interface centrale pour les modèles de langage.

**ChatMemory** - Conserve l'historique des conversations.

**ContentRetriever** - Trouve des segments de documents pertinents pour RAG.

**DocumentSplitter** - Fractionne les documents en segments.

**EmbeddingModel** - Convertit le texte en vecteurs numériques.

**EmbeddingStore** - Stocke et récupère les embeddings.

**MessageWindowChatMemory** - Conserve une fenêtre glissante des messages récents.

**PromptTemplate** - Crée des prompts réutilisables avec des variables `{{variable}}`.

**TextSegment** - Morceau de texte avec métadonnées. Utilisé dans RAG.

**ToolExecutionRequest** - Représente une requête d’exécution d’outil.

**UserMessage / AiMessage / SystemMessage** - Types de messages dans la conversation.

## Concepts AI/ML

**Apprentissage avec quelques exemples (Few-Shot)** - Fournir des exemples dans les prompts. [Module 02](../02-prompt-engineering/README.md)

**Grand Modèle de Langage (LLM)** - Modèles IA entraînés sur de grandes quantités de texte.

**Effort de raisonnement** - Paramètre GPT-5.2 contrôlant la profondeur de la réflexion. [Module 02](../02-prompt-engineering/README.md)

**Température** - Contrôle l'aléatoire des sorties. Faible = déterministe, élevé = créatif.

**Base de données vectorielle** - Base spécialisée pour les embeddings. [Module 03](../03-rag/README.md)

**Apprentissage sans exemple (Zero-Shot)** - Exécuter des tâches sans exemples. [Module 02](../02-prompt-engineering/README.md)

## Garde-fous - [Module 00](../00-quick-start/README.md)

**Défense en profondeur** - Approche multicouche combinant garde-fous au niveau applicatif et filtres de sécurité du fournisseur.

**Blocage dur** - Le fournisseur renvoie une erreur HTTP 400 pour des violations graves.

**InputGuardrail** - Interface LangChain4j pour valider les entrées utilisateur avant qu’elles n’atteignent le LLM. Économise coûts et latence en bloquant tôt les prompts nuisibles.

**InputGuardrailResult** - Type de retour pour validation des garde-fous : `success()` ou `fatal("raison")`.

**OutputGuardrail** - Interface pour valider les réponses IA avant de les retourner aux utilisateurs.

**Filtres de sécurité du fournisseur** - Filtres de contenu intégrés par les fournisseurs IA (ex. GitHub Models) qui interceptent les violations au niveau API.

**Refus doux** - Le modèle décline poliment de répondre sans renvoyer d’erreur.

## Ingénierie de prompt - [Module 02](../02-prompt-engineering/README.md)

**Chaîne de pensée** - Raisonnement étape par étape pour une meilleure précision.

**Sortie contrainte** - Imposer un format ou une structure spécifique.

**Forte motivation** - Pattern GPT-5.2 pour un raisonnement approfondi.

**Faible motivation** - Pattern GPT-5.2 pour des réponses rapides.

**Conversation multi-tours** - Maintien du contexte sur plusieurs échanges.

**Prompting basé sur le rôle** - Définir la persona du modèle via des messages système.

**Auto-réflexion** - Le modèle évalue et améliore sa sortie.

**Analyse structurée** - Cadre d’évaluation fixe.

**Modèle d’exécution de tâche** - Planifier → Exécuter → Résumer.

## RAG (Retrieval-Augmented Generation) - [Module 03](../03-rag/README.md)

**Pipeline de traitement documentaire** - Charger → découper → encoder → stocker.

**Stockage d’embeddings en mémoire** - Stockage non persistant pour tests.

**RAG** - Combine récupération et génération pour ancrer les réponses.

**Score de similarité** - Mesure (0-1) de similarité sémantique.

**Référence source** - Métadonnées sur le contenu récupéré.

## Agents et Outils - [Module 04](../04-tools/README.md)

**@Tool Annotation** - Marque les méthodes Java comme outils appelables par l’IA.

**Pattern ReAct** - Raisonner → Agir → Observer → Répéter.

**Gestion de session** - Contextes séparés pour différents utilisateurs.

**Outil** - Fonction qu’un agent IA peut appeler.

**Description d’outil** - Documentation de la fonction et des paramètres de l’outil.

## Module Agentique - [Module 05](../05-mcp/README.md)

**@Agent Annotation** - Marque les interfaces comme agents IA avec définition déclarative du comportement.

**Agent Listener** - Hook pour surveiller l’exécution d’agent via `beforeAgentInvocation()` et `afterAgentInvocation()`.

**Portée agentique** - Mémoire partagée où les agents stockent les résultats via `outputKey` pour consommation par d’autres agents.

**AgenticServices** - Fabrique pour créer des agents via `agentBuilder()` et `supervisorBuilder()`.

**Flux conditionnel** - Routage vers différents agents spécialistes selon conditions.

**Humain dans la boucle** - Pattern de workflow ajoutant des points de contrôle humains pour approbation ou revue de contenu.

**langchain4j-agentic** - Dépendance Maven pour création d’agents déclaratifs (expérimental).

**Boucle de workflow** - Itération de l’exécution d’agent jusqu’à satisfaction d’une condition (ex. score qualité ≥ 0.8).

**outputKey** - Paramètre d’annotation d’agent précisant où stocker les résultats dans la portée agentique.

**Workflow parallèle** - Exécute plusieurs agents simultanément pour tâches indépendantes.

**Stratégie de réponse** - Façon dont le superviseur formule la réponse finale : LAST, SUMMARY, ou SCORED.

**Workflow séquentiel** - Exécute les agents dans l’ordre où la sortie alimente l’étape suivante.

**Pattern superviseur d’agent** - Pattern agentique avancé où un LLM superviseur décide dynamiquement quels sous-agents invoquer.

## Protocole de Contexte du Modèle (MCP) - [Module 05](../05-mcp/README.md)

**langchain4j-mcp** - Dépendance Maven pour l’intégration MCP dans LangChain4j.

**MCP** - Protocole de Contexte du Modèle : standard pour connecter les applications IA à des outils externes. Construisez une fois, utilisez partout.

**Client MCP** - Application se connectant aux serveurs MCP pour découvrir et utiliser des outils.

**Serveur MCP** - Service exposant des outils via MCP avec descriptions claires et schémas de paramètres.

**McpToolProvider** - Composant LangChain4j qui enveloppe les outils MCP pour usage dans services IA et agents.

**McpTransport** - Interface pour communication MCP. Implémentations incluent Stdio et HTTP.

**Transport Stdio** - Transport local via stdin/stdout. Utile pour accès système de fichiers ou outils en ligne de commande.

**StdioMcpTransport** - Implémentation LangChain4j qui lance un serveur MCP en sous-processus.

**Découverte d’outils** - Le client interroge le serveur sur les outils disponibles avec descriptions et schémas.

## Services Azure - [Module 01](../01-introduction/README.md)

**Azure AI Search** - Recherche cloud avec capacités vectorielles. [Module 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Déploie des ressources Azure.

**Azure OpenAI** - Service IA d’entreprise de Microsoft.

**Bicep** - Langage d’infrastructure en tant que code Azure. [Guide d'infrastructure](../01-introduction/infra/README.md)

**Nom de déploiement** - Nom pour le déploiement du modèle dans Azure.

**GPT-5.2** - Dernier modèle OpenAI avec contrôle du raisonnement. [Module 02](../02-prompt-engineering/README.md)

## Tests et Développement - [Guide de test](TESTING.md)

**Dev Container** - Environnement de développement containerisé. [Configuration](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Playground gratuit pour modèles IA. [Module 00](../00-quick-start/README.md)

**Test en mémoire** - Tests avec stockage en mémoire.

**Tests d’intégration** - Tests avec infrastructure réelle.

**Maven** - Outil d’automatisation de build Java.

**Mockito** - Framework de moquage Java.

**Spring Boot** - Framework d’application Java. [Module 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Clause de non-responsabilité** :  
Ce document a été traduit à l’aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforcions d’assurer l’exactitude, veuillez noter que les traductions automatisées peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue native doit être considéré comme la source faisant autorité. Pour les informations cruciales, il est recommandé de recourir à une traduction professionnelle réalisée par un humain. Nous déclinons toute responsabilité en cas de malentendus ou d’interprétations erronées résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->