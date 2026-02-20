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
  - [Modèles de prompts](../../../02-prompt-engineering)
- [Schémas avancés](../../../02-prompt-engineering)
- [Utilisation des ressources Azure existantes](../../../02-prompt-engineering)
- [Captures d'écran de l'application](../../../02-prompt-engineering)
- [Exploration des schémas](../../../02-prompt-engineering)
  - [Faible vs forte empressement](../../../02-prompt-engineering)
  - [Exécution de tâche (préambules d'outil)](../../../02-prompt-engineering)
  - [Code auto-réfléchissant](../../../02-prompt-engineering)
  - [Analyse structurée](../../../02-prompt-engineering)
  - [Chat multi-tour](../../../02-prompt-engineering)
  - [Raisonnement étape par étape](../../../02-prompt-engineering)
  - [Sortie contrainte](../../../02-prompt-engineering)
- [Ce que vous apprenez vraiment](../../../02-prompt-engineering)
- [Prochaines étapes](../../../02-prompt-engineering)

## Ce que vous apprendrez

<img src="../../../translated_images/fr/what-youll-learn.c68269ac048503b2.webp" alt="Ce que vous apprendrez" width="800"/>

Dans le module précédent, vous avez vu comment la mémoire permet l'IA conversationnelle et utilisé les modèles GitHub pour des interactions basiques. Nous allons maintenant nous concentrer sur la façon dont vous posez des questions — les prompts eux-mêmes — en utilisant GPT-5.2 d'Azure OpenAI. La manière dont vous structurez vos prompts influence considérablement la qualité des réponses obtenues. Nous commençons par une revue des techniques fondamentales de prompting, puis passons à huit schémas avancés qui exploitent pleinement les capacités de GPT-5.2.

Nous utilisons GPT-5.2 car il introduit le contrôle du raisonnement — vous pouvez indiquer au modèle combien de réflexion il doit effectuer avant de répondre. Cela rend les différentes stratégies de prompting plus évidentes et vous aide à comprendre quand utiliser chaque approche. Nous bénéficions également des limites de taux moindres d'Azure pour GPT-5.2 comparé aux modèles GitHub.

## Prérequis

- Module 01 terminé (ressources Azure OpenAI déployées)
- Fichier `.env` à la racine avec les identifiants Azure (créé par `azd up` dans le module 01)

> **Note :** Si vous n’avez pas terminé le module 01, suivez d’abord les instructions de déploiement qui y sont indiquées.

## Comprendre l'ingénierie des prompts

<img src="../../../translated_images/fr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Qu'est-ce que l'ingénierie des prompts ?" width="800"/>

L'ingénierie des prompts consiste à concevoir un texte d'entrée qui vous donne systématiquement les résultats dont vous avez besoin. Ce n’est pas simplement poser des questions — c’est structurer les requêtes afin que le modèle comprenne exactement ce que vous voulez et comment le fournir.

Pensez-y comme donner des instructions à un collègue. « Corrige le bug » est vague. « Corrige l'exception de pointeur nul dans UserService.java ligne 45 en ajoutant une vérification de null » est spécifique. Les modèles de langage fonctionnent de la même façon — la spécificité et la structure sont importantes.

<img src="../../../translated_images/fr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Comment LangChain4j s'intègre" width="800"/>

LangChain4j fournit l'infrastructure — connexions aux modèles, mémoire, et types de messages — tandis que les schémas de prompts ne sont que du texte soigneusement structuré que vous envoyez via cette infrastructure. Les éléments clés sont `SystemMessage` (qui définit le comportement et le rôle de l'IA) et `UserMessage` (qui porte votre demande réelle).

## Fondamentaux de l'ingénierie des prompts

<img src="../../../translated_images/fr/five-patterns-overview.160f35045ffd2a94.webp" alt="Vue d'ensemble des cinq schémas d'ingénierie des prompts" width="800"/>

Avant de plonger dans les schémas avancés de ce module, passons en revue cinq techniques fondamentales de prompting. Ce sont les blocs de construction que chaque ingénieur de prompts doit connaître. Si vous avez déjà suivi le [module de démarrage rapide](../00-quick-start/README.md#2-prompt-patterns), vous les avez vus en action — voici le cadre conceptuel qui les sous-tend.

### Zero-Shot Prompting

L'approche la plus simple : donner au modèle une instruction directe sans exemples. Le modèle s’appuie entièrement sur son entraînement pour comprendre et exécuter la tâche. Cela fonctionne bien pour les requêtes simples où le comportement attendu est évident.

<img src="../../../translated_images/fr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Instruction directe sans exemples — le modèle déduit la tâche uniquement à partir de l'instruction*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Réponse : "Positif"
```

**Quand l'utiliser :** classifications simples, questions directes, traductions, ou toute tâche que le modèle peut gérer sans guide supplémentaire.

### Few-Shot Prompting

Fournir des exemples qui démontrent le schéma que vous souhaitez que le modèle suive. Le modèle apprend le format attendu entrée-sortie à partir de vos exemples et l'applique aux nouvelles entrées. Cela améliore considérablement la cohérence pour les tâches où le format ou comportement désiré n’est pas évident.

<img src="../../../translated_images/fr/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Apprentissage à partir d'exemples — le modèle identifie le schéma et l’applique aux nouvelles entrées*

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

**Quand l'utiliser :** classifications personnalisées, formatage cohérent, tâches spécifiques au domaine, ou lorsque les résultats zero-shot sont incohérents.

### Chaîne de pensée

Demander au modèle de montrer son raisonnement étape par étape. Au lieu de donner directement une réponse, le modèle décompose le problème et analyse chaque partie explicitement. Cela améliore la précision sur les tâches de mathématiques, logique, et raisonnement à plusieurs étapes.

<img src="../../../translated_images/fr/chain-of-thought.5cff6630e2657e2a.webp" alt="Chaîne de pensée (Chain of Thought)" width="800"/>

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

**Quand l'utiliser :** problèmes mathématiques, énigmes logiques, débogage, ou toute tâche où montrer le raisonnement améliore la précision et la confiance.

### Prompting basé sur le rôle

Définir une persona ou un rôle pour l’IA avant de poser votre question. Cela fournit un contexte qui façonne le ton, la profondeur, et le focus de la réponse. Un « architecte logiciel » donne des conseils différents d’un « développeur junior » ou d’un « auditeur sécurité ».

<img src="../../../translated_images/fr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Prompting basé sur le rôle" width="800"/>

*Définir contexte et persona — la même question reçoit une réponse différente selon le rôle assigné*

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

**Quand l'utiliser :** revues de code, tutorat, analyses spécifiques au domaine, ou lorsque vous avez besoin de réponses adaptées à un niveau d’expertise ou perspective particulière.

### Modèles de prompts

Créer des prompts réutilisables avec des espaces réservés variables. Au lieu d’écrire un nouveau prompt à chaque fois, définissez un modèle une fois et remplissez-le avec différentes valeurs. La classe `PromptTemplate` de LangChain4j facilite cela avec la syntaxe `{{variable}}`.

<img src="../../../translated_images/fr/prompt-templates.14bfc37d45f1a933.webp" alt="Modèles de prompts" width="800"/>

*Prompts réutilisables avec espaces réservés variables — un modèle, plusieurs usages*

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

**Quand l'utiliser :** requêtes répétées avec différentes entrées, traitement par lot, création de workflows IA réutilisables, ou tout scénario où la structure du prompt reste la même mais les données changent.

---

Ces cinq fondamentaux vous offrent une boîte à outils solide pour la plupart des tâches de prompting. La suite de ce module s’appuie dessus avec **huit schémas avancés** qui exploitent le contrôle du raisonnement, l’auto-évaluation, et les capacités de sortie structurée de GPT-5.2.

## Schémas avancés

Avec les fondamentaux couverts, passons aux huit schémas avancés qui rendent ce module unique. Tous les problèmes ne nécessitent pas la même approche. Certaines questions appellent des réponses rapides, d’autres des réflexions approfondies. Certaines demandent un raisonnement visible, d’autres seulement des résultats. Chaque schéma ci-dessous est optimisé pour un scénario différent — et le contrôle du raisonnement de GPT-5.2 accentue encore ces différences.

<img src="../../../translated_images/fr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Huit schémas de prompting" width="800"/>

*Vue d'ensemble des huit schémas d'ingénierie des prompts et leurs cas d’usage*

<img src="../../../translated_images/fr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Contrôle du raisonnement avec GPT-5.2" width="800"/>

*Le contrôle du raisonnement de GPT-5.2 vous permet de spécifier la quantité de réflexion que doit faire le modèle — des réponses rapides et directes à une exploration approfondie*

**Faible empressement (rapide et ciblé)** - Pour les questions simples où vous souhaitez des réponses rapides et directes. Le modèle effectue un raisonnement minimal — maximum 2 étapes. Utilisez cela pour les calculs, recherches, ou questions simples.

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
> - « Quelle est la différence entre les schémas de prompting à faible et haute empressement ? »
> - « Comment les balises XML dans les prompts aident-elles à structurer la réponse de l'IA ? »
> - « Quand devrais-je utiliser les schémas d’auto-réflexion par rapport à l'instruction directe ? »

**Haut empressement (profond et complet)** - Pour les problèmes complexes où vous souhaitez une analyse complète. Le modèle explore en profondeur et montre un raisonnement détaillé. Utilisez cela pour la conception système, décisions d’architecture, ou recherches complexes.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Exécution de tâche (progression étape par étape)** - Pour les workflows à plusieurs étapes. Le modèle fournit un plan initial, narre chaque étape pendant qu’il travaille, puis donne un résumé. Utilisez cela pour les migrations, implémentations, ou tout processus multi-étapes.

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

Le prompting Chaîne de pensée demande explicitement au modèle de montrer son raisonnement, améliorant la précision sur les tâches complexes. La décomposition étape par étape aide aussi bien les humains que l’IA à comprendre la logique.

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Posez des questions sur ce schéma :
> - « Comment adapter le schéma d’exécution de tâche pour les opérations longues ? »
> - « Quelles sont les bonnes pratiques pour structurer les préambules d’outil en production ? »
> - « Comment capturer et afficher les mises à jour de progression intermédiaires dans une interface utilisateur ? »

<img src="../../../translated_images/fr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Schéma d’exécution de tâche" width="800"/>

*Planifier → Exécuter → Résumer pour les tâches multi-étapes*

**Code auto-réfléchissant** - Pour générer du code qualité production. Le modèle génère du code respectant les normes de production avec gestion appropriée des erreurs. Utilisez cela pour créer de nouvelles fonctionnalités ou services.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cycle d’auto-réflexion" width="800"/>

*Boucle d'amélioration itérative - générer, évaluer, identifier les problèmes, améliorer, répéter*

**Analyse structurée** - Pour une évaluation cohérente. Le modèle examine le code en suivant un cadre fixe (correction, pratiques, performance, sécurité, maintenabilité). Utilisez cela pour les revues de code ou évaluations qualité.

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

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Posez des questions sur l'analyse structurée :
> - « Comment personnaliser le cadre d’analyse pour différents types de revues de code ? »
> - « Quelle est la meilleure façon de parser et d'agir sur la sortie structurée de manière programmatique ? »
> - « Comment garantir une cohérence des niveaux de gravité entre différentes sessions de revue ? »

<img src="../../../translated_images/fr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Schéma d’analyse structurée" width="800"/>

*Cadre pour des revues de code cohérentes avec niveaux de gravité*

**Chat multi-tour** - Pour les conversations nécessitant du contexte. Le modèle se souvient des messages précédents et construit dessus. Utilisez cela pour des sessions d’aide interactives ou Q&A complexes.

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

*Comment le contexte de conversation s’accumule sur plusieurs tours jusqu’à atteindre la limite des tokens*

**Raisonnement étape par étape** - Pour les problèmes exigeant une logique visible. Le modèle montre un raisonnement explicite pour chaque étape. Utilisez cela pour les problèmes mathématiques, énigmes logiques, ou quand vous devez comprendre le processus de réflexion.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/fr/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Schéma étape par étape" width="800"/>

*Décomposer les problèmes en étapes logiques explicites*

**Sortie contrainte** - Pour des réponses avec des exigences de format spécifiques. Le modèle suit strictement les règles de format et longueur. Utilisez cela pour des résumés ou quand vous avez besoin d’une structure de sortie précise.

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

**Vérifiez le déploiement :**

Assurez-vous que le fichier `.env` existe à la racine avec les identifiants Azure (créé durant le Module 01) :
```bash
cat ../.env  # Doit afficher AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Démarrez l'application :**

> **Note :** Si vous avez déjà démarré toutes les applications avec la commande `./start-all.sh` du Module 01, ce module est déjà en fonctionnement sur le port 8083. Vous pouvez passer les commandes de démarrage ci-dessous et aller directement à http://localhost:8083.

**Option 1 : Utilisation du Spring Boot Dashboard (recommandé pour les utilisateurs VS Code)**

Le conteneur de développement inclut l’extension Spring Boot Dashboard, qui fournit une interface visuelle pour gérer toutes les applications Spring Boot. Vous pouvez la trouver dans la barre d’activité à gauche de VS Code (cherchez l'icône Spring Boot).

Depuis le Spring Boot Dashboard, vous pouvez :
- Voir toutes les applications Spring Boot disponibles dans l’espace de travail
- Démarrer/arrêter les applications d’un clic
- Visualiser les logs des applications en temps réel
- Surveiller l’état des applications
Il vous suffit de cliquer sur le bouton de lecture à côté de "prompt-engineering" pour démarrer ce module, ou de lancer tous les modules en même temps.

<img src="../../../translated_images/fr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Option 2 : Utilisation des scripts shell**

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

Ou ne démarrer que ce module :

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

> **Note :** Si vous préférez construire manuellement tous les modules avant de démarrer :
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
.\stop.ps1  # Ce module seulement
# Ou
cd ..; .\stop-all.ps1  # Tous les modules
```

## Captures d’écran de l’application

<img src="../../../translated_images/fr/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Le tableau de bord principal affichant les 8 modèles d’ingénierie de prompt avec leurs caractéristiques et cas d’utilisation*

## Exploration des modèles

L’interface web vous permet d’expérimenter différentes stratégies de prompt. Chaque modèle résout différents problèmes – essayez-les pour voir à quel moment chaque approche brille.

> **Note : Streaming vs Non-Streaming** — Chaque page de modèle propose deux boutons : **🔴 Stream Response (Live)** et une option **Non-streaming**. Le streaming utilise les Server-Sent Events (SSE) pour afficher les tokens en temps réel au fur et à mesure que le modèle les génère, vous voyez donc le progrès immédiatement. L’option non-streaming attend la réponse complète avant de l’afficher. Pour les prompts qui déclenchent un raisonnement approfondi (ex : High Eagerness, Code auto-réfléchi), l’appel non-streaming peut prendre très longtemps – parfois plusieurs minutes – sans retour visible. **Utilisez le streaming lors d’expérimentations avec des prompts complexes** afin de voir le modèle travailler et éviter de penser que la requête a expiré.
>
> **Note : Navigateur requis** — La fonctionnalité streaming utilise l’API Fetch Streams (`response.body.getReader()`) qui nécessite un navigateur complet (Chrome, Edge, Firefox, Safari). Elle ne fonctionne **pas** dans le Simple Browser intégré de VS Code, car sa webview ne supporte pas l’API ReadableStream. Si vous utilisez le Simple Browser, les boutons non-streaming fonctionneront normalement – seuls les boutons streaming sont impactés. Ouvrez `http://localhost:8083` dans un navigateur externe pour une expérience complète.

### Faible vs Forte implication (Eagerness)

Posez une question simple comme « Quel est 15 % de 200 ? » avec Faible Implication. Vous obtiendrez une réponse instantanée et directe. Maintenant, posez une question complexe comme « Concevez une stratégie de mise en cache pour une API à fort trafic » avec Forte Implication. Cliquez sur **🔴 Stream Response (Live)** et regardez le raisonnement détaillé du modèle s’afficher token par token. Même modèle, même structure de question – mais le prompt lui indique combien de réflexion faire.

### Exécution de tâche (Préambules d’outil)

Les workflows à étapes multiples bénéficient d’une planification préalable et d’une narration du progrès. Le modèle décrit ce qu’il va faire, raconte chaque étape, puis résume les résultats.

### Code auto-réfléchi

Essayez « Créez un service de validation d’email ». Au lieu de simplement générer du code et s’arrêter, le modèle génère, évalue selon des critères de qualité, identifie les faiblesses, puis améliore. Vous le verrez itérer jusqu’à ce que le code respecte les standards de production.

### Analyse structurée

Les revues de code nécessitent des cadres d’évaluation cohérents. Le modèle analyse le code selon des catégories fixes (justesse, bonnes pratiques, performance, sécurité) avec des niveaux de gravité.

### Discussion multi-tours

Demandez « Qu’est-ce que Spring Boot ? » puis enchaînez immédiatement avec « Montrez-moi un exemple ». Le modèle se souvient de votre première question et vous fournit un exemple Spring Boot spécifique. Sans mémoire, la deuxième question serait trop vague.

### Raisonnement étape par étape

Choisissez un problème mathématique et essayez-le avec Raisonnement étape par étape et Faible Implication. Faible implication vous donne juste la réponse – rapide mais opaque. Le raisonnement étape par étape vous montre chaque calcul et décision.

### Sortie contrainte

Quand vous avez besoin de formats ou de comptes de mots précis, ce modèle impose une stricte conformité. Essayez de générer un résumé avec exactement 100 mots sous forme de points.

## Ce que vous apprenez vraiment

**L’effort de raisonnement change tout**

GPT-5.2 vous permet de contrôler l’effort de calcul via vos prompts. Faible effort signifie des réponses rapides avec une exploration minimale. Fort effort signifie que le modèle prend du temps pour réfléchir profondément. Vous apprenez à adapter l’effort à la complexité de la tâche – ne perdez pas de temps sur des questions simples, mais ne vous précipitez pas non plus pour des décisions complexes.

**La structure guide le comportement**

Vous avez remarqué les balises XML dans les prompts ? Elles ne sont pas décoratives. Les modèles suivent des instructions structurées plus efficacement que du texte libre. Lorsque vous avez besoin de processus multi-étapes ou de logique complexe, la structure aide le modèle à suivre où il en est et ce qui vient ensuite.

<img src="../../../translated_images/fr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomie d’un prompt bien structuré avec des sections claires et une organisation de type XML*

**Qualité par auto-évaluation**

Les modèles auto-réfléchissants fonctionnent en rendant explicites les critères de qualité. Au lieu d’espérer que le modèle « fasse bien », vous lui dites exactement ce que signifie « bien » : logique correcte, gestion des erreurs, performance, sécurité. Le modèle peut alors évaluer sa propre sortie et s’améliorer. Cela transforme la génération de code en un processus contrôlé.

**Le contexte est fini**

Les conversations multi-tours fonctionnent en incluant l’historique des messages à chaque requête. Mais il y a une limite – chaque modèle possède un nombre maximal de tokens. À mesure que les conversations grandissent, vous devrez adopter des stratégies pour conserver un contexte pertinent sans dépasser ce plafond. Ce module vous montre comment fonctionne la mémoire ; plus tard, vous apprendrez quand résumer, quand oublier, et quand récupérer.

## Prochaines étapes

**Module suivant :** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation :** [← Précédent : Module 01 - Introduction](../01-introduction/README.md) | [Retour au principal](../README.md) | [Suivant : Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :  
Ce document a été traduit à l’aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforcions d’assurer l’exactitude, veuillez noter que les traductions automatiques peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue d’origine doit être considéré comme la source faisant autorité. Pour les informations critiques, il est recommandé de recourir à une traduction professionnelle réalisée par un humain. Nous déclinons toute responsabilité en cas de malentendus ou de mauvaises interprétations résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->