# LangChain4j Glossaire

## Table des Matières

- [Concepts de Base](../../../docs)
- [Composants LangChain4j](../../../docs)
- [Concepts AI/ML](../../../docs)
- [Guardrails](../../../docs)
- [Ingénierie des Prompts](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agents et Outils](../../../docs)
- [Module Agentique](../../../docs)
- [Protocole de Contexte Modèle (MCP)](../../../docs)
- [Services Azure](../../../docs)
- [Tests et Développement](../../../docs)

Référence rapide pour les termes et concepts utilisés tout au long du cours.

## Concepts de Base

**Agent IA** - Système utilisant l'IA pour raisonner et agir de manière autonome. [Module 04](../04-tools/README.md)

**Chaîne** - Séquence d'opérations où la sortie alimente l'étape suivante.

**Découpage** - Fragmenter les documents en morceaux plus petits. Typiquement : 300-500 tokens avec chevauchement. [Module 03](../03-rag/README.md)

**Fenêtre de Contexte** - Nombre maximal de tokens qu'un modèle peut traiter. GPT-5.2 : 400K tokens (jusqu'à 272K en entrée, 128K en sortie).

**Embeddings** - Vecteurs numériques représentant la signification du texte. [Module 03](../03-rag/README.md)

**Appel de Fonction** - Le modèle génère des requêtes structurées pour appeler des fonctions externes. [Module 04](../04-tools/README.md)

**Hallucination** - Quand les modèles génèrent des informations incorrectes mais plausibles.

**Prompt** - Texte d’entrée donné à un modèle de langage. [Module 02](../02-prompt-engineering/README.md)

**Recherche Sémantique** - Recherche par signification utilisant des embeddings, pas des mots-clés. [Module 03](../03-rag/README.md)

**Avec état vs Sans état** - Sans état : pas de mémoire. Avec état : maintien de l’historique de la conversation. [Module 01](../01-introduction/README.md)

**Tokens** - Unités de texte de base traitées par les modèles. Impacte les coûts et les limites. [Module 01](../01-introduction/README.md)

**Chaînage d’Outils** - Exécution séquentielle d’outils où la sortie informe l’appel suivant. [Module 04](../04-tools/README.md)

## Composants LangChain4j

**AiServices** - Crée des interfaces de services IA typées.

**OpenAiOfficialChatModel** - Client unifié pour les modèles OpenAI et Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Crée des embeddings utilisant le client officiel OpenAI (supporte OpenAI et Azure OpenAI).

**ChatModel** - Interface centrale pour les modèles de langage.

**ChatMemory** - Maintient l’historique des conversations.

**ContentRetriever** - Trouve des fragments documents pertinents pour RAG.

**DocumentSplitter** - Divise les documents en fragments.

**EmbeddingModel** - Convertit le texte en vecteurs numériques.

**EmbeddingStore** - Stocke et récupère des embeddings.

**MessageWindowChatMemory** - Maintient une fenêtre glissante des messages récents.

**PromptTemplate** - Crée des prompts réutilisables avec des espaces réservés `{{variable}}`.

**TextSegment** - Fragment de texte avec métadonnées. Utilisé en RAG.

**ToolExecutionRequest** - Représente une requête d'exécution d'outil.

**UserMessage / AiMessage / SystemMessage** - Types de messages de conversation.

## Concepts AI/ML

**Apprentissage Few-Shot** - Fournir des exemples dans les prompts. [Module 02](../02-prompt-engineering/README.md)

**Grand Modèle de Langage (LLM)** - Modèles IA entraînés sur d’immenses volumes de texte.

**Effort de Raisonnement** - Paramètre GPT-5.2 contrôlant la profondeur de la réflexion. [Module 02](../02-prompt-engineering/README.md)

**Température** - Contrôle la variabilité des sorties. Faible = déterministe, élevé = créatif.

**Base de Données Vectorielle** - Base spécialisée pour les embeddings. [Module 03](../03-rag/README.md)

**Apprentissage Zero-Shot** - Effectuer des tâches sans exemples. [Module 02](../02-prompt-engineering/README.md)

## Guardrails - [Module 00](../00-quick-start/README.md)

**Défense en Profondeur** - Approche de sécurité multi-couches combinant guardrails applicatifs et filtres de sécurité fournisseur.

**Blocage Strict** - Le fournisseur retourne une erreur HTTP 400 pour violations graves de contenu.

**InputGuardrail** - Interface LangChain4j pour valider la saisie utilisateur avant qu'elle n'atteigne le LLM. Économise coûts et latence en bloquant tôt les prompts nuisibles.

**InputGuardrailResult** - Type de retour pour la validation guardrail : `success()` ou `fatal("raison")`.

**OutputGuardrail** - Interface pour valider les réponses IA avant de les retourner aux utilisateurs.

**Filtres de Sécurité Fournisseur** - Filtres intégrés fournis par les fournisseurs d’IA (ex. GitHub Models) qui détectent les violations au niveau API.

**Refus Doux** - Le modèle refuse poliment de répondre sans générer d’erreur.

## Ingénierie des Prompts - [Module 02](../02-prompt-engineering/README.md)

**Chaîne de Raisonnement** - Raisonnement étape par étape pour une meilleure précision.

**Sortie Contraint** - Imposer un format ou une structure spécifique.

**Forte Volonté** - Patron GPT-5.2 pour un raisonnement approfondi.

**Faible Volonté** - Patron GPT-5.2 pour des réponses rapides.

**Conversation Multi-Tours** - Maintien du contexte sur plusieurs échanges.

**Prompting Basé sur les Rôles** - Définir la persona du modèle via des messages système.

**Autoréflexion** - Le modèle évalue et améliore ses propres sorties.

**Analyse Structurée** - Cadre d’évaluation fixe.

**Patron d’Exécution de Tâche** - Planifier → Exécuter → Résumer.

## RAG (Retrieval-Augmented Generation) - [Module 03](../03-rag/README.md)

**Pipeline de Traitement de Document** - Charger → découper → encoder → stocker.

**Magasin d’Embeddings en Mémoire** - Stockage non persistant pour les tests.

**RAG** - Combine la récupération avec la génération pour ancrer les réponses.

**Score de Similarité** - Mesure (0-1) de similarité sémantique.

**Référence Source** - Métadonnées sur le contenu récupéré.

## Agents et Outils - [Module 04](../04-tools/README.md)

**Annotation @Tool** - Marque les méthodes Java comme outils appelables par l’IA.

**Patron ReAct** - Raisonner → Agir → Observer → Répéter.

**Gestion de Session** - Contextes séparés pour différents utilisateurs.

**Outil** - Fonction qu’un agent IA peut appeler.

**Description d’Outil** - Documentation sur l’usage et les paramètres de l’outil.

## Module Agentique - [Module 05](../05-mcp/README.md)

**Annotation @Agent** - Marque les interfaces comme agents IA avec définition de comportement déclarative.

**Agent Listener** - Crochet pour surveiller l’exécution agent via `beforeAgentInvocation()` et `afterAgentInvocation()`.

**Portée Agentique** - Mémoire partagée où les agents stockent leurs sorties sous `outputKey` pour consommation par d’autres agents.

**AgenticServices** - Fabrique pour créer des agents via `agentBuilder()` et `supervisorBuilder()`.

**Flux Conditionnel** - Dirige selon des conditions vers différents agents spécialistes.

**Humain dans la Boucle** - Patron de flux ajoutant des points de contrôle humains pour approbation ou revue de contenu.

**langchain4j-agentic** - Dépendance Maven pour construction déclarative d’agents (expérimental).

**Boucle de Flux** - Itérer l’exécution de l’agent jusqu’à ce qu’une condition soit remplie (ex. score qualité ≥ 0.8).

**outputKey** - Paramètre d’annotation agent définissant où les résultats sont stockés dans la Portée Agentique.

**Flux Parallèle** - Exécuter plusieurs agents simultanément pour des tâches indépendantes.

**Stratégie de Réponse** - Façon dont le superviseur formule la réponse finale : LAST, SUMMARY, ou SCORED.

**Flux Séquentiel** - Exécuté agents en ordre où la sortie alimente l’étape suivante.

**Patron Agent Superviseur** - Patron agentique avancé où un LLM superviseur décide dynamiquement quels sous-agents invoquer.

## Protocole de Contexte Modèle (MCP) - [Module 05](../05-mcp/README.md)

**langchain4j-mcp** - Dépendance Maven pour intégration MCP dans LangChain4j.

**MCP** - Protocole de Contexte Modèle : standard pour connecter les applis IA à des outils externes. Construire une fois, utiliser partout.

**Client MCP** - Application qui se connecte aux serveurs MCP pour découvrir et utiliser les outils.

**Serveur MCP** - Service exposant des outils via MCP avec descriptions claires et schémas de paramètres.

**McpToolProvider** - Composant LangChain4j qui encapsule les outils MCP pour usage dans services IA et agents.

**McpTransport** - Interface pour communication MCP. Implémentations incluent Stdio et HTTP.

**Transport Stdio** - Transport local par processus via stdin/stdout. Utile pour accès système de fichiers ou outils ligne de commande.

**StdioMcpTransport** - Implémentation LangChain4j lançant le serveur MCP en sous-processus.

**Découverte d’Outils** - Client interroge le serveur pour les outils disponibles avec descriptions et schémas.

## Services Azure - [Module 01](../01-introduction/README.md)

**Azure AI Search** - Recherche cloud avec capacités vectorielles. [Module 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Déploie des ressources Azure.

**Azure OpenAI** - Service IA entreprise de Microsoft.

**Bicep** - Langage d’infrastructure Azure-as-code. [Guide Infrastructure](../01-introduction/infra/README.md)

**Nom de Déploiement** - Nom du déploiement modèle dans Azure.

**GPT-5.2** - Dernier modèle OpenAI avec contrôle du raisonnement. [Module 02](../02-prompt-engineering/README.md)

## Tests et Développement - [Guide de Tests](TESTING.md)

**Dev Container** - Environnement de développement containerisé. [Configuration](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Terrain de jeu AI modèle gratuit. [Module 00](../00-quick-start/README.md)

**Test en Mémoire** - Tests avec stockage en mémoire.

**Tests d’Intégration** - Tests avec infrastructure réelle.

**Maven** - Outil d’automatisation de construction Java.

**Mockito** - Framework de moquage Java.

**Spring Boot** - Framework d’application Java. [Module 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avis de non-responsabilité** :  
Ce document a été traduit à l'aide du service de traduction par IA [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforçons d'assurer l'exactitude, veuillez noter que les traductions automatisées peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue native doit être considéré comme la source faisant foi. Pour les informations critiques, une traduction professionnelle réalisée par un humain est recommandée. Nous déclinons toute responsabilité en cas de malentendus ou de mauvaises interprétations résultant de l'utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->