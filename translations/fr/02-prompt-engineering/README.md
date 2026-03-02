# Module 02 : Ingénierie des prompts avec GPT-5.2

## Table des matières

- [Présentation vidéo](../../../02-prompt-engineering)
- [Ce que vous apprendrez](../../../02-prompt-engineering)
- [Prérequis](../../../02-prompt-engineering)
- [Comprendre l'ingénierie des prompts](../../../02-prompt-engineering)
- [Fondamentaux de l'ingénierie des prompts](../../../02-prompt-engineering)
  - [Prompting zéro-shot](../../../02-prompt-engineering)
  - [Prompting few-shot](../../../02-prompt-engineering)
  - [Chaîne de pensée](../../../02-prompt-engineering)
  - [Prompting basé sur un rôle](../../../02-prompt-engineering)
  - [Modèles de prompts](../../../02-prompt-engineering)
- [Schémas avancés](../../../02-prompt-engineering)
- [Utilisation des ressources Azure existantes](../../../02-prompt-engineering)
- [Captures d'écran de l'application](../../../02-prompt-engineering)
- [Explorer les schémas](../../../02-prompt-engineering)
  - [Faible vs haute impatience](../../../02-prompt-engineering)
  - [Exécution des tâches (Préambules des outils)](../../../02-prompt-engineering)
  - [Code auto-réfléchissant](../../../02-prompt-engineering)
  - [Analyse structurée](../../../02-prompt-engineering)
  - [Chat multi-interactions](../../../02-prompt-engineering)
  - [Raisonnement étape par étape](../../../02-prompt-engineering)
  - [Sortie contrainte](../../../02-prompt-engineering)
- [Ce que vous apprenez vraiment](../../../02-prompt-engineering)
- [Étapes suivantes](../../../02-prompt-engineering)

## Présentation vidéo

Regardez cette session en direct qui explique comment démarrer avec ce module :

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Ingénierie des prompts avec LangChain4j - Session en direct" width="800"/></a>

## Ce que vous apprendrez

<img src="../../../translated_images/fr/what-youll-learn.c68269ac048503b2.webp" alt="Ce que vous apprendrez" width="800"/>

Dans le module précédent, vous avez vu comment la mémoire permet à l'IA conversationnelle et utilisé les modèles GitHub pour des interactions basiques. Maintenant, nous allons nous concentrer sur la façon dont vous posez des questions — les prompts eux-mêmes — en utilisant GPT-5.2 d'Azure OpenAI. La manière dont vous structurez vos prompts influence énormément la qualité des réponses obtenues. Nous commençons par une revue des techniques fondamentales de prompting, puis passons à huit schémas avancés qui exploitent pleinement les capacités de GPT-5.2.

Nous utilisons GPT-5.2 parce qu'il introduit un contrôle du raisonnement - vous pouvez indiquer au modèle la quantité de réflexion avant de répondre. Cela rend les différentes stratégies de prompting plus apparentes et vous aide à comprendre quand utiliser chaque approche. Nous bénéficierons également des limites de taux plus faibles d'Azure pour GPT-5.2 comparé aux modèles GitHub.

## Prérequis

- Module 01 complété (ressources Azure OpenAI déployées)
- Fichier `.env` dans le répertoire racine avec les identifiants Azure (créé par `azd up` dans le Module 01)

> **Note :** Si vous n'avez pas complété le Module 01, suivez d'abord les instructions de déploiement là-bas.

## Comprendre l'ingénierie des prompts

<img src="../../../translated_images/fr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Qu'est-ce que l'ingénierie des prompts ?" width="800"/>

L'ingénierie des prompts consiste à concevoir un texte d'entrée qui vous donne constamment les résultats souhaités. Ce n'est pas juste poser des questions – c'est structurer des requêtes pour que le modèle comprenne exactement ce que vous voulez et comment le fournir.

Pensez-y comme donner des instructions à un collègue. « Corrige le bug » est vague. « Corrige l'exception de pointeur nul dans UserService.java ligne 45 en ajoutant une vérification de null » est précis. Les modèles de langage fonctionnent de la même manière – spécificité et structure comptent.

<img src="../../../translated_images/fr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Comment LangChain4j s'intègre" width="800"/>

LangChain4j fournit l'infrastructure — connexions aux modèles, mémoire, types de messages — tandis que les schémas de prompts ne sont que du texte soigneusement structuré que vous envoyez via cette infrastructure. Les blocs de construction clés sont `SystemMessage` (qui définit le comportement et le rôle de l'IA) et `UserMessage` (qui porte votre requête réelle).

## Fondamentaux de l'ingénierie des prompts

<img src="../../../translated_images/fr/five-patterns-overview.160f35045ffd2a94.webp" alt="Vue d'ensemble des cinq schémas d'ingénierie des prompts" width="800"/>

Avant de plonger dans les schémas avancés de ce module, passons en revue cinq techniques fondamentales de prompting. Ce sont les blocs de construction que tout ingénieur en prompts devrait connaître. Si vous avez déjà travaillé avec le [module de démarrage rapide](../00-quick-start/README.md#2-prompt-patterns), vous les avez vus en action — voici le cadre conceptuel qui les sous-tend.

### Prompting zéro-shot

L'approche la plus simple : donner une instruction directe au modèle sans exemples. Le modèle s'appuie entièrement sur son entraînement pour comprendre et exécuter la tâche. Cela fonctionne bien pour des requêtes simples où le comportement attendu est évident.

<img src="../../../translated_images/fr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Prompting zéro-shot" width="800"/>

*Instruction directe sans exemples — le modèle déduit la tâche à partir de l'instruction seule*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Réponse : "Positif"
```

**Quand utiliser :** Classifications simples, questions directes, traductions ou toute tâche que le modèle peut gérer sans guide supplémentaire.

### Prompting few-shot

Fournir des exemples qui montrent le schéma que vous voulez que le modèle suive. Le modèle apprend le format attendu entrée-sortie à partir de vos exemples et l'applique à de nouvelles entrées. Cela améliore considérablement la cohérence pour des tâches où le format ou le comportement souhaité n'est pas évident.

<img src="../../../translated_images/fr/few-shot-prompting.9d9eace1da88989a.webp" alt="Prompting few-shot" width="800"/>

*Apprendre à partir d'exemples — le modèle identifie le schéma et l'applique à de nouvelles entrées*

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

**Quand utiliser :** Classifications personnalisées, formatage cohérent, tâches spécifiques au domaine ou quand les résultats zéro-shot sont incohérents.

### Chaîne de pensée

Demander au modèle de montrer son raisonnement étape par étape. Plutôt que de sauter directement à la réponse, le modèle décompose le problème et travaille explicitement chaque partie. Cela améliore la précision sur les maths, la logique et les raisonnements en plusieurs étapes.

<img src="../../../translated_images/fr/chain-of-thought.5cff6630e2657e2a.webp" alt="Prompting chaîne de pensée" width="800"/>

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

**Quand utiliser :** Problèmes mathématiques, énigmes logiques, débogage ou toute tâche où montrer le processus de raisonnement améliore la précision et la confiance.

### Prompting basé sur un rôle

Attribuer un rôle ou une personnalité à l'IA avant de poser la question. Cela fournit un contexte qui façonne le ton, la profondeur et le focus de la réponse. Un « architecte logiciel » donne des conseils différents d'un « développeur junior » ou d'un « auditeur sécurité ».

<img src="../../../translated_images/fr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Prompting basé sur un rôle" width="800"/>

*Définir le contexte et la personnalité — la même question reçoit une réponse différente selon le rôle assigné*

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

**Quand utiliser :** Revues de code, tutorat, analyses spécifiques au domaine ou lorsque vous avez besoin de réponses adaptées à un niveau d'expertise ou une perspective particulière.

### Modèles de prompts

Créer des prompts réutilisables avec des espaces réservés variables. Au lieu d'écrire un nouveau prompt à chaque fois, définissez un modèle une fois et remplissez-le avec des valeurs différentes. La classe `PromptTemplate` de LangChain4j facilite cela avec la syntaxe `{{variable}}`.

<img src="../../../translated_images/fr/prompt-templates.14bfc37d45f1a933.webp" alt="Modèles de prompts" width="800"/>

*Prompts réutilisables avec espaces réservés variables — un modèle, plusieurs utilisations*

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

**Quand utiliser :** Requêtes répétées avec différentes entrées, traitement par lots, création de flux de travail IA réutilisables ou tout scénario où la structure du prompt reste la même mais les données changent.

---

Ces cinq fondamentaux vous fournissent une boîte à outils solide pour la plupart des tâches de prompting. Le reste de ce module s'appuie dessus avec **huit schémas avancés** qui tirent parti du contrôle du raisonnement, de l'auto-évaluation et des capacités de sortie structurée de GPT-5.2.

## Schémas avancés

Avec les fondamentaux couverts, passons aux huit schémas avancés qui rendent ce module unique. Tous les problèmes ne nécessitent pas la même approche. Certaines questions demandent des réponses rapides, d’autres une réflexion approfondie. Certaines ont besoin d’un raisonnement visible, d’autres seulement de résultats. Chaque schéma ci-dessous est optimisé pour un scénario différent — et le contrôle du raisonnement de GPT-5.2 rend les différences encore plus marquées.

<img src="../../../translated_images/fr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Huit schémas de prompting" width="800"/>

*Vue d'ensemble des huit schémas d'ingénierie des prompts et leurs cas d'utilisation*

<img src="../../../translated_images/fr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Contrôle du raisonnement avec GPT-5.2" width="800"/>

*Le contrôle du raisonnement de GPT-5.2 vous permet de spécifier la quantité de réflexion du modèle — des réponses rapides et directes à une exploration approfondie*

**Faible impatience (rapide et ciblé)** - Pour les questions simples où vous voulez des réponses rapides et directes. Le modèle fait un raisonnement minimal - maximum 2 étapes. Utilisez ceci pour des calculs, recherches ou questions simples.

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
> - « Quelle est la différence entre les schémas de prompting à faible et haute impatience ? »
> - « Comment les balises XML dans les prompts aident-elles à structurer la réponse de l'IA ? »
> - « Quand devrais-je utiliser les schémas d’auto-réflexion par rapport à l’instruction directe ? »

**Haute impatience (profond et complet)** - Pour les problèmes complexes où vous souhaitez une analyse complète. Le modèle explore en profondeur et montre un raisonnement détaillé. Utilisez ceci pour la conception système, les décisions architecturales ou des recherches complexes.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Exécution des tâches (progrès étape par étape)** - Pour les flux de travail en plusieurs étapes. Le modèle fournit un plan à l'avance, narre chaque étape durant l'exécution, puis donne un résumé. Utilisez ceci pour migrations, implémentations ou tout processus en plusieurs étapes.

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

Le prompting chaîne de pensée demande explicitement au modèle de montrer son raisonnement, améliorant la précision pour les tâches complexes. La décomposition étape par étape aide à la compréhension humaine et IA.

> **🤖 Essayez avec le chat [GitHub Copilot](https://github.com/features/copilot) :** Posez des questions sur ce schéma :
> - « Comment adapter le schéma d'exécution de tâche pour des opérations longues ? »
> - « Quelles sont les bonnes pratiques pour structurer les préambules d'outils en production ? »
> - « Comment capturer et afficher les mises à jour de progrès intermédiaires dans une interface utilisateur ? »

<img src="../../../translated_images/fr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Schéma d'exécution des tâches" width="800"/>

*Planifier → Exécuter → Résumer pour les tâches multi-étapes*

**Code auto-réfléchissant** - Pour générer du code de qualité production. Le modèle génère du code suivant les standards de production avec gestion appropriée des erreurs. Utilisez ceci pour construire de nouvelles fonctionnalités ou services.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cycle d'auto-réflexion" width="800"/>

*Boucle d'amélioration itérative - générer, évaluer, identifier les problèmes, améliorer, répéter*

**Analyse structurée** - Pour une évaluation cohérente. Le modèle révise le code en utilisant un cadre fixe (correction, pratiques, performance, sécurité, maintenabilité). Utilisez ceci pour les revues de code ou évaluations qualité.

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

> **🤖 Essayez avec le chat [GitHub Copilot](https://github.com/features/copilot) :** Posez des questions sur l'analyse structurée :
> - « Comment personnaliser le cadre d'analyse pour différents types de revues de code ? »
> - « Quelle est la meilleure façon de parser et d'agir sur une sortie structurée de façon programmatique ? »
> - « Comment garantir des niveaux de gravité cohérents entre différentes sessions de revue ? »

<img src="../../../translated_images/fr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Schéma d'analyse structurée" width="800"/>

*Cadre pour des revues de code cohérentes avec niveaux de gravité*

**Chat multi-interactions** - Pour des conversations nécessitant du contexte. Le modèle se souvient des messages précédents et construit dessus. Utilisez ceci pour des sessions d’aide interactives ou Q&A complexes.

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

*Comment le contexte de la conversation s'accumule sur plusieurs échanges jusqu'à atteindre la limite des tokens*

**Raisonnement étape par étape** - Pour des problèmes nécessitant une logique visible. Le modèle montre un raisonnement explicite pour chaque étape. Utilisez ceci pour mathématiques, énigmes logiques ou lorsque vous avez besoin de comprendre le processus de réflexion.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fr/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Schéma pas à pas" width="800"/>

*Décomposer les problèmes en étapes logiques explicites*

**Sortie contrainte** - Pour des réponses avec des exigences spécifiques de format. Le modèle suit strictement les règles de format et de longueur. Utilisez ceci pour des résumés ou lorsque vous avez besoin d’une structure précise de sortie.

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

<img src="../../../translated_images/fr/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Schéma de sortie contrainte" width="800"/>

*Application stricte des exigences de format, longueur et structure*

## Utilisation des ressources Azure existantes

**Vérifier le déploiement :**

Assurez-vous que le fichier `.env` existe dans le répertoire racine avec les identifiants Azure (créé lors du Module 01) :
```bash
cat ../.env  # Devrait afficher AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Démarrer l'application :**

> **Note :** Si vous avez déjà démarré toutes les applications avec `./start-all.sh` depuis le Module 01, ce module est déjà en cours d'exécution sur le port 8083. Vous pouvez ignorer les commandes de démarrage ci-dessous et aller directement sur http://localhost:8083.
**Option 1 : Utiliser le Spring Boot Dashboard (Recommandé pour les utilisateurs de VS Code)**

Le conteneur de développement inclut l’extension Spring Boot Dashboard, qui fournit une interface visuelle pour gérer toutes les applications Spring Boot. Vous pouvez la trouver dans la barre d’activités à gauche de VS Code (cherchez l’icône Spring Boot).

Depuis le Spring Boot Dashboard, vous pouvez :
- Voir toutes les applications Spring Boot disponibles dans l’espace de travail
- Démarrer/arrêter les applications en un clic
- Voir les journaux des applications en temps réel
- Surveiller le statut des applications

Cliquez simplement sur le bouton lecture à côté de « prompt-engineering » pour démarrer ce module, ou lancez tous les modules en même temps.

<img src="../../../translated_images/fr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Option 2 : Utiliser des scripts shell**

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

Les deux scripts chargent automatiquement les variables d’environnement depuis le fichier `.env` à la racine et compileront les JAR si ceux-ci n’existent pas.

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

## Captures d'écran de l'application

<img src="../../../translated_images/fr/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Le tableau de bord principal affichant les 8 patterns de prompt engineering avec leurs caractéristiques et cas d’utilisation*

## Exploration des Patterns

L’interface web vous permet d’expérimenter différentes stratégies de prompting. Chaque pattern résout des problèmes différents - essayez-les pour voir quand chaque approche excelle.

> **Note : Streaming vs Non-Streaming** — Chaque page de pattern offre deux boutons : **🔴 Stream Response (Live)** et une option **Non-streaming**. Le streaming utilise les Server-Sent Events (SSE) pour afficher les tokens en temps réel au fur et à mesure que le modèle les génère, vous voyez donc immédiatement la progression. L’option non-streaming attend la réponse complète avant de l’afficher. Pour les prompts qui déclenchent un raisonnement approfondi (par exemple, High Eagerness, Self-Reflecting Code), l’appel non-streaming peut prendre énormément de temps — parfois des minutes — sans aucun retour visible. **Utilisez le streaming lors de l’expérimentation de prompts complexes** pour voir le modèle travailler et éviter l’impression que la requête a expiré.
>
> **Note : Nécessité d’un navigateur** — La fonctionnalité de streaming utilise l’API Fetch Streams (`response.body.getReader()`) qui nécessite un navigateur complet (Chrome, Edge, Firefox, Safari). Elle ne fonctionne **pas** dans le Simple Browser intégré à VS Code, car sa webview ne supporte pas l’API ReadableStream. Si vous utilisez le Simple Browser, les boutons non-streaming fonctionneront normalement — seuls les boutons de streaming sont concernés. Ouvrez `http://localhost:8083` dans un navigateur externe pour profiter pleinement de la fonctionnalité.

### Low vs High Eagerness

Posez une question simple comme « Quel est 15 % de 200 ? » en utilisant Low Eagerness. Vous obtiendrez une réponse instantanée et directe. Maintenant posez une question complexe comme « Concevez une stratégie de cache pour une API à fort trafic » en utilisant High Eagerness. Cliquez sur **🔴 Stream Response (Live)** et regardez le raisonnement détaillé du modèle apparaître token par token. Même modèle, même structure de question — mais le prompt lui indique l’intensité de réflexion à appliquer.

### Exécution de tâches (Préambules d’outils)

Les workflows en plusieurs étapes bénéficient d’une planification préalable et d’une narration de la progression. Le modèle décrit ce qu’il va faire, narre chaque étape, puis résume les résultats.

### Code Auto-Réflexif

Essayez « Créer un service de validation d’email ». Au lieu de simplement générer du code et s’arrêter, le modèle génère, évalue selon des critères de qualité, identifie les faiblesses, puis améliore. Vous verrez le modèle itérer jusqu’à ce que le code réponde aux standards de production.

### Analyse Structurée

Les revues de code nécessitent des cadres d’évaluation cohérents. Le modèle analyse le code en utilisant des catégories fixes (justesse, bonnes pratiques, performance, sécurité) avec des niveaux de gravité.

### Conversation Multi-Échanges

Demandez « Qu’est-ce que Spring Boot ? » puis immédiatement « Montre-moi un exemple ». Le modèle se souvient de votre première question et vous donne un exemple spécifique à Spring Boot. Sans mémoire, cette deuxième question serait trop vague.

### Raisonnement Pas à Pas

Choisissez un problème de mathématiques et testez-le avec Raisonnement Pas à Pas et Low Eagerness. Low eagerness vous donne juste la réponse — rapide mais opaque. Le raisonnement étape par étape vous montre tous les calculs et décisions.

### Sortie Contraignée

Lorsque vous avez besoin de formats spécifiques ou de décomptes de mots, ce pattern impose une stricte conformité. Essayez de générer un résumé en exactement 100 mots sous forme de points.

## Ce que vous apprenez vraiment

**L’effort de raisonnement change tout**

GPT-5.2 vous permet de contrôler l’effort de calcul via vos prompts. Un faible effort signifie des réponses rapides avec une exploration minimale. Un effort élevé signifie que le modèle prend le temps de réfléchir en profondeur. Vous apprenez à ajuster l’effort à la complexité de la tâche — ne perdez pas de temps sur des questions simples, mais ne bâclez pas les décisions complexes non plus.

**La structure guide le comportement**

Vous avez remarqué les balises XML dans les prompts ? Elles ne sont pas décoratives. Les modèles suivent plus fidèlement des instructions structurées que du texte libre. Lorsque vous avez besoin de processus en plusieurs étapes ou d’une logique complexe, la structure aide le modèle à savoir où il en est et ce qu’il doit faire ensuite.

<img src="../../../translated_images/fr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomie d’un prompt bien structuré avec sections claires et organisation de type XML*

**Qualité par auto-évaluation**

Les patterns auto-réflexifs fonctionnent en rendant explicites les critères de qualité. Au lieu d’espérer que le modèle « fasse bien », vous lui dites exactement ce que signifie « bien » : logique correcte, gestion des erreurs, performance, sécurité. Le modèle peut alors évaluer sa propre sortie et s’améliorer. Cela transforme la génération de code en un processus maîtrisé.

**Le contexte est limité**

Les conversations multi-échanges fonctionnent en incluant l’historique des messages à chaque requête. Mais il y a une limite — chaque modèle a un nombre maximal de tokens. Au fur et à mesure que la conversation s’allonge, vous devrez utiliser des stratégies pour garder le contexte pertinent sans dépasser ce plafond. Ce module vous montre comment fonctionne la mémoire ; vous apprendrez plus tard quand résumer, quand oublier, et quand récupérer.

## Étapes suivantes

**Module suivant :** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation :** [← Précédent : Module 01 - Introduction](../01-introduction/README.md) | [Retour au principal](../README.md) | [Suivant : Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Clause de non-responsabilité** :  
Ce document a été traduit à l’aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforcions d’assurer l’exactitude, veuillez noter que les traductions automatiques peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue d’origine doit être considéré comme la source faisant foi. Pour les informations critiques, une traduction professionnelle réalisée par un humain est recommandée. Nous déclinons toute responsabilité en cas de malentendus ou de mauvaises interprétations résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->