# Module 02 : Ingénierie des invites avec GPT-5.2

## Table des matières

- [Ce que vous apprendrez](../../../02-prompt-engineering)
- [Prérequis](../../../02-prompt-engineering)
- [Comprendre l'ingénierie des invites](../../../02-prompt-engineering)
- [Comment cela utilise LangChain4j](../../../02-prompt-engineering)
- [Les modèles de base](../../../02-prompt-engineering)
- [Utiliser les ressources Azure existantes](../../../02-prompt-engineering)
- [Captures d'écran de l'application](../../../02-prompt-engineering)
- [Explorer les modèles](../../../02-prompt-engineering)
  - [Faible vs forte impatience](../../../02-prompt-engineering)
  - [Exécution de tâches (préambules d'outils)](../../../02-prompt-engineering)
  - [Code auto-réfléchissant](../../../02-prompt-engineering)
  - [Analyse structurée](../../../02-prompt-engineering)
  - [Chat à tours multiples](../../../02-prompt-engineering)
  - [Raisonnement étape par étape](../../../02-prompt-engineering)
  - [Sortie contrainte](../../../02-prompt-engineering)
- [Ce que vous apprenez réellement](../../../02-prompt-engineering)
- [Étapes suivantes](../../../02-prompt-engineering)

## Ce que vous apprendrez

Dans le module précédent, vous avez vu comment la mémoire permet à l'IA conversationnelle et utilisé les modèles GitHub pour des interactions basiques. Maintenant, nous allons nous concentrer sur la façon dont vous posez des questions - les invites elles-mêmes - en utilisant GPT-5.2 d'Azure OpenAI. La façon dont vous structurez vos invites impacte considérablement la qualité des réponses obtenues.

Nous utiliserons GPT-5.2 car il introduit le contrôle du raisonnement - vous pouvez indiquer au modèle combien de réflexion effectuer avant de répondre. Cela rend les différentes stratégies d'invite plus évidentes et vous aide à comprendre quand utiliser chaque approche. Nous bénéficierons également des limites de taux plus souples d'Azure pour GPT-5.2 comparé aux modèles GitHub.

## Prérequis

- Module 01 terminé (ressources Azure OpenAI déployées)
- Fichier `.env` dans le répertoire racine avec les identifiants Azure (créé par `azd up` dans le module 01)

> **Note :** Si vous n'avez pas terminé le module 01, suivez d'abord les instructions de déploiement là-bas.

## Comprendre l'ingénierie des invites

L'ingénierie des invites consiste à concevoir un texte d'entrée qui vous donne de manière constante les résultats dont vous avez besoin. Il ne s'agit pas seulement de poser des questions - il faut structurer les requêtes pour que le modèle comprenne exactement ce que vous voulez et comment le livrer.

Pensez-y comme donner des instructions à un collègue. « Corrige le bug » est vague. « Corrige l'exception de pointeur nul dans UserService.java ligne 45 en ajoutant une vérification de null » est précis. Les modèles de langage fonctionnent de la même manière - la spécificité et la structure comptent.

## Comment cela utilise LangChain4j

Ce module démontre des modèles avancés d'invite en utilisant la même base LangChain4j des modules précédents, avec un focus sur la structure des invites et le contrôle du raisonnement.

<img src="../../../translated_images/fr/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*Comment LangChain4j connecte vos invites à Azure OpenAI GPT-5.2*

**Dépendances** - Le module 02 utilise les dépendances langchain4j suivantes définies dans `pom.xml` :  
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```
  
**Configuration OpenAiOfficialChatModel** - [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Le modèle de chat est configuré manuellement en tant que bean Spring en utilisant le client officiel OpenAI, qui supporte les endpoints Azure OpenAI. La principale différence avec le module 01 est la façon dont nous structurons les invites envoyées à `chatModel.chat()`, pas la configuration du modèle elle-même.

**Messages Système et Utilisateur** - [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j sépare les types de messages pour plus de clarté. `SystemMessage` définit le comportement et le contexte de l'IA (comme « Vous êtes un réviseur de code »), tandis que `UserMessage` contient la requête proprement dite. Cette séparation vous permet de maintenir un comportement IA cohérent pour différentes requêtes utilisateur.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```
  
<img src="../../../translated_images/fr/message-types.93e0779798a17c9d.webp" alt="Architecture des types de messages" width="800"/>

*SystemMessage fournit un contexte persistant tandis que UserMessages contiennent des requêtes individuelles*

**MessageWindowChatMemory pour les échanges multi-tours** - Pour le modèle de conversation multi-tours, nous réutilisons `MessageWindowChatMemory` du module 01. Chaque session possède sa propre instance mémoire stockée dans un `Map<String, ChatMemory>`, permettant plusieurs conversations simultanées sans mélange de contexte.

**Modèles d'Invite** - Le vrai focus ici est l'ingénierie des invites, pas de nouvelles APIs LangChain4j. Chaque modèle (faible impatience, forte impatience, exécution de tâche, etc.) utilise la même méthode `chatModel.chat(prompt)` mais avec des chaînes d'invite soigneusement structurées. Les balises XML, instructions et mise en forme font partie du texte de l'invite, pas des fonctionnalités LangChain4j.

**Contrôle du Raisonnement** - L'effort de raisonnement de GPT-5.2 est contrôlé via des instructions dans l'invite comme « maximum 2 étapes de raisonnement » ou « explorer en profondeur ». Ce sont des techniques d'ingénierie des invites, pas des configurations LangChain4j. La bibliothèque se contente de transmettre vos invites au modèle.

La conclusion clé : LangChain4j fournit l'infrastructure (connexion modèle via [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), mémoire, gestion des messages via [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), tandis que ce module vous apprend à créer des invites efficaces dans cette infrastructure.

## Les modèles de base

Tous les problèmes ne nécessitent pas la même approche. Certaines questions demandent des réponses rapides, d’autres une réflexion approfondie. Certaines requièrent un raisonnement visible, d’autres seulement des résultats. Ce module couvre huit modèles d’invite, chacun optimisé pour différents scénarios. Vous expérimenterez tous pour apprendre quand chaque approche est la meilleure.

<img src="../../../translated_images/fr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Huit modèles d'ingénierie des invites" width="800"/>

*Vue d’ensemble des huit modèles d’ingénierie des invites et de leurs cas d’usage*

<img src="../../../translated_images/fr/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Comparaison des efforts de raisonnement" width="800"/>

*Faible impatience (rapide, direct) vs Forte impatience (approfondie, exploratoire) dans le raisonnement*

**Faible impatience (rapide et ciblé)** - Pour des questions simples où vous souhaitez des réponses rapides et directes. Le modèle effectue un raisonnement minimal - maximum 2 étapes. Utilisez-le pour des calculs, recherches ou questions simples.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```
  
> 💡 **Explorez avec GitHub Copilot :** Ouvrez [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) et demandez :  
> - « Quelle est la différence entre les modèles d’invite à faible impatience et à forte impatience ? »  
> - « Comment les balises XML dans les invites aident-elles à structurer la réponse de l’IA ? »  
> - « Quand devrais-je utiliser les modèles d’auto-réflexion versus l’instruction directe ? »

**Forte impatience (approfondie et complète)** - Pour des problèmes complexes nécessitant une analyse complète. Le modèle explore en profondeur et montre un raisonnement détaillé. Utilisez-le pour la conception système, les décisions d’architecture ou la recherche complexe.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**Exécution de tâches (progression étape par étape)** - Pour des flux de travail multi-étapes. Le modèle fournit un plan initial, narrateur chaque étape pendant son exécution, puis donne un résumé. Utilisez-le pour des migrations, implémentations ou tout processus en plusieurs étapes.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```
  
Le prompting Chain-of-Thought demande explicitement au modèle de montrer son processus de raisonnement, améliorant la précision pour les tâches complexes. La décomposition étape par étape aide aussi bien les humains que l’IA à comprendre la logique.

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Demandez au sujet de ce modèle :  
> - « Comment adapterais-je le modèle d’exécution de tâche pour des opérations de longue durée ? »  
> - « Quelles sont les bonnes pratiques pour structurer les préambules d’outils dans des applications en production ? »  
> - « Comment capturer et afficher les mises à jour de progression intermédiaires dans une interface utilisateur ? »

<img src="../../../translated_images/fr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Modèle d'exécution de tâche" width="800"/>

*Planifier → Exécuter → Résumer pour des tâches multi-étapes*

**Code auto-réfléchissant** - Pour générer du code prêt pour la production. Le modèle génère du code, le vérifie selon des critères de qualité, et l’améliore de façon itérative. Utilisez-le pour créer de nouvelles fonctionnalités ou services.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/fr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cycle d'auto-réflexion" width="800"/>

*Boucle d’amélioration itérative - générer, évaluer, identifier les problèmes, améliorer, répéter*

**Analyse structurée** - Pour une évaluation cohérente. Le modèle analyse le code selon un cadre fixe (correctitude, pratiques, performances, sécurité). Utilisez-le pour des revues de code ou évaluations de qualité.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```
  
> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Posez des questions sur l’analyse structurée :  
> - « Comment personnaliser le cadre d’analyse pour différents types de revues de code ? »  
> - « Quelle est la meilleure manière de parser et agir sur la sortie structurée de façon programmatique ? »  
> - « Comment assurer une cohérence des niveaux de gravité entre différentes sessions de revue ? »

<img src="../../../translated_images/fr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Modèle d'analyse structurée" width="800"/>

*Cadre à quatre catégories pour des revues de code cohérentes avec niveaux de gravité*

**Chat à tours multiples** - Pour des conversations nécessitant du contexte. Le modèle se souvient des messages précédents et s’appuie dessus. Utilisez-le pour des sessions d’aide interactives ou Q&A complexes.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
<img src="../../../translated_images/fr/context-memory.dff30ad9fa78832a.webp" alt="Mémoire de contexte" width="800"/>

*Comment le contexte conversationnel s’accumule sur plusieurs tours jusqu’à la limite de tokens*

**Raisonnement étape par étape** - Pour des problèmes nécessitant une logique visible. Le modèle expose explicitement le raisonnement pour chaque étape. Utilisez-le pour des problèmes mathématiques, puzzles logiques, ou lorsque vous souhaitez comprendre le processus de réflexion.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/fr/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Modèle étape par étape" width="800"/>

*Décomposition des problèmes en étapes logiques explicites*

**Sortie contrainte** - Pour des réponses avec des exigences spécifiques de format. Le modèle suit strictement les règles de format et de longueur. Utilisez-le pour des résumés ou lorsque vous avez besoin d’une structure précise de sortie.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/fr/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Modèle de sortie contrainte" width="800"/>

*Application de règles spécifiques de format, longueur et structure*

## Utiliser les ressources Azure existantes

**Vérifier le déploiement :**

Assurez-vous que le fichier `.env` existe dans le répertoire racine avec les identifiants Azure (créé lors du module 01) :  
```bash
cat ../.env  # Doit afficher AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Démarrer l’application :**

> **Note :** Si vous avez déjà démarré toutes les applications avec `./start-all.sh` du module 01, ce module est déjà lancé sur le port 8083. Vous pouvez sauter les commandes de démarrage ci-dessous et aller directement sur http://localhost:8083.

**Option 1 : Utilisation du Spring Boot Dashboard (recommandé pour les utilisateurs VS Code)**

Le conteneur de développement inclut l’extension Spring Boot Dashboard, qui offre une interface visuelle pour gérer toutes les applications Spring Boot. Vous la trouverez dans la barre d’activité à gauche de VS Code (cherchez l’icône Spring Boot).

Depuis le Spring Boot Dashboard, vous pouvez :  
- Voir toutes les applications Spring Boot du workspace  
- Démarrer/arrêter les applications en un clic  
- Voir les logs en temps réel  
- Surveiller le statut des applications

Cliquez simplement sur le bouton play à côté de « prompt-engineering » pour démarrer ce module, ou lancez tous les modules simultanément.

<img src="../../../translated_images/fr/dashboard.da2c2130c904aaf0.webp" alt="Tableau de bord Spring Boot" width="400"/>

**Option 2 : Utilisation de scripts shell**

Démarrer toutes les applications web (modules 01-04) :

**Bash :**  
```bash
cd ..  # Depuis le répertoire racine
./start-all.sh
```
  
**PowerShell :**  
```powershell
cd ..  # Depuis le répertoire racine
.\start-all.ps1
```
  
Ou démarrez uniquement ce module :

**Bash :**  
```bash
cd 02-prompt-engineering
./start.sh
```
  
**PowerShell :**  
```powershell
cd 02-prompt-engineering
.\start.ps1
```
  
Les deux scripts chargent automatiquement les variables d’environnement depuis le fichier `.env` racine et construisent les JARs s’ils n’existent pas.

> **Note :** Si vous préférez construire manuellement tous les modules avant de démarrer :  
>  
> **Bash :**  
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  
> **PowerShell :**  
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  
Ouvrez http://localhost:8083 dans votre navigateur.

**Pour arrêter :**

**Bash :**  
```bash
./stop.sh  # Seulement ce module
# Ou
cd .. && ./stop-all.sh  # Tous les modules
```
  
**PowerShell :**  
```powershell
.\stop.ps1  # Ce module seulement
# Ou
cd ..; .\stop-all.ps1  # Tous les modules
```
  
## Captures d'écran de l'application

<img src="../../../translated_images/fr/dashboard-home.5444dbda4bc1f79d.webp" alt="Accueil du tableau de bord" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Le tableau de bord principal affichant les 8 modèles d’ingénierie des invites avec leurs caractéristiques et cas d’usage*

## Explorer les modèles

L’interface web vous permet d’expérimenter différentes stratégies d’invite. Chaque modèle résout des problèmes différents - essayez-les pour voir quand chaque approche brille.

### Faible vs forte impatience

Posez une question simple comme « Quel est 15 % de 200 ? » avec faible impatience. Vous obtiendrez une réponse instantanée et directe. Maintenant, posez quelque chose de complexe comme « Concevez une stratégie de mise en cache pour une API à fort trafic » avec forte impatience. Observez comment le modèle ralentit et fournit un raisonnement détaillé. Même modèle, même structure de question - mais l’invite lui indique combien réfléchir.
<img src="../../../translated_images/fr/low-eagerness-demo.898894591fb23aa0.webp" alt="Démonstration de faible empressement" width="800"/>

*Calcul rapide avec un raisonnement minimal*

<img src="../../../translated_images/fr/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Démonstration de fort empressement" width="800"/>

*Stratégie de mise en cache complète (2.8MB)*

### Exécution de tâche (préambules d'outils)

Les flux de travail multi-étapes bénéficient d'une planification préalable et d'une narration du progrès. Le modèle décrit ce qu'il va faire, narre chaque étape, puis résume les résultats.

<img src="../../../translated_images/fr/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Démonstration d'exécution de tâche" width="800"/>

*Création d'un endpoint REST avec narration étape par étape (3.9MB)*

### Code auto-réfléchi

Essayez "Créer un service de validation d’email". Au lieu de simplement générer le code puis s'arrêter, le modèle génère, évalue selon des critères de qualité, identifie les faiblesses et améliore. Vous le verrez itérer jusqu'à ce que le code atteigne les standards de production.

<img src="../../../translated_images/fr/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Démonstration de code auto-réfléchi" width="800"/>

*Service complet de validation d’email (5.2MB)*

### Analyse structurée

Les revues de code nécessitent des cadres d’évaluation cohérents. Le modèle analyse le code selon des catégories fixes (correction, bonnes pratiques, performance, sécurité) avec des niveaux de gravité.

<img src="../../../translated_images/fr/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Démonstration d'analyse structurée" width="800"/>

*Revue de code basée sur un cadre structuré*

### Chat multi-tour

Demandez "Qu'est-ce que Spring Boot ?" puis suivez immédiatement par "Montre-moi un exemple". Le modèle se souvient de votre première question et vous donne un exemple spécifique de Spring Boot. Sans mémoire, cette deuxième question serait trop vague.

<img src="../../../translated_images/fr/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Démonstration de chat multi-tour" width="800"/>

*Conservation du contexte à travers les questions*

### Raisonnement étape par étape

Choisissez un problème mathématique et essayez-le avec Raisonnement étape par étape et Faible empressement. Le faible empressement vous donne juste la réponse – rapide mais opaque. Étape par étape vous montre chaque calcul et décision.

<img src="../../../translated_images/fr/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Démonstration de raisonnement étape par étape" width="800"/>

*Problème mathématique avec étapes explicites*

### Sortie contrainte

Lorsque vous avez besoin de formats spécifiques ou de nombres de mots, ce modèle impose une stricte conformité. Essayez de générer un résumé avec exactement 100 mots en format liste à puces.

<img src="../../../translated_images/fr/constrained-output-demo.567cc45b75da1633.webp" alt="Démonstration de sortie contrainte" width="800"/>

*Résumé d’apprentissage automatique avec contrôle du format*

## Ce que vous apprenez vraiment

**L'effort de raisonnement change tout**

GPT-5.2 vous permet de contrôler l’effort de calcul via vos prompts. Un faible effort signifie des réponses rapides avec une exploration minimale. Un effort élevé signifie que le modèle prend le temps de penser en profondeur. Vous apprenez à ajuster l’effort à la complexité de la tâche – ne perdez pas de temps sur des questions simples, mais ne précipitez pas les décisions complexes non plus.

**La structure guide le comportement**

Vous avez remarqué les balises XML dans les prompts ? Elles ne sont pas décoratives. Les modèles suivent des instructions structurées plus fiablement que du texte libre. Quand vous avez besoin de processus multi-étapes ou de logique complexe, la structure aide le modèle à savoir où il en est et ce qui vient ensuite.

<img src="../../../translated_images/fr/prompt-structure.a77763d63f4e2f89.webp" alt="Structure du prompt" width="800"/>

*Anatomie d’un prompt bien structuré avec sections claires et organisation de style XML*

**Qualité par auto-évaluation**

Les modèles auto-réfléchissants fonctionnent en rendant explicites les critères de qualité. Au lieu d’espérer que le modèle "fasse bien", vous lui dites exactement ce que "bien" signifie : logique correcte, gestion des erreurs, performance, sécurité. Le modèle peut alors évaluer sa propre sortie et s’améliorer. Cela transforme la génération de code d’une loterie en un processus.

**Le contexte est fini**

Les conversations multi-tour fonctionnent en incluant l'historique des messages à chaque requête. Mais il y a une limite – chaque modèle a un nombre maximal de tokens. À mesure que les conversations grandissent, vous aurez besoin de stratégies pour garder le contexte pertinent sans dépasser ce plafond. Ce module vous montre comment fonctionne la mémoire ; plus tard vous apprendrez quand résumer, quand oublier, et quand récupérer.

## Étapes suivantes

**Module suivant :** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation :** [← Précédent : Module 01 - Introduction](../01-introduction/README.md) | [Retour à l'accueil](../README.md) | [Suivant : Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :  
Ce document a été traduit à l’aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforçions d’assurer l’exactitude, veuillez noter que les traductions automatisées peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue d’origine doit être considéré comme la source faisant foi. Pour les informations critiques, nous recommandons une traduction professionnelle effectuée par un humain. Nous déclinons toute responsabilité en cas de malentendus ou de mauvaises interprétations résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->