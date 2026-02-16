# Module 02 : Ingénierie des prompts avec GPT-5.2

## Table des matières

- [Ce que vous apprendrez](../../../02-prompt-engineering)
- [Prérequis](../../../02-prompt-engineering)
- [Comprendre l'ingénierie des prompts](../../../02-prompt-engineering)
- [Fondamentaux de l'ingénierie des prompts](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chaîne de pensée](../../../02-prompt-engineering)
  - [Prompting basé sur le rôle](../../../02-prompt-engineering)
  - [Modèles de prompt](../../../02-prompt-engineering)
- [Modèles avancés](../../../02-prompt-engineering)
- [Utilisation des ressources Azure existantes](../../../02-prompt-engineering)
- [Captures d'écran de l'application](../../../02-prompt-engineering)
- [Exploration des modèles](../../../02-prompt-engineering)
  - [Faible vs forte ardeur](../../../02-prompt-engineering)
  - [Exécution de tâches (preambles d'outil)](../../../02-prompt-engineering)
  - [Code auto-réfléchissant](../../../02-prompt-engineering)
  - [Analyse structurée](../../../02-prompt-engineering)
  - [Chat multi-tours](../../../02-prompt-engineering)
  - [Raisonnement étape par étape](../../../02-prompt-engineering)
  - [Sortie contrainte](../../../02-prompt-engineering)
- [Ce que vous apprenez vraiment](../../../02-prompt-engineering)
- [Prochaines étapes](../../../02-prompt-engineering)

## Ce que vous apprendrez

<img src="../../../translated_images/fr/what-youll-learn.c68269ac048503b2.webp" alt="Ce que vous apprendrez" width="800"/>

Dans le module précédent, vous avez vu comment la mémoire permet l'IA conversationnelle et utilisé les modèles GitHub pour des interactions de base. Maintenant, nous allons nous concentrer sur la manière dont vous posez des questions — les prompts eux-mêmes — en utilisant GPT-5.2 d’Azure OpenAI. La façon dont vous structurez vos prompts influence considérablement la qualité des réponses que vous obtenez. Nous commençons par une revue des techniques fondamentales de prompting, puis passons à huit modèles avancés qui exploitent pleinement les capacités de GPT-5.2.

Nous utilisons GPT-5.2 car il introduit un contrôle du raisonnement - vous pouvez indiquer au modèle combien de réflexion doit être faite avant de répondre. Cela rend les différentes stratégies de prompting plus évidentes et vous aide à comprendre quand utiliser chaque approche. Nous bénéficierons aussi des limites de débit plus faibles d’Azure pour GPT-5.2 comparé aux modèles GitHub.

## Prérequis

- Module 01 complété (ressources Azure OpenAI déployées)
- Fichier `.env` dans le répertoire racine avec les identifiants Azure (créé par `azd up` dans le Module 01)

> **Note :** Si vous n’avez pas complété le Module 01, suivez d’abord les instructions de déploiement là-bas.

## Comprendre l'ingénierie des prompts

<img src="../../../translated_images/fr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Qu'est-ce que l'ingénierie des prompts ?" width="800"/>

L’ingénierie des prompts consiste à concevoir un texte d'entrée qui vous donne constamment les résultats dont vous avez besoin. Il ne s'agit pas seulement de poser des questions - mais de structurer les demandes pour que le modèle comprenne exactement ce que vous voulez et comment le délivrer.

Pensez-y comme donner des instructions à un collègue. "Corrige le bug" est vague. "Corrige l'exception de pointeur nul dans UserService.java ligne 45 en ajoutant une vérification de null" est spécifique. Les modèles de langage fonctionnent de la même manière - la spécificité et la structure comptent.

<img src="../../../translated_images/fr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Comment LangChain4j s'intègre" width="800"/>

LangChain4j fournit l’infrastructure — connexions aux modèles, mémoire, et types de messages — tandis que les modèles de prompt sont simplement des textes soigneusement structurés que vous envoyez via cette infrastructure. Les éléments clés sont `SystemMessage` (qui définit le comportement et le rôle de l’IA) et `UserMessage` (qui porte votre demande réelle).

## Fondamentaux de l'ingénierie des prompts

<img src="../../../translated_images/fr/five-patterns-overview.160f35045ffd2a94.webp" alt="Vue d'ensemble des cinq modèles d'ingénierie des prompts" width="800"/>

Avant de plonger dans les modèles avancés de ce module, passons en revue cinq techniques fondamentales de prompting. Ce sont les briques de base que tout ingénieur de prompt devrait connaître. Si vous avez déjà travaillé le [module de démarrage rapide](../00-quick-start/README.md#2-prompt-patterns), vous les avez vues en action — voici le cadre conceptuel qui les sous-tend.

### Zero-Shot Prompting

L’approche la plus simple : donner au modèle une instruction directe sans exemples. Le modèle s’appuie entièrement sur sa formation pour comprendre et exécuter la tâche. Cela fonctionne bien pour des demandes simples où le comportement attendu est évident.

<img src="../../../translated_images/fr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Instruction directe sans exemple — le modèle déduit la tâche uniquement à partir de l’instruction*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Réponse : "Positif"
```

**Quand utiliser :** Classifications simples, questions directes, traductions, ou toute tâche que le modèle peut gérer sans guidage supplémentaire.

### Few-Shot Prompting

Fournir des exemples qui démontrent le modèle que vous souhaitez que le modèle suive. Le modèle apprend le format entrée-sortie attendu à partir de vos exemples et l’applique aux nouvelles entrées. Cela améliore considérablement la cohérence pour les tâches où le format ou le comportement souhaité n’est pas évident.

<img src="../../../translated_images/fr/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Apprentissage à partir d’exemples — le modèle identifie le modèle et l’applique aux nouvelles entrées*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**Quand utiliser :** Classifications personnalisées, formatage cohérent, tâches spécifiques au domaine, ou lorsque les résultats zero-shot sont inconsistants.

### Chaîne de pensée

Demander au modèle de montrer son raisonnement étape par étape. Au lieu de donner directement une réponse, le modèle décompose le problème et travaille sur chaque partie explicitement. Cela améliore la précision pour des tâches de mathématiques, logique et raisonnement multi-étapes.

<img src="../../../translated_images/fr/chain-of-thought.5cff6630e2657e2a.webp" alt="Chaîne de pensée Prompting" width="800"/>

*Raisonnement étape par étape — décomposer les problèmes complexes en étapes logiques explicites*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Le modèle montre : 15 - 8 = 7, puis 7 + 12 = 19 pommes
```

**Quand utiliser :** Problèmes mathématiques, casse-têtes logiques, débogage, ou toute tâche où montrer le processus de raisonnement améliore la précision et la confiance.

### Prompting basé sur le rôle

Attribuer un rôle ou une persona à l’IA avant de poser votre question. Cela fournit un contexte qui façonne le ton, la profondeur et l’orientation de la réponse. Un "architecte logiciel" donne des conseils différents d’un "développeur junior" ou d’un "auditeur de sécurité".

<img src="../../../translated_images/fr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Prompting basé sur le rôle" width="800"/>

*Définir le contexte et la persona — la même question reçoit une réponse différente selon le rôle assigné*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**Quand utiliser :** Revue de code, tutorat, analyses spécifiques au domaine, ou lorsque vous avez besoin de réponses adaptées à un niveau d’expertise ou une perspective particulière.

### Modèles de prompt

Créer des prompts réutilisables avec des espaces réservés variables. Au lieu d’écrire un nouveau prompt à chaque fois, définir un modèle une fois et remplir avec différentes valeurs. La classe `PromptTemplate` de LangChain4j facilite cela avec la syntaxe `{{variable}}`.

<img src="../../../translated_images/fr/prompt-templates.14bfc37d45f1a933.webp" alt="Modèles de prompt" width="800"/>

*Prompts réutilisables avec espaces variables — un modèle, plusieurs usages*

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

**Quand utiliser :** Requêtes répétées avec différentes entrées, traitement par lots, création de workflows IA réutilisables, ou tout scénario où la structure du prompt reste la même mais les données changent.

---

Ces cinq fondamentaux vous fournissent une boîte à outils solide pour la plupart des tâches de prompting. Le reste de ce module s’appuie dessus avec **huit modèles avancés** qui tirent parti du contrôle du raisonnement, de l’auto-évaluation et des capacités de sortie structurée de GPT-5.2.

## Modèles avancés

Avec les fondamentaux vus, passons aux huit modèles avancés qui rendent ce module unique. Tous les problèmes ne nécessitent pas la même approche. Certaines questions demandent des réponses rapides, d’autres une réflexion approfondie. Certaines exigent un raisonnement visible, d’autres veulent juste des résultats. Chaque modèle ci-dessous est optimisé pour un scénario différent — et le contrôle du raisonnement de GPT-5.2 accentue encore plus ces différences.

<img src="../../../translated_images/fr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Huit modèles de prompting" width="800"/>

*Vue d’ensemble des huit modèles d’ingénierie des prompts et de leurs cas d’usage*

<img src="../../../translated_images/fr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Contrôle du raisonnement avec GPT-5.2" width="800"/>

*Le contrôle du raisonnement de GPT-5.2 vous permet de spécifier combien de réflexion le modèle doit faire — des réponses rapides et directes à une exploration approfondie*

<img src="../../../translated_images/fr/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Comparaison des efforts de raisonnement" width="800"/>

*Faible ardeur (rapide, direct) vs haute ardeur (approfondi, exploratoire)*

**Faible ardeur (Rapide & Focusé)** - Pour les questions simples où vous voulez des réponses rapides et directes. Le modèle fait un raisonnement minimal - maximum 2 étapes. Utilisez-le pour les calculs, recherches, ou questions simples.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Explorez avec GitHub Copilot :** Ouvrez [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) et demandez :
> - "Quelle est la différence entre les modèles de prompting à faible et haute ardeur ?"
> - "Comment les balises XML dans les prompts aident-elles à structurer la réponse de l’IA ?"
> - "Quand dois-je utiliser les modèles d’auto-réflexion vs instructions directes ?"

**Haute ardeur (Approfondi & Complet)** - Pour les problèmes complexes nécessitant une analyse exhaustive. Le modèle explore en profondeur et montre un raisonnement détaillé. Utilisez-le pour la conception système, décisions architecturales ou recherches complexes.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Exécution de tâches (Progrès étape par étape)** - Pour workflows multi-étapes. Le modèle fournit un plan initial, décrit chaque étape en travaillant, puis fournit un résumé. Utilisez-le pour migrations, implémentations ou tout processus multi-étapes.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Le prompting en chaîne de pensée (Chain-of-Thought) demande explicitement au modèle de montrer son processus de raisonnement, améliorant la précision pour des tâches complexes. La décomposition pas à pas aide humains et IA à comprendre la logique.

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Posez des questions sur ce modèle :
> - "Comment adapterais-je le modèle d’exécution de tâches pour des opérations de longue durée ?"
> - "Quelles sont les meilleures pratiques pour structurer les préambules d’outil dans des applications en production ?"
> - "Comment capturer et afficher les mises à jour intermédiaires de progression dans une UI ?"

<img src="../../../translated_images/fr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Modèle d'exécution de tâches" width="800"/>

*Planifier → Exécuter → Résumer un workflow pour les tâches multi-étapes*

**Code auto-réfléchissant** - Pour générer du code de qualité production. Le modèle génère du code, le vérifie selon des critères de qualité, puis l’améliore de manière itérative. Utilisez-le lors de la création de nouvelles fonctionnalités ou services.

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

<img src="../../../translated_images/fr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cycle d’auto-réflexion" width="800"/>

*Boucle d’amélioration itérative - générer, évaluer, identifier les problèmes, améliorer, répéter*

**Analyse structurée** - Pour une évaluation cohérente. Le modèle passe en revue le code selon un cadre fixe (exactitude, pratiques, performance, sécurité). Utilisez-le pour les revues de code ou évaluations qualité.

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
> - "Comment personnaliser le cadre d’analyse pour différents types de revues de code ?"
> - "Quelle est la meilleure façon de parser et agir sur une sortie structurée de façon programmatique ?"
> - "Comment assurer une cohérence des niveaux de gravité entre différentes sessions d’examen ?"

<img src="../../../translated_images/fr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Modèle d’analyse structurée" width="800"/>

*Cadre à quatre catégories pour des revues de code cohérentes avec niveaux de gravité*

**Chat multi-tours** - Pour des conversations nécessitant du contexte. Le modèle se souvient des messages précédents et s’appuie dessus. Utilisez-le pour des sessions d’aide interactives ou des Q&R complexes.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/fr/context-memory.dff30ad9fa78832a.webp" alt="Mémoire du contexte" width="800"/>

*Comment le contexte de conversation s’accumule au fil des tours jusqu’à atteindre la limite de tokens*

**Raisonnement étape par étape** - Pour des problèmes nécessitant une logique visible. Le modèle montre un raisonnement explicite pour chaque étape. Utilisez-le pour des problèmes mathématiques, casse-têtes logiques ou quand vous devez comprendre le processus de réflexion.

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

*Décomposer les problèmes en étapes logiques explicites*

**Sortie contrainte** - Pour des réponses avec des exigences spécifiques de format. Le modèle suit strictement les règles de format et de longueur. Utilisez-le pour des résumés ou quand vous avez besoin d’une structure de sortie précise.

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

*Imposer des exigences spécifiques de format, longueur et structure*

## Utilisation des ressources Azure existantes

**Vérifier le déploiement :**

Assurez-vous que le fichier `.env` existe dans le répertoire racine avec les identifiants Azure (créé pendant le Module 01) :
```bash
cat ../.env  # Devrait afficher AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Démarrer l'application :**

> **Note :** Si vous avez déjà démarré toutes les applications avec `./start-all.sh` depuis le Module 01, ce module tourne déjà sur le port 8083. Vous pouvez ignorer les commandes de démarrage ci-dessous et aller directement à http://localhost:8083.

**Option 1 : Utilisation du Spring Boot Dashboard (recommandé pour les utilisateurs VS Code)**

Le conteneur de développement inclut l’extension Spring Boot Dashboard, qui fournit une interface visuelle pour gérer toutes les applications Spring Boot. Vous pouvez la trouver dans la barre d’activité sur le côté gauche de VS Code (cherchez l’icône Spring Boot).
Depuis le tableau de bord Spring Boot, vous pouvez :
- Voir toutes les applications Spring Boot disponibles dans l’espace de travail
- Démarrer/arrêter les applications en un seul clic
- Visualiser les journaux d’application en temps réel
- Surveiller le statut de l’application

Cliquez simplement sur le bouton de lecture à côté de "prompt-engineering" pour démarrer ce module, ou lancez tous les modules en une seule fois.

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

Ou démarrez seulement ce module :

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

Les deux scripts chargent automatiquement les variables d’environnement depuis le fichier `.env` à la racine et construiront les JARs s’ils n’existent pas.

> **Note :** Si vous préférez construire tous les modules manuellement avant de démarrer :
>
> **Bash :**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell :**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Ouvrez http://localhost:8083 dans votre navigateur.

**Pour arrêter :**

**Bash :**
```bash
./stop.sh  # Ce module uniquement
# Ou
cd .. && ./stop-all.sh  # Tous les modules
```

**PowerShell :**
```powershell
.\stop.ps1  # Ce module uniquement
# Ou
cd ..; .\stop-all.ps1  # Tous les modules
```

## Captures d’écran de l’application

<img src="../../../translated_images/fr/dashboard-home.5444dbda4bc1f79d.webp" alt="Accueil du tableau de bord" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Le tableau de bord principal affichant les 8 patrons de prompt engineering avec leurs caractéristiques et cas d’utilisation*

## Explorer les Patrons

L’interface web vous permet d’expérimenter différentes stratégies de prompting. Chaque patron résout des problèmes différents – essayez-les pour voir quand chaque approche est efficace.

### Faible vs Forte Implication

Posez une question simple comme « Quel est 15 % de 200 ? » en utilisant une faible implication. Vous obtiendrez une réponse instantanée et directe. Maintenant, posez quelque chose de complexe comme « Concevez une stratégie de mise en cache pour une API à fort trafic » avec une forte implication. Observez comment le modèle ralentit et fournit un raisonnement détaillé. Même modèle, même structure de question – mais le prompt lui dit combien de réflexion faire.

<img src="../../../translated_images/fr/low-eagerness-demo.898894591fb23aa0.webp" alt="Démonstration Faible Implication" width="800"/>

*Calcul rapide avec un raisonnement minimal*

<img src="../../../translated_images/fr/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Démonstration Forte Implication" width="800"/>

*Stratégie de mise en cache complète (2,8 Mo)*

### Exécution de Tâches (Préambules d’Outils)

Les workflows multi-étapes bénéficient d’une planification préalable et d’une narration des progrès. Le modèle décrit ce qu’il va faire, narre chaque étape, puis résume les résultats.

<img src="../../../translated_images/fr/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Démonstration Exécution de Tâches" width="800"/>

*Création d’un endpoint REST avec narration étape par étape (3,9 Mo)*

### Code Auto-Réfléchi

Essayez « Créez un service de validation d’email ». Au lieu de générer simplement le code et de s’arrêter, le modèle génère, évalue selon des critères de qualité, identifie les faiblesses et améliore. Vous le verrez itérer jusqu’à ce que le code réponde aux standards de production.

<img src="../../../translated_images/fr/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Démonstration Code Auto-Réfléchi" width="800"/>

*Service complet de validation d’email (5,2 Mo)*

### Analyse Structurée

Les revues de code nécessitent des cadres d’évaluation cohérents. Le modèle analyse le code en utilisant des catégories fixes (correction, pratiques, performance, sécurité) avec des niveaux de gravité.

<img src="../../../translated_images/fr/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Démonstration Analyse Structurée" width="800"/>

*Revue de code basée sur un cadre*

### Chat Multi-Tours

Demandez « Qu’est-ce que Spring Boot ? » puis enchaînez immédiatement avec « Montre-moi un exemple ». Le modèle se souvient de votre première question et vous donne un exemple spécifique de Spring Boot. Sans mémoire, la deuxième question serait trop vague.

<img src="../../../translated_images/fr/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Démonstration Chat Multi-Tours" width="800"/>

*Préservation du contexte entre questions*

### Raisonnement Étape par Étape

Choisissez un problème de mathématiques et essayez-le avec le raisonnement étape par étape et avec une faible implication. La faible implication vous donne juste la réponse - rapide mais opaque. Le raisonnement détaillé vous montre chaque calcul et décision.

<img src="../../../translated_images/fr/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Démonstration Raisonnement Étape par Étape" width="800"/>

*Problème de mathématiques avec étapes explicites*

### Sortie Contraintes

Quand vous avez besoin de formats spécifiques ou de compte de mots, ce patron impose une stricte conformité. Essayez de générer un résumé avec exactement 100 mots sous forme de points de liste.

<img src="../../../translated_images/fr/constrained-output-demo.567cc45b75da1633.webp" alt="Démonstration Sortie Contraintes" width="800"/>

*Résumé d’apprentissage machine avec contrôle du format*

## Ce que vous apprenez vraiment

**L’effort de raisonnement change tout**

GPT-5.2 vous permet de contrôler l’effort computationnel via vos prompts. Un faible effort signifie des réponses rapides avec une exploration minimale. Un effort élevé signifie que le modèle prend le temps de réfléchir en profondeur. Vous apprenez à adapter l’effort à la complexité de la tâche – ne perdez pas de temps sur des questions simples, mais ne précipitez pas non plus les décisions complexes.

**La structure guide le comportement**

Vous remarquez les balises XML dans les prompts ? Elles ne sont pas décoratives. Les modèles suivent des instructions structurées plus fiablement que du texte libre. Quand vous avez besoin de processus multi-étapes ou de logique complexe, la structure aide le modèle à suivre où il en est et ce qui vient ensuite.

<img src="../../../translated_images/fr/prompt-structure.a77763d63f4e2f89.webp" alt="Structure du Prompt" width="800"/>

*Anatomie d’un prompt bien structuré avec sections claires et organisation style XML*

**Qualité par auto-évaluation**

Les patrons auto-réfléchissants fonctionnent en rendant explicites les critères de qualité. Plutôt que d’espérer que le modèle « fasse bien », vous lui dites exactement ce que signifie « bien » : logique correcte, gestion des erreurs, performance, sécurité. Le modèle peut alors évaluer sa propre sortie et s’améliorer. Cela transforme la génération de code d’une loterie en un processus.

**Le contexte est fini**

Les conversations multi-tours fonctionnent en incluant l’historique des messages avec chaque requête. Mais il y a une limite – chaque modèle a un nombre maximal de tokens. À mesure que les conversations grandissent, vous aurez besoin de stratégies pour conserver le contexte pertinent sans atteindre ce plafond. Ce module vous montre comment fonctionne la mémoire ; plus tard, vous apprendrez quand résumer, quand oublier et quand récupérer.

## Étapes suivantes

**Module suivant :** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation :** [← Précédent : Module 01 - Introduction](../01-introduction/README.md) | [Retour au début](../README.md) | [Suivant : Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :  
Ce document a été traduit à l’aide du service de traduction automatisée [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforçons d’assurer la précision, veuillez noter que les traductions automatisées peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue d’origine doit être considéré comme la source faisant foi. Pour toute information critique, une traduction professionnelle réalisée par un humain est recommandée. Nous déclinons toute responsabilité en cas de malentendus ou de mauvaises interprétations résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->