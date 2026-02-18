# Module 02 : Ingénierie des Invites avec GPT-5.2

## Table des matières

- [Ce que vous apprendrez](../../../02-prompt-engineering)
- [Prérequis](../../../02-prompt-engineering)
- [Comprendre l'ingénierie des invites](../../../02-prompt-engineering)
- [Fondamentaux de l'ingénierie des invites](../../../02-prompt-engineering)
  - [Invites Zero-Shot](../../../02-prompt-engineering)
  - [Invites Few-Shot](../../../02-prompt-engineering)
  - [Chaîne de réflexion](../../../02-prompt-engineering)
  - [Invites basées sur un rôle](../../../02-prompt-engineering)
  - [Modèles d'invites](../../../02-prompt-engineering)
- [Modèles avancés](../../../02-prompt-engineering)
- [Utiliser les ressources Azure existantes](../../../02-prompt-engineering)
- [Captures d'écran de l'application](../../../02-prompt-engineering)
- [Explorer les modèles](../../../02-prompt-engineering)
  - [Faible vs forte impatience](../../../02-prompt-engineering)
  - [Exécution des tâches (Préambules des outils)](../../../02-prompt-engineering)
  - [Code auto-réfléchissant](../../../02-prompt-engineering)
  - [Analyse structurée](../../../02-prompt-engineering)
  - [Chat multi-tours](../../../02-prompt-engineering)
  - [Raisonnement étape par étape](../../../02-prompt-engineering)
  - [Sortie contrainte](../../../02-prompt-engineering)
- [Ce que vous apprenez vraiment](../../../02-prompt-engineering)
- [Étapes suivantes](../../../02-prompt-engineering)

## Ce que vous apprendrez

<img src="../../../translated_images/fr/what-youll-learn.c68269ac048503b2.webp" alt="Ce que vous apprendrez" width="800"/>

Dans le module précédent, vous avez vu comment la mémoire permet l'IA conversationnelle et utilisé les modèles GitHub pour des interactions basiques. Nous allons maintenant nous concentrer sur la manière de poser des questions — les invites elles-mêmes — en utilisant GPT-5.2 d’Azure OpenAI. La façon dont vous structurez vos invites influe de manière drastique sur la qualité des réponses obtenues. Nous commençons par un rappel des techniques fondamentales d’invites, puis passons à huit modèles avancés qui exploitent pleinement les capacités de GPT-5.2.

Nous utilisons GPT-5.2 car il introduit un contrôle du raisonnement - vous pouvez indiquer au modèle combien de réflexion faire avant de répondre. Cela rend les différentes stratégies d’invites plus évidentes et vous aide à comprendre quand utiliser chaque approche. Nous bénéficierons également des limites d’usage moins strictes d’Azure pour GPT-5.2 comparé aux modèles GitHub.

## Prérequis

- Module 01 terminé (ressources Azure OpenAI déployées)
- Fichier `.env` dans le répertoire racine avec les identifiants Azure (créé par `azd up` dans le Module 01)

> **Note :** Si vous n'avez pas terminé le Module 01, suivez d'abord les instructions de déploiement présentes dans celui-ci.

## Comprendre l'ingénierie des invites

<img src="../../../translated_images/fr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Qu'est-ce que l'ingénierie des invites ?" width="800"/>

L’ingénierie des invites consiste à concevoir un texte d’entrée qui vous garantit de manière constante les résultats dont vous avez besoin. Ce n’est pas seulement poser des questions — c’est structurer vos requêtes de sorte que le modèle comprenne exactement ce que vous voulez et comment le délivrer.

Pensez-y comme si vous donniez des instructions à un collègue. « Corrige le bug » est vague. « Corrige l’exception de pointeur nul dans UserService.java ligne 45 en ajoutant une vérification de null » est précis. Les modèles de langage fonctionnent de la même manière — la spécificité et la structure comptent.

<img src="../../../translated_images/fr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Comment LangChain4j s'intègre" width="800"/>

LangChain4j fournit l’infrastructure — connexions aux modèles, mémoire, types de messages — tandis que les modèles d’invites sont simplement des textes soigneusement structurés que vous envoyez via cette infrastructure. Les éléments clés sont `SystemMessage` (qui définit le comportement et le rôle de l’IA) et `UserMessage` (qui contient votre demande réelle).

## Fondamentaux de l'ingénierie des invites

<img src="../../../translated_images/fr/five-patterns-overview.160f35045ffd2a94.webp" alt="Présentation des cinq modèles d'ingénierie des invites" width="800"/>

Avant d’entrer dans les modèles avancés de ce module, passons en revue cinq techniques fondamentales d’invites. Ce sont les briques essentielles que chaque ingénieur d’invites doit connaître. Si vous avez déjà travaillé sur le [module de démarrage rapide](../00-quick-start/README.md#2-prompt-patterns), vous les avez vues en action — voici le cadre conceptuel qui les sous-tend.

### Invites Zero-Shot

L’approche la plus simple : donner au modèle une instruction directe sans exemples. Le modèle se base entièrement sur son entraînement pour comprendre et exécuter la tâche. Cela fonctionne bien pour les requêtes simples dont le comportement attendu est évident.

<img src="../../../translated_images/fr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Invites Zero-Shot" width="800"/>

*Instruction directe sans exemples — le modèle déduit la tâche uniquement à partir de l'instruction*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Réponse : « Positif »
```

**Quand utiliser :** Classifications simples, questions directes, traductions, ou toute tâche que le modèle peut gérer sans directives supplémentaires.

### Invites Few-Shot

Fournir des exemples qui montrent le modèle le schéma à suivre. Le modèle apprend le format d'entrée-sortie attendu à partir de vos exemples et l’applique à de nouvelles entrées. Cela améliore considérablement la cohérence pour les tâches où le format ou comportement désiré n’est pas évident.

<img src="../../../translated_images/fr/few-shot-prompting.9d9eace1da88989a.webp" alt="Invites Few-Shot" width="800"/>

*Apprentissage à partir d’exemples — le modèle identifie le schéma et l’applique aux nouvelles entrées*

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

**Quand utiliser :** Classifications personnalisées, formatage cohérent, tâches spécifiques au domaine, ou lorsque les résultats zero-shot sont incohérents.

### Chaîne de réflexion

Demandez au modèle de montrer son raisonnement étape par étape. Au lieu de fournir directement une réponse, le modèle décompose le problème et travaille chaque partie explicitement. Cela améliore la précision sur les mathématiques, la logique et les raisonnements multi-étapes.

<img src="../../../translated_images/fr/chain-of-thought.5cff6630e2657e2a.webp" alt="Chaîne de réflexion" width="800"/>

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

**Quand utiliser :** Problèmes mathématiques, puzzles logiques, débogage, ou toute tâche où afficher le processus de raisonnement améliore précision et confiance.

### Invites basées sur un rôle

Attribuez un rôle ou une personnalité à l’IA avant de poser votre question. Cela fournit un contexte qui oriente le ton, la profondeur et le focus de la réponse. Un « architecte logiciel » donne des conseils différents d’un « développeur junior » ou d’un « auditeur sécurité ».

<img src="../../../translated_images/fr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Invites basées sur un rôle" width="800"/>

*Définir un contexte et une personnalité — la même question obtient une réponse différente selon le rôle assigné*

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

**Quand utiliser :** Revues de code, tutorat, analyses spécifiques à un domaine, ou lorsque vous avez besoin de réponses adaptées à un niveau d’expertise ou perspective particulière.

### Modèles d'invites

Créez des invites réutilisables avec des espaces réservés variables. Au lieu d’écrire une nouvelle invite à chaque fois, vous définissez un modèle une fois et le remplissez avec différentes valeurs. La classe `PromptTemplate` de LangChain4j facilite cela avec la syntaxe `{{variable}}`.

<img src="../../../translated_images/fr/prompt-templates.14bfc37d45f1a933.webp" alt="Modèles d'invites" width="800"/>

*Invites réutilisables avec espaces réservés variables — un modèle, plusieurs usages*

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

**Quand utiliser :** Requêtes répétées avec différentes données, traitement par lots, construction de workflows IA réutilisables, ou tout scénario où la structure de l'invite reste la même mais les données changent.

---

Ces cinq fondamentaux vous fournissent une boîte à outils solide pour la plupart des tâches d’invites. Le reste de ce module s’appuie sur eux avec **huit modèles avancés** qui exploitent le contrôle du raisonnement, l’auto-évaluation et les capacités de sortie structurée de GPT-5.2.

## Modèles avancés

Une fois les fondamentaux couverts, passons aux huit modèles avancés qui rendent ce module unique. Tous les problèmes ne nécessitent pas la même approche. Certaines questions demandent des réponses rapides, d’autres une réflexion approfondie. Certaines nécessitent un raisonnement visible, d’autres juste des résultats. Chaque modèle ci-dessous est optimisé pour un scénario différent — et le contrôle du raisonnement de GPT-5.2 accentue encore ces différences.

<img src="../../../translated_images/fr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Huit modèles d'invites" width="800"/>

*Vue d'ensemble des huit modèles d'ingénierie des invites et leurs cas d’usage*

<img src="../../../translated_images/fr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Contrôle du raisonnement avec GPT-5.2" width="800"/>

*Le contrôle du raisonnement de GPT-5.2 vous permet de spécifier combien de réflexion le modèle doit faire — de réponses rapides directes à une exploration approfondie*

**Faible impatience (Rapide & ciblé)** - Pour les questions simples où vous voulez des réponses rapides et directes. Le modèle fait un minimum de raisonnement - maximum 2 étapes. Utilisez cette méthode pour calculs, consultations ou questions simples.

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Explorez avec GitHub Copilot :** Ouvrez [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) et demandez :
> - « Quelle est la différence entre les modèles d’invite faible impatience et forte impatience ? »
> - « Comment les balises XML dans les invites aident-elles à structurer la réponse de l’IA ? »
> - « Quand dois-je utiliser les modèles d’auto-réflexion vs les instructions directes ? »

**Forte impatience (Approfondi & complet)** - Pour les problèmes complexes où vous voulez une analyse exhaustive. Le modèle explore en détail et montre son raisonnement. Utilisez cela pour la conception système, les décisions d’architecture ou la recherche complexe.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Exécution de tâche (Progrès étape par étape)** - Pour les workflows multi-étapes. Le modèle fournit un plan initial, narre chaque étape pendant qu’il travaille, puis donne un résumé. Utilisez cela pour les migrations, les implémentations, ou tout processus multi-étapes.

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

L’invite de chaîne de réflexion demande explicitement au modèle de montrer son processus de raisonnement, améliorant la précision pour les tâches complexes. La décomposition étape par étape aide autant les humains que l’IA à comprendre la logique.

> **🤖 Essayez avec le chat [GitHub Copilot](https://github.com/features/copilot) :** Posez des questions sur ce modèle :
> - « Comment adapterais-je le modèle d'exécution de tâche pour des opérations longues ? »
> - « Quelles sont les bonnes pratiques pour structurer les préambules d’outils dans des applications en production ? »
> - « Comment capturer et afficher les mises à jour intermédiaires de progression dans une interface utilisateur ? »

<img src="../../../translated_images/fr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Modèle d'exécution de tâche" width="800"/>

*Flux Planifier → Exécuter → Résumer pour les tâches multi-étapes*

**Code auto-réfléchissant** - Pour générer du code de qualité production. Le modèle génère du code suivant les standards de production avec gestion correcte des erreurs. Utilisez cela pour construire de nouvelles fonctionnalités ou services.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cycle d'auto-réflexion" width="800"/>

*Boucle d’amélioration itérative - générer, évaluer, identifier les problèmes, améliorer, répéter*

**Analyse structurée** - Pour une évaluation cohérente. Le modèle examine le code via un cadre fixe (exactitude, bonnes pratiques, performance, sécurité, maintenabilité). Utilisez pour revues de code ou évaluations qualité.

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Essayez avec le chat [GitHub Copilot](https://github.com/features/copilot) :** Demandez sur l’analyse structurée :
> - « Comment personnaliser le cadre d’analyse pour différents types de revues de code ? »
> - « Quelle est la meilleure façon d’analyser et d’agir sur une sortie structurée de manière programmatique ? »
> - « Comment assurer une cohérence des niveaux de gravité entre différentes sessions de revue ? »

<img src="../../../translated_images/fr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Modèle d’analyse structurée" width="800"/>

*Cadre pour des revues de code cohérentes avec niveaux de gravité*

**Chat multi-tours** - Pour les conversations nécessitant du contexte. Le modèle retient les messages précédents et s'appuie dessus. Utilisez pour sessions d’aide interactives ou Q&A complexes.

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

*Comment le contexte de conversation s’accumule sur plusieurs tours jusqu’à atteindre la limite de tokens*

**Raisonnement étape par étape** - Pour les problèmes nécessitant une logique visible. Le modèle montre le raisonnement explicite à chaque étape. Utilisez pour problèmes mathématiques, puzzles logiques, ou pour comprendre le processus de réflexion.

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

**Sortie contrainte** - Pour des réponses avec des exigences spécifiques de format. Le modèle suit strictement des règles de format et de longueur. Utilisez pour résumés ou lorsque vous avez besoin d’une structure de sortie précise.

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

*Application de règles spécifiques de format, longueur, et structure*

## Utiliser les ressources Azure existantes

**Vérifiez le déploiement :**

Assurez-vous que le fichier `.env` existe dans le répertoire racine avec les identifiants Azure (créé pendant le Module 01) :
```bash
cat ../.env  # Devrait afficher AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Démarrez l’application :**

> **Note :** Si vous avez déjà démarré toutes les applications avec `./start-all.sh` du Module 01, ce module tourne déjà sur le port 8083. Vous pouvez donc ignorer les commandes de démarrage ci-dessous et aller directement sur http://localhost:8083.

**Option 1 : Utiliser Spring Boot Dashboard (recommandé pour les utilisateurs de VS Code)**

Le conteneur de développement inclut l’extension Spring Boot Dashboard, qui fournit une interface visuelle pour gérer toutes les applications Spring Boot. Vous la trouverez dans la barre d’activités à gauche de VS Code (cherchez l’icône Spring Boot).

Depuis le Spring Boot Dashboard, vous pouvez :
- Voir toutes les applications Spring Boot disponibles dans l’espace de travail
- Démarrer/arrêter les applications d’un simple clic
- Visualiser les logs en temps réel
- Surveiller le statut des applications
Cliquez simplement sur le bouton de lecture à côté de "prompt-engineering" pour démarrer ce module, ou lancez tous les modules en même temps.

<img src="../../../translated_images/fr/dashboard.da2c2130c904aaf0.webp" alt="Tableau de bord Spring Boot" width="400"/>

**Option 2 : Utilisation de scripts shell**

Démarrez toutes les applications web (modules 01-04) :

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

Les deux scripts chargent automatiquement les variables d’environnement depuis le fichier `.env` racine et construiront les JARs s’ils n’existent pas.

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
.\stop.ps1  # Seulement ce module
# Ou
cd ..; .\stop-all.ps1  # Tous les modules
```

## Captures d'écran de l'application

<img src="../../../translated_images/fr/dashboard-home.5444dbda4bc1f79d.webp" alt="Accueil du tableau de bord" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Le tableau de bord principal affichant les 8 modèles d’ingénierie de prompt avec leurs caractéristiques et cas d’usage*

## Exploration des modèles

L’interface web vous permet d’expérimenter différentes stratégies de prompting. Chaque modèle résout des problèmes différents – testez-les pour voir quand chaque approche est la plus efficace.

### Faible vs Forte Enthousiasme

Posez une question simple comme "Quelle est 15 % de 200 ?" avec Faible Enthousiasme. Vous obtiendrez une réponse instantanée et directe. Maintenant posez quelque chose de complexe comme "Concevez une stratégie de mise en cache pour une API à fort trafic" avec Fort Enthousiasme. Regardez comment le modèle ralentit et fournit un raisonnement détaillé. Même modèle, même structure de question – mais le prompt lui indique l’effort de réflexion à fournir.

<img src="../../../translated_images/fr/low-eagerness-demo.898894591fb23aa0.webp" alt="Démo Faible Enthousiasme" width="800"/>

*Calcul rapide avec un raisonnement minimal*

<img src="../../../translated_images/fr/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Démo Fort Enthousiasme" width="800"/>

*Stratégie de mise en cache complète (2.8MB)*

### Exécution de Tâches (Préambules d’Outils)

Les workflows en plusieurs étapes bénéficient d’une planification préalable et d’une narration des progrès. Le modèle décrit ce qu’il va faire, narre chaque étape, puis résume les résultats.

<img src="../../../translated_images/fr/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Démo Exécution de Tâches" width="800"/>

*Création d’un endpoint REST avec narration pas à pas (3.9MB)*

### Code Autocritique

Essayez "Créer un service de validation d'e-mails". Au lieu de simplement générer du code puis s’arrêter, le modèle génère, évalue selon des critères de qualité, identifie les faiblesses, et améliore. Vous verrez le modèle itérer jusqu’à ce que le code réponde aux standards de production.

<img src="../../../translated_images/fr/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Démo Code Autocritique" width="800"/>

*Service complet de validation d’e-mails (5.2MB)*

### Analyse Structurée

Les revues de code nécessitent des cadres d’évaluation cohérents. Le modèle analyse le code en utilisant des catégories fixes (correction, pratiques, performance, sécurité) avec des niveaux de gravité.

<img src="../../../translated_images/fr/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Démo Analyse Structurée" width="800"/>

*Revue de code basée sur un cadre*

### Chat Multi-Tours

Demandez "Qu’est-ce que Spring Boot ?" puis immédiatement après "Montre-moi un exemple". Le modèle se souvient de votre première question et vous fournit un exemple spécifique de Spring Boot. Sans mémoire, cette deuxième question serait trop vague.

<img src="../../../translated_images/fr/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Démo Chat Multi-Tours" width="800"/>

*Conservation du contexte entre les questions*

### Raisonnement Pas à Pas

Choisissez un problème de maths et essayez-le à la fois avec Raisonnement Pas à Pas et Faible Enthousiasme. Faible enthousiasme vous donne simplement la réponse – rapide mais opaque. Pas à pas vous montre chaque calcul et décision.

<img src="../../../translated_images/fr/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Démo Raisonnement Pas à Pas" width="800"/>

*Problème de maths avec étapes explicites*

### Sortie Contraintes

Quand vous avez besoin de formats spécifiques ou de nombres de mots précis, ce modèle impose une stricte conformité. Essayez de générer un résumé contenant exactement 100 mots en format de points.

<img src="../../../translated_images/fr/constrained-output-demo.567cc45b75da1633.webp" alt="Démo Sortie Contraintes" width="800"/>

*Résumé en apprentissage automatique avec contrôle de format*

## Ce que vous apprenez réellement

**L’Effort de Raisonnement Change Tout**

GPT-5.2 vous permet de contrôler l’effort computationnel via vos prompts. Un faible effort signifie des réponses rapides avec une exploration minimale. Un effort élevé signifie que le modèle prend le temps de réfléchir profondément. Vous apprenez à ajuster l’effort selon la complexité de la tâche – ne perdez pas de temps sur des questions simples, mais ne bâclez pas non plus les décisions complexes.

**La Structure Guide le Comportement**

Vous avez remarqué les balises XML dans les prompts ? Elles ne sont pas décoratives. Les modèles suivent des instructions structurées plus fiablement que du texte libre. Quand vous avez besoin de processus multi-étapes ou de logique complexe, la structure aide le modèle à suivre où il en est et ce qui vient après.

<img src="../../../translated_images/fr/prompt-structure.a77763d63f4e2f89.webp" alt="Structure du prompt" width="800"/>

*Anatomie d’un prompt bien structuré avec sections claires et organisation de style XML*

**Qualité Grâce à l’Auto-Évaluation**

Les modèles autocritiques fonctionnent en rendant explicites les critères de qualité. Plutôt que d’espérer que le modèle "fasse bien", vous lui dites précisément ce que "bien" signifie : logique correcte, gestion d’erreur, performance, sécurité. Le modèle peut alors évaluer sa propre production et s’améliorer. Cela transforme la génération de code d’une loterie en un processus.

**Le Contexte Est Limité**

Les conversations multi-tours fonctionnent en incluant l’historique des messages à chaque requête. Mais il y a une limite – chaque modèle a un nombre maximal de tokens. Au fur et à mesure que les conversations grandissent, vous devrez adopter des stratégies pour garder un contexte pertinent sans atteindre ce plafond. Ce module vous montre comment la mémoire fonctionne ; plus tard vous apprendrez quand résumer, quand oublier, et quand récupérer.

## Prochaines étapes

**Module suivant :** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation :** [← Précédent : Module 01 - Introduction](../01-introduction/README.md) | [Retour au principal](../README.md) | [Suivant : Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :  
Ce document a été traduit à l’aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforçons d’assurer l’exactitude, veuillez noter que les traductions automatiques peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue native doit être considéré comme la source faisant foi. Pour les informations critiques, une traduction professionnelle effectuée par un humain est recommandée. Nous déclinons toute responsabilité en cas de malentendus ou de mauvaises interprétations résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->