# Module 02 : Ingénierie des prompts avec GPT-5.2

## Table des matières

- [Présentation vidéo](../../../02-prompt-engineering)
- [Ce que vous apprendrez](../../../02-prompt-engineering)
- [Prérequis](../../../02-prompt-engineering)
- [Comprendre l'ingénierie des prompts](../../../02-prompt-engineering)
- [Fondamentaux de l'ingénierie des prompts](../../../02-prompt-engineering)
  - [Prompting Zero-Shot](../../../02-prompt-engineering)
  - [Prompting Few-Shot](../../../02-prompt-engineering)
  - [Chaîne de pensée](../../../02-prompt-engineering)
  - [Prompting basé sur les rôles](../../../02-prompt-engineering)
  - [Modèles de prompts](../../../02-prompt-engineering)
- [Schémas avancés](../../../02-prompt-engineering)
- [Exécuter l'application](../../../02-prompt-engineering)
- [Captures d'écran de l'application](../../../02-prompt-engineering)
- [Exploration des schémas](../../../02-prompt-engineering)
  - [Faible vs Forte Empressement](../../../02-prompt-engineering)
  - [Exécution de tâches (préambules d'outils)](../../../02-prompt-engineering)
  - [Code auto-réfléchissant](../../../02-prompt-engineering)
  - [Analyse structurée](../../../02-prompt-engineering)
  - [Chat multi-tours](../../../02-prompt-engineering)
  - [Raisonnement étape par étape](../../../02-prompt-engineering)
  - [Sortie contrainte](../../../02-prompt-engineering)
- [Ce que vous apprenez vraiment](../../../02-prompt-engineering)
- [Étapes suivantes](../../../02-prompt-engineering)

## Présentation vidéo

Regardez cette session en direct qui explique comment démarrer avec ce module :

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Ingénierie des prompts avec LangChain4j - Session en direct" width="800"/></a>

## Ce que vous apprendrez

Le diagramme suivant donne un aperçu des sujets clés et des compétences que vous développerez dans ce module — des techniques de raffinage des prompts au workflow étape par étape que vous suivrez.

<img src="../../../translated_images/fr/what-youll-learn.c68269ac048503b2.webp" alt="Ce que vous apprendrez" width="800"/>

Dans les modules précédents, vous avez exploré les interactions basiques de LangChain4j avec les modèles GitHub et vu comment la mémoire permet l'IA conversationnelle avec Azure OpenAI. Nous allons maintenant nous concentrer sur la manière dont vous posez des questions — les prompts eux-mêmes — en utilisant GPT-5.2 d'Azure OpenAI. La façon dont vous structurez vos prompts influence considérablement la qualité des réponses. Nous commençons par une revue des techniques fondamentales de prompting, puis passons à huit schémas avancés qui exploitent pleinement les capacités de GPT-5.2.

Nous utiliserons GPT-5.2 car il introduit le contrôle du raisonnement — vous pouvez indiquer au modèle combien de réflexion effectuer avant de répondre. Cela rend les différentes stratégies de prompting plus apparentes et vous aide à comprendre quand utiliser chaque approche. Nous profiterons également des limites de taux plus souples d'Azure pour GPT-5.2 par rapport aux modèles GitHub.

## Prérequis

- Module 01 complété (ressources Azure OpenAI déployées)
- Fichier `.env` dans le répertoire racine avec les identifiants Azure (créé par `azd up` dans le Module 01)

> **Note :** Si vous n'avez pas terminé le Module 01, suivez d'abord les instructions de déploiement là-bas.

## Comprendre l'ingénierie des prompts

Fondamentalement, l'ingénierie des prompts est la différence entre des consignes vagues et précises, comme l'illustre la comparaison ci-dessous.

<img src="../../../translated_images/fr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Qu'est-ce que l'ingénierie des prompts ?" width="800"/>

L'ingénierie des prompts consiste à concevoir un texte d'entrée qui vous donne systématiquement les résultats dont vous avez besoin. Il ne s'agit pas seulement de poser des questions — il s'agit de structurer les requêtes pour que le modèle comprenne exactement ce que vous voulez et comment le fournir.

Pensez-y comme donner des instructions à un collègue. « Corrige le bug » est vague. « Corrige l'exception de pointeur nul dans UserService.java ligne 45 en ajoutant une vérification de null » est précis. Les modèles de langage fonctionnent de la même manière — la précision et la structure comptent.

Le diagramme ci-dessous montre comment LangChain4j s'intègre dans ce schéma — connectant vos modèles de prompt au modèle via les blocs `SystemMessage` et `UserMessage`.

<img src="../../../translated_images/fr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Comment LangChain4j s'intègre" width="800"/>

LangChain4j fournit l'infrastructure — connexions aux modèles, mémoire et types de messages — tandis que les schémas de prompt sont simplement du texte structuré que vous envoyez à travers cette infrastructure. Les blocs de construction clés sont `SystemMessage` (qui définit le comportement et le rôle de l'IA) et `UserMessage` (qui porte votre demande réelle).

## Fondamentaux de l'ingénierie des prompts

Les cinq techniques principales montrées ci-dessous forment la base d'une ingénierie des prompts efficace. Chacune aborde un aspect différent de la façon dont vous communiquez avec les modèles de langage.

<img src="../../../translated_images/fr/five-patterns-overview.160f35045ffd2a94.webp" alt="Vue d'ensemble des cinq schémas d'ingénierie des prompts" width="800"/>

Avant de plonger dans les schémas avancés de ce module, revoyons cinq techniques fondamentales de prompting. Ce sont les blocs de construction que tout ingénieur en prompts devrait connaître. Si vous avez déjà travaillé le [module de démarrage rapide](../00-quick-start/README.md#2-prompt-patterns), vous les avez vus en action — voici le cadre conceptuel derrière eux.

### Prompting Zero-Shot

L'approche la plus simple : donnez au modèle une instruction directe sans exemples. Le modèle s'appuie entièrement sur son entraînement pour comprendre et exécuter la tâche. Cela fonctionne bien pour des demandes simples où le comportement attendu est évident.

<img src="../../../translated_images/fr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Prompting Zero-Shot" width="800"/>

*Instruction directe sans exemples — le modèle déduit la tâche à partir de l'instruction seule*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Réponse : "Positif"
```

**Quand utiliser :** classifications simples, questions directes, traductions, ou toute tâche que le modèle peut gérer sans orientation supplémentaire.

### Prompting Few-Shot

Fournissez des exemples qui montrent le modèle le schéma que vous voulez qu’il suive. Le modèle apprend le format entrée-sortie attendu à partir de vos exemples et l'applique aux nouvelles entrées. Cela améliore considérablement la cohérence pour des tâches où le format ou le comportement désiré n'est pas évident.

<img src="../../../translated_images/fr/few-shot-prompting.9d9eace1da88989a.webp" alt="Prompting Few-Shot" width="800"/>

*Apprentissage par exemples — le modèle identifie le schéma et l'applique aux nouvelles entrées*

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

**Quand utiliser :** classifications personnalisées, formatage cohérent, tâches spécifiques à un domaine, ou lorsque les résultats zero-shot sont incohérents.

### Chaîne de pensée

Demandez au modèle de montrer son raisonnement étape par étape. Au lieu de sauter directement à une réponse, le modèle décompose le problème et travaille chaque partie explicitement. Cela améliore la précision pour les mathématiques, la logique et les raisonnements multi-étapes.

<img src="../../../translated_images/fr/chain-of-thought.5cff6630e2657e2a.webp" alt="Chaîne de pensée" width="800"/>

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

**Quand utiliser :** problèmes mathématiques, énigmes logiques, débogage, ou toute tâche où montrer le raisonnement améliore l'exactitude et la confiance.

### Prompting basé sur les rôles

Définissez une persona ou un rôle pour l'IA avant de poser votre question. Cela fournit un contexte qui façonne le ton, la profondeur et le focus de la réponse. Un « architecte logiciel » donne des conseils différents d'un « développeur junior » ou d'un « auditeur sécurité ».

<img src="../../../translated_images/fr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Prompting basé sur les rôles" width="800"/>

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

**Quand utiliser :** revues de code, tutorat, analyses spécifiques à un domaine, ou lorsque vous avez besoin de réponses adaptées à un niveau d’expertise ou une perspective particulière.

### Modèles de prompts

Créez des prompts réutilisables avec des espaces réservés variables. Au lieu d'écrire un nouveau prompt à chaque fois, définissez un modèle une fois et remplissez-le avec différentes valeurs. La classe `PromptTemplate` de LangChain4j rend cela simple avec la syntaxe `{{variable}}`.

<img src="../../../translated_images/fr/prompt-templates.14bfc37d45f1a933.webp" alt="Modèles de prompts" width="800"/>

*Prompts réutilisables avec espaces réservés variables — un modèle, de nombreuses utilisations*

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

**Quand utiliser :** requêtes répétées avec différentes entrées, traitement par lots, création de workflows IA réutilisables, ou tout scénario où la structure du prompt reste la même mais les données changent.

---

Ces cinq fondamentaux vous donnent un solide kit d’outils pour la plupart des tâches de prompting. Le reste de ce module s’appuie dessus avec **huit schémas avancés** qui tirent parti du contrôle du raisonnement, de l'auto-évaluation et des capacités de sortie structurée de GPT-5.2.

## Schémas avancés

Avec les fondamentaux couverts, passons aux huit schémas avancés qui rendent ce module unique. Tous les problèmes n’ont pas besoin de la même approche. Certaines questions demandent des réponses rapides, d’autres une réflexion profonde. Certaines ont besoin d'un raisonnement visible, d’autres seulement des résultats. Chaque schéma ci-dessous est optimisé pour un scénario différent — et le contrôle du raisonnement de GPT-5.2 rend les différences encore plus marquées.

<img src="../../../translated_images/fr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Huit schémas de prompting" width="800"/>

*Vue d'ensemble des huit schémas d'ingénierie des prompts et leurs cas d'usage*

GPT-5.2 ajoute une autre dimension à ces schémas : *le contrôle du raisonnement*. Le curseur ci-dessous montre comment vous pouvez ajuster l’effort de réflexion du modèle — depuis des réponses rapides et directes jusqu’à une analyse profonde et minutieuse.

<img src="../../../translated_images/fr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Contrôle du raisonnement avec GPT-5.2" width="800"/>

*Le contrôle du raisonnement de GPT-5.2 vous permet de spécifier combien de réflexion le modèle doit faire — des réponses rapides aux explorations approfondies*

**Faible empressement (Rapide & ciblé)** - Pour des questions simples où vous voulez des réponses rapides et directes. Le modèle fait un raisonnement minimal - maximum 2 étapes. Utilisez cela pour des calculs, recherches, ou questions simples.

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
> - « Quelle est la différence entre les schémas de faible et fort empressement en prompting ? »
> - « Comment les balises XML dans les prompts aident-elles à structurer la réponse de l’IA ? »
> - « Quand devrais-je utiliser les schémas d'auto-réflexion vs l'instruction directe ? »

**Fort empressement (Profond & minutieux)** - Pour des problèmes complexes où vous voulez une analyse complète. Le modèle explore en profondeur et montre un raisonnement détaillé. Utilisez cela pour la conception de systèmes, décisions d’architecture, ou recherches complexes.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Exécution de tâches (progrès étape par étape)** - Pour des workflows multi-étapes. Le modèle fournit un plan initial, décrit chaque étape au fur et à mesure, puis donne un résumé. Utilisez cela pour des migrations, implémentations, ou tout processus multi-étapes.

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

Le prompting en chaîne de pensée demande explicitement au modèle de montrer son raisonnement, améliorant la précision pour les tâches complexes. La décomposition étape par étape aide à la compréhension aussi bien pour les humains que pour l’IA.

> **🤖 Essayez avec le chat [GitHub Copilot](https://github.com/features/copilot) :** Posez des questions sur ce schéma :
> - « Comment adapter le schéma d'exécution de tâches pour des opérations longues ? »
> - « Quelles sont les bonnes pratiques pour structurer les préambules d’outils dans les applications de production ? »
> - « Comment capturer et afficher les mises à jour de progrès intermédiaires dans une interface utilisateur ? »

Le diagramme ci-dessous illustre ce workflow Plan → Exécution → Résumé.

<img src="../../../translated_images/fr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Schéma d'exécution de tâches" width="800"/>

*Workflow Plan → Exécution → Résumé pour les tâches multi-étapes*

**Code auto-réfléchissant** - Pour générer du code de qualité production. Le modèle génère du code en suivant les standards de production avec une gestion appropriée des erreurs. Utilisez cela lors de la création de nouvelles fonctionnalités ou services.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

Le diagramme ci-dessous montre cette boucle d'amélioration itérative — générer, évaluer, identifier les faiblesses, et affiner jusqu'à ce que le code respecte les standards de production.

<img src="../../../translated_images/fr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cycle d'auto-réflexion" width="800"/>

*Boucle d'amélioration itérative - générer, évaluer, identifier les problèmes, améliorer, répéter*

**Analyse structurée** - Pour une évaluation cohérente. Le modèle révise le code avec un cadre fixe (exactitude, pratiques, performance, sécurité, maintenabilité). Utilisez cela pour des revues de code ou évaluations qualité.

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

> **🤖 Essayez avec le chat [GitHub Copilot](https://github.com/features/copilot) :** Posez des questions sur l’analyse structurée :
> - « Comment personnaliser le cadre d’analyse pour différents types de revues de code ? »
> - « Quelle est la meilleure façon de parser et d’agir sur une sortie structurée de manière programmatique ? »
> - « Comment assurer une cohérence des niveaux de gravité à travers différentes sessions de revue ? »

Le diagramme suivant montre comment ce cadre structuré organise une revue de code en catégories cohérentes avec des niveaux de gravité.

<img src="../../../translated_images/fr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Schéma d'analyse structurée" width="800"/>

*Cadre pour des revues de code cohérentes avec niveaux de gravité*

**Chat multi-tours** - Pour des conversations nécessitant du contexte. Le modèle se souvient des messages précédents et construit dessus. Utilisez cela pour des sessions d’aide interactives ou Q&A complexes.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

Le diagramme ci-dessous visualise comment le contexte de conversation s'accumule à chaque tour et comment il se rapporte à la limite de tokens du modèle.

<img src="../../../translated_images/fr/context-memory.dff30ad9fa78832a.webp" alt="Mémoire du contexte" width="800"/>

*Comment le contexte de la conversation s'accumule sur plusieurs tours jusqu'à atteindre la limite de tokens*
**Raisonnement étape par étape** - Pour les problèmes nécessitant une logique visible. Le modèle montre un raisonnement explicite pour chaque étape. Utilisez ceci pour les problèmes de mathématiques, les puzzles logiques ou lorsque vous devez comprendre le processus de réflexion.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Le diagramme ci-dessous illustre comment le modèle décompose les problèmes en étapes logiques explicites et numérotées.

<img src="../../../translated_images/fr/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Motif étape par étape" width="800"/>

*Décomposer les problèmes en étapes logiques explicites*

**Sortie contrainte** - Pour les réponses avec des exigences spécifiques de format. Le modèle suit strictement les règles de format et de longueur. Utilisez ceci pour les résumés ou lorsque vous avez besoin d’une structure de sortie précise.

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

Le diagramme suivant montre comment les contraintes guident le modèle pour produire une sortie qui respecte strictement vos exigences de format et de longueur.

<img src="../../../translated_images/fr/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Motif de sortie contrainte" width="800"/>

*Application spécifique des exigences de format, longueur et structure*

## Exécuter l'application

**Vérifier le déploiement :**

Assurez-vous que le fichier `.env` existe dans le répertoire racine avec les identifiants Azure (créés lors du module 01). Exécutez ceci depuis le répertoire du module (`02-prompt-engineering/`) :

**Bash :**
```bash
cat ../.env  # Devrait afficher AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell :**
```powershell
Get-Content ..\.env  # Devrait afficher AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Démarrer l'application :**

> **Note :** Si vous avez déjà démarré toutes les applications en utilisant `./start-all.sh` depuis le répertoire racine (comme décrit dans le module 01), ce module fonctionne déjà sur le port 8083. Vous pouvez ignorer les commandes de démarrage ci-dessous et aller directement à http://localhost:8083.

**Option 1 : Utiliser le Spring Boot Dashboard (recommandé pour les utilisateurs de VS Code)**

Le conteneur de développement inclut l’extension Spring Boot Dashboard, qui offre une interface visuelle pour gérer toutes les applications Spring Boot. Vous pouvez le trouver dans la barre d’activités à gauche dans VS Code (cherchez l’icône Spring Boot).

Depuis le Spring Boot Dashboard, vous pouvez :
- Voir toutes les applications Spring Boot disponibles dans l’espace de travail
- Démarrer/arrêter les applications en un clic
- Visualiser les logs des applications en temps réel
- Surveiller le statut des applications

Cliquez simplement sur le bouton lecture à côté de "prompt-engineering" pour démarrer ce module, ou démarrez tous les modules en une seule fois.

<img src="../../../translated_images/fr/dashboard.da2c2130c904aaf0.webp" alt="Tableau de bord Spring Boot" width="400"/>

*Le Spring Boot Dashboard dans VS Code — démarrer, arrêter et surveiller tous les modules depuis un seul endroit*

**Option 2 : Utiliser des scripts shell**

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

Ou démarrer uniquement ce module :

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

Les deux scripts chargent automatiquement les variables d’environnement depuis le fichier `.env` racine et construiront les JAR si ils n’existent pas.

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
./stop.sh  # Seulement ce module
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

Voici l’interface principale du module de prompt engineering, où vous pouvez expérimenter les huit motifs côte à côte.

<img src="../../../translated_images/fr/dashboard-home.5444dbda4bc1f79d.webp" alt="Accueil du tableau de bord" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Le tableau de bord principal montrant les 8 motifs de prompt engineering avec leurs caractéristiques et cas d’usage*

## Explorer les motifs

L’interface web vous permet d’expérimenter différentes stratégies de prompting. Chaque motif résout des problèmes différents — essayez-les pour voir quand chaque approche est pertinente.

> **Note : Streaming vs Non-Streaming** — Chaque page de motif offre deux boutons : **🔴 Diffuser la réponse (Live)** et une option **Non-streaming**. Le streaming utilise les Server-Sent Events (SSE) pour afficher les tokens en temps réel au fur et à mesure que le modèle les génère, vous voyez donc le progrès immédiatement. L’option non-streaming attend la réponse complète avant de l’afficher. Pour les prompts qui déclenchent un raisonnement approfondi (ex : Grande Implication, Code Auto-réflexif), l’appel non-streaming peut prendre très longtemps — parfois des minutes — sans retour visible. **Utilisez le streaming lors de l’expérimentation de prompts complexes** pour voir le modèle travailler et éviter l’impression que la requête a expiré.
>
> **Note : Navigateur requis** — La fonction de streaming utilise l’API Fetch Streams (`response.body.getReader()`) qui nécessite un navigateur complet (Chrome, Edge, Firefox, Safari). Elle ne fonctionne **pas** dans le Simple Browser intégré de VS Code, car sa webview ne supporte pas l’API ReadableStream. Si vous utilisez le Simple Browser, les boutons non-streaming fonctionneront toujours normalement — seuls les boutons de streaming sont impactés. Ouvrez `http://localhost:8083` dans un navigateur externe pour une expérience complète.

### Faible vs Grande Implication

Posez une question simple comme « Quel est 15 % de 200 ? » en mode Faible Implication. Vous obtenez une réponse instantanée et directe. Maintenant posez quelque chose de complexe comme « Concevez une stratégie de mise en cache pour une API à fort trafic » en mode Grande Implication. Cliquez sur **🔴 Diffuser la réponse (Live)** et regardez le raisonnement détaillé du modèle apparaître token après token. Même modèle, même structure de question — mais le prompt indique combien de réflexion faire.

### Exécution de tâches (Preambles Outils)

Les workflows en plusieurs étapes bénéficient d’une planification préalable et d’une narration des progrès. Le modèle décrit ce qu’il va faire, raconte chaque étape, puis résume les résultats.

### Code Auto-réflexif

Essayez « Crée un service de validation d’email ». Au lieu de simplement générer du code puis s’arrêter, le modèle génère, évalue selon des critères de qualité, identifie les faiblesses, puis améliore. Vous le voyez itérer jusqu’à ce que le code respecte les standards de production.

### Analyse structurée

Les revues de code nécessitent des cadres d’évaluation cohérents. Le modèle analyse le code avec des catégories fixes (correction, bonnes pratiques, performance, sécurité) avec des niveaux de gravité.

### Chat multi-tours

Demandez « Qu’est-ce que Spring Boot ? » puis enchaînez immédiatement avec « Montre-moi un exemple ». Le modèle se souvient de votre première question et vous donne un exemple précis de Spring Boot. Sans mémoire, cette deuxième question serait trop vague.

### Raisonnement étape par étape

Choisissez un problème mathématique et essayez-le à la fois avec Raisonnement étape par étape et Faible Implication. Faible implication vous donne juste la réponse — rapide mais opaque. Le raisonnement étape par étape vous montre chaque calcul et décision.

### Sortie contrainte

Quand vous avez besoin de formats spécifiques ou de nombres de mots précis, ce motif impose un respect strict. Essayez de générer un résumé avec exactement 100 mots au format liste à puces.

## Ce que vous apprenez vraiment

**L’effort de raisonnement change tout**

GPT-5.2 vous permet de contrôler l’effort de calcul via vos prompts. Un faible effort signifie des réponses rapides avec peu d’exploration. Un effort élevé signifie que le modèle prend le temps de réfléchir profondément. Vous apprenez à adapter l’effort à la complexité de la tâche — ne perdez pas de temps sur des questions simples, mais ne précipitez pas les décisions complexes non plus.

**La structure guide le comportement**

Vous remarquez les balises XML dans les prompts ? Elles ne sont pas décoratives. Les modèles suivent des instructions structurées plus fiablement que du texte libre. Quand vous avez besoin de processus multi-étapes ou de logique complexe, la structure aide le modèle à savoir où il en est et ce qui vient après. Le diagramme ci-dessous décompose un prompt bien structuré, montrant comment les balises comme `<system>`, `<instructions>`, `<context>`, `<user-input>` et `<constraints>` organisent vos instructions en sections claires.

<img src="../../../translated_images/fr/prompt-structure.a77763d63f4e2f89.webp" alt="Structure du prompt" width="800"/>

*Anatomie d’un prompt bien structuré avec des sections claires et organisation de type XML*

**Qualité par auto-évaluation**

Les motifs auto-réflexifs fonctionnent en rendant explicites les critères de qualité. Au lieu d’espérer que le modèle « fasse bien », vous lui dites exactement ce que signifie « bien » : logique correcte, gestion des erreurs, performance, sécurité. Le modèle peut alors évaluer sa propre sortie et s’améliorer. Cela transforme la génération de code d’une loterie en un processus.

**Le contexte est fini**

Les conversations multi-tours fonctionnent en incluant l’historique des messages à chaque requête. Mais il y a une limite — chaque modèle a un nombre maximal de tokens. À mesure que les conversations s’allongent, vous aurez besoin de stratégies pour garder le contexte pertinent sans dépasser ce plafond. Ce module vous montre comment fonctionne la mémoire ; plus tard vous apprendrez quand résumer, quand oublier, et quand récupérer.

## Prochaines étapes

**Module suivant :** [03-rag - RAG (Génération augmentée par recherche)](../03-rag/README.md)

---

**Navigation :** [← Précédent : Module 01 - Introduction](../01-introduction/README.md) | [Retour au début](../README.md) | [Suivant : Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avis de non-responsabilité** :  
Ce document a été traduit à l’aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforcions d’être précis, veuillez noter que les traductions automatiques peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue d’origine doit être considéré comme la source faisant foi. Pour toute information critique, une traduction professionnelle réalisée par un humain est recommandée. Nous déclinons toute responsabilité en cas de malentendus ou de mauvaises interprétations résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->