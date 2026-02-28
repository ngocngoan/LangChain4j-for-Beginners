# Module 02 : Ingénierie des Prompts avec GPT-5.2

## Table des Matières

- [Présentation Vidéo](../../../02-prompt-engineering)
- [Ce que Vous Apprendrez](../../../02-prompt-engineering)
- [Prérequis](../../../02-prompt-engineering)
- [Comprendre l'Ingénierie des Prompts](../../../02-prompt-engineering)
- [Fondamentaux de l'Ingénierie des Prompts](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chaîne de Raisonnement](../../../02-prompt-engineering)
  - [Prompt Basé sur un Rôle](../../../02-prompt-engineering)
  - [Modèles de Prompts](../../../02-prompt-engineering)
- [Schémas Avancés](../../../02-prompt-engineering)
- [Utilisation des Ressources Azure Existantes](../../../02-prompt-engineering)
- [Captures d’Écran de l'Application](../../../02-prompt-engineering)
- [Exploration des Schémas](../../../02-prompt-engineering)
  - [Faible vs Forte Implication](../../../02-prompt-engineering)
  - [Exécution de Tâches (Préambules d'Outils)](../../../02-prompt-engineering)
  - [Code Auto-réfléchissant](../../../02-prompt-engineering)
  - [Analyse Structurée](../../../02-prompt-engineering)
  - [Conversation Multi-Tours](../../../02-prompt-engineering)
  - [Raisonnement Pas à Pas](../../../02-prompt-engineering)
  - [Sortie Contraintes](../../../02-prompt-engineering)
- [Ce que Vous Apprenez Vraiment](../../../02-prompt-engineering)
- [Prochaines Étapes](../../../02-prompt-engineering)

## Présentation Vidéo

Regardez cette session en direct qui explique comment débuter avec ce module : [Ingénierie des Prompts avec LangChain4j - Session en Direct](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## Ce que Vous Apprendrez

<img src="../../../translated_images/fr/what-youll-learn.c68269ac048503b2.webp" alt="Ce que Vous Apprendrez" width="800"/>

Dans le module précédent, vous avez vu comment la mémoire permet l’IA conversationnelle et utilisé les modèles GitHub pour des interactions basiques. Nous allons maintenant nous concentrer sur la façon dont vous posez des questions — les prompts eux-mêmes — en utilisant GPT-5.2 d’Azure OpenAI. La manière dont vous structurez vos prompts influence considérablement la qualité des réponses. Nous commençons par revoir les techniques fondamentales de prompt, puis passons à huit schémas avancés qui tirent pleinement parti des capacités de GPT-5.2.

Nous utilisons GPT-5.2 car il introduit un contrôle du raisonnement — vous pouvez indiquer au modèle la quantité de réflexion avant de répondre. Cela rend les différentes stratégies de prompting plus apparentes et vous aide à comprendre quand utiliser chaque approche. Nous profiterons également des limites de taux moindres d’Azure sur GPT-5.2 comparé aux modèles GitHub.

## Prérequis

- Module 01 complété (ressources Azure OpenAI déployées)  
- Fichier `.env` dans le répertoire racine avec les identifiants Azure (créé par `azd up` dans le Module 01)  

> **Note :** Si vous n’avez pas encore terminé le Module 01, suivez d’abord les instructions de déploiement là-bas.

## Comprendre l'Ingénierie des Prompts

<img src="../../../translated_images/fr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Qu’est-ce que l’Ingénierie des Prompts ?" width="800"/>

L’ingénierie des prompts consiste à concevoir un texte d’entrée qui vous donne de manière cohérente les résultats dont vous avez besoin. Il ne s’agit pas seulement de poser des questions — c’est structurer les requêtes pour que le modèle comprenne exactement ce que vous voulez et comment le fournir.

Pensez-y comme donner des instructions à un collègue. « Corrige le bug » est vague. « Corrige l’exception de pointeur nul dans UserService.java ligne 45 en ajoutant une vérification nulle » est précis. Les modèles linguistiques fonctionnent de la même manière — la spécificité et la structure comptent.

<img src="../../../translated_images/fr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Comment LangChain4j s’intègre" width="800"/>

LangChain4j fournit l’infrastructure — connexions au modèle, mémoire, types de messages — tandis que les schémas de prompt sont simplement des textes soigneusement structurés que vous envoyez via cette infrastructure. Les blocs de construction clés sont `SystemMessage` (qui définit le comportement et le rôle de l’IA) et `UserMessage` (qui porte votre requête réelle).

## Fondamentaux de l'Ingénierie des Prompts

<img src="../../../translated_images/fr/five-patterns-overview.160f35045ffd2a94.webp" alt="Aperçu des Cinq Schémas d’Ingénierie des Prompts" width="800"/>

Avant de plonger dans les schémas avancés de ce module, passons en revue cinq techniques fondamentales de prompting. Ce sont les bases que tout ingénieur de prompt devrait connaître. Si vous avez déjà travaillé avec le [module de démarrage rapide](../00-quick-start/README.md#2-prompt-patterns), vous les avez vues en action — voici le cadre conceptuel qui les sous-tend.

### Zero-Shot Prompting

L’approche la plus simple : donnez une instruction directe au modèle sans exemple. Le modèle s’appuie entièrement sur son entraînement pour comprendre et exécuter la tâche. Cela fonctionne bien pour des requêtes simples dont le comportement attendu est évident.

<img src="../../../translated_images/fr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Instruction directe sans exemples — le modèle déduit la tâche à partir de l’instruction seule*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Réponse : « Positif »
```
  
**Quand l’utiliser :** Classifications simples, questions directes, traductions, ou toute tâche que le modèle peut gérer sans guide supplémentaire.

### Few-Shot Prompting

Fournissez des exemples qui démontrent le schéma que vous voulez que le modèle suive. Le modèle apprend le format d’entrée-sortie attendu à partir de vos exemples et l’applique aux nouvelles entrées. Cela améliore grandement la cohérence pour les tâches où le format ou le comportement attendu n’est pas évident.

<img src="../../../translated_images/fr/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Apprentissage depuis des exemples — le modèle identifie le schéma et l’applique à de nouvelles entrées*

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
  
**Quand l’utiliser :** Classifications personnalisées, formatage cohérent, tâches spécifiques au domaine ou lorsque les résultats zero-shot sont inconsistants.

### Chaîne de Raisonnement

Demandez au modèle de montrer son raisonnement pas à pas. Au lieu de passer directement à la réponse, le modèle décompose le problème et travaille explicitement chaque partie. Cela améliore la précision sur les tâches de mathématiques, logique et raisonnement en plusieurs étapes.

<img src="../../../translated_images/fr/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Raisonnement pas à pas — décomposer les problèmes complexes en étapes logiques explicites*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Le modèle montre : 15 - 8 = 7, puis 7 + 12 = 19 pommes
```
  
**Quand l’utiliser :** Problèmes mathématiques, énigmes logiques, débogage ou toute tâche où montrer le processus de raisonnement améliore la précision et la confiance.

### Prompt Basé sur un Rôle

Attribuez une persona ou un rôle à l’IA avant de poser votre question. Cela fournit un contexte qui façonne le ton, la profondeur et le focus de la réponse. Un « architecte logiciel » donne des conseils différents d’un « développeur junior » ou d’un « auditeur sécurité ».

<img src="../../../translated_images/fr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Définir le contexte et la persona — la même question obtient une réponse différente selon le rôle assigné*

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
  
**Quand l’utiliser :** Revues de code, tutorat, analyses spécifiques au domaine ou lorsque vous avez besoin de réponses adaptées à un niveau d’expertise ou perspective particulière.

### Modèles de Prompts

Créez des prompts réutilisables avec des espaces réservés variables. Au lieu d’écrire un nouveau prompt à chaque fois, définissez un modèle une fois et remplissez différentes valeurs. La classe `PromptTemplate` de LangChain4j facilite cela avec la syntaxe `{{variable}}`.

<img src="../../../translated_images/fr/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

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
  
**Quand l’utiliser :** Requêtes répétées avec différentes entrées, traitement par lots, construction de flux de travail IA réutilisables ou tout scénario où la structure du prompt reste la même mais les données changent.

---

Ces cinq fondamentaux vous donnent une boîte à outils solide pour la plupart des tâches de prompting. Le reste de ce module s’appuie dessus avec **huit schémas avancés** qui exploitent le contrôle du raisonnement, l’auto-évaluation et les capacités de sortie structurée de GPT-5.2.

## Schémas Avancés

Avec les fondamentaux en place, passons aux huit schémas avancés qui rendent ce module unique. Tous les problèmes ne nécessitent pas la même approche. Certaines questions demandent des réponses rapides, d’autres une réflexion approfondie. Certaines doivent montrer le raisonnement, d’autres ont juste besoin du résultat. Chaque schéma ci-dessous est optimisé pour un scénario différent — et le contrôle du raisonnement de GPT-5.2 rend ces différences encore plus marquées.

<img src="../../../translated_images/fr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Huit Schémas de Prompting" width="800"/>

*Aperçu des huit schémas d’ingénierie des prompts et leurs cas d’usage*

<img src="../../../translated_images/fr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Contrôle du Raisonnement avec GPT-5.2" width="800"/>

*Le contrôle de raisonnement de GPT-5.2 vous permet de spécifier la quantité de réflexion effectuée par le modèle — des réponses rapides directes à une exploration approfondie*

**Faible Implication (Rapide & Ciblé)** - Pour les questions simples où vous voulez des réponses rapides et directes. Le modèle effectue un raisonnement minimal — maximum 2 étapes. Utilisez ceci pour des calculs, recherches ou questions simples.

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
> - « Quelle est la différence entre les schémas de prompting à faible et haute implication ? »  
> - « Comment les balises XML dans les prompts aident-elles à structurer la réponse de l’IA ? »  
> - « Quand devrais-je utiliser les schémas d’autoréflexion vs l’instruction directe ? »

**Haute Implication (Profond & Approfondi)** - Pour les problèmes complexes où vous voulez une analyse complète. Le modèle explore en profondeur et montre un raisonnement détaillé. Utilisez ce mode pour la conception système, décisions d’architecture ou recherches complexes.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**Exécution de Tâches (Progression Pas à Pas)** - Pour les workflows multi-étapes. Le modèle fournit un plan initial, narre chaque étape pendant qu’il travaille, puis donne un résumé. Utilisez ceci pour les migrations, implémentations, ou tout processus multi-étapes.

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
  
Le prompting en chaîne de raisonnement demande explicitement au modèle de montrer son processus de réflexion, améliorant la précision pour les tâches complexes. La décomposition pas à pas aide humains et IA à comprendre la logique.

> **🤖 Essayez avec le Chat [GitHub Copilot](https://github.com/features/copilot) :** Posez des questions sur ce schéma :  
> - « Comment adapterais-je le schéma d’exécution de tâches pour des opérations longues ? »  
> - « Quelles sont les bonnes pratiques pour structurer les préambules d’outils dans les applications en production ? »  
> - « Comment capturer et afficher des mises à jour de progrès intermédiaires dans une interface utilisateur ? »

<img src="../../../translated_images/fr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Schéma d’Exécution de Tâche" width="800"/>

*Flux Planifier → Exécuter → Résumer pour des tâches multi-étapes*

**Code Auto-réfléchissant** - Pour générer du code de qualité production. Le modèle génère du code respectant les normes de production avec une gestion appropriée des erreurs. Utilisez ce schéma pour créer de nouvelles fonctionnalités ou services.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/fr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cycle d’Auto-Réflexion" width="800"/>

*Boucle d’amélioration itérative — générer, évaluer, identifier les problèmes, améliorer, répéter*

**Analyse Structurée** - Pour évaluation cohérente. Le modèle passe en revue le code selon un cadre fixe (correction, bonnes pratiques, performances, sécurité, maintenabilité). Utilisez ce schéma pour des revues de code ou évaluations qualité.

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
  
> **🤖 Essayez avec le Chat [GitHub Copilot](https://github.com/features/copilot) :** Posez des questions sur l’analyse structurée :  
> - « Comment personnaliser le cadre d’analyse pour différents types de revues de code ? »  
> - « Quelle est la meilleure méthode pour analyser et agir sur une sortie structurée de façon programmatique ? »  
> - « Comment garantir la cohérence des niveaux de gravité entre différentes sessions de revue ? »

<img src="../../../translated_images/fr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Schéma d’Analyse Structurée" width="800"/>

*Cadre pour des revues de code cohérentes avec niveaux de gravité*

**Conversation Multi-Tours** - Pour des échanges nécessitant du contexte. Le modèle se souvient des messages précédents et construit sur ces derniers. Utilisez cela pour des sessions d’aide interactives ou Q&A complexes.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
<img src="../../../translated_images/fr/context-memory.dff30ad9fa78832a.webp" alt="Mémoire de Contexte" width="800"/>

*Comment le contexte de conversation s’accumule sur plusieurs tours jusqu’à atteindre la limite de tokens*

**Raisonnement Pas à Pas** - Pour les problèmes nécessitant une logique visible. Le modèle montre un raisonnement explicite à chaque étape. Utilisez ce schéma pour des problèmes mathématiques, énigmes logiques, ou lorsque vous avez besoin de comprendre le processus de pensée.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/fr/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Schéma Pas à Pas" width="800"/>

*Décomposition des problèmes en étapes logiques explicites*

**Sortie Contraintes** - Pour des réponses avec exigences de format spécifiques. Le modèle suit rigoureusement les règles de format et de longueur. Utilisez cela pour des résumés ou lorsque vous avez besoin d’une structure de sortie précise.

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
  
<img src="../../../translated_images/fr/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Schéma de Sortie Contraintes" width="800"/>

*Application stricte de règles de format, longueur et structure*

## Utilisation des Ressources Azure Existantes

**Vérifier le déploiement :**

Assurez-vous que le fichier `.env` existe dans le répertoire racine avec les identifiants Azure (créé pendant le Module 01) :  
```bash
cat ../.env  # Devrait afficher AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Démarrer l’application :**

> **Note :** Si vous avez déjà démarré toutes les applications avec `./start-all.sh` depuis le Module 01, ce module tourne déjà sur le port 8083. Vous pouvez alors ignorer les commandes de démarrage ci-dessous et aller directement sur http://localhost:8083.

**Option 1 : Utilisation du Spring Boot Dashboard (recommandé pour les utilisateurs VS Code)**
Le conteneur de développement inclut l'extension Spring Boot Dashboard, qui fournit une interface visuelle pour gérer toutes les applications Spring Boot. Vous pouvez la trouver dans la barre d’activités sur le côté gauche de VS Code (cherchez l'icône Spring Boot).

Depuis le Spring Boot Dashboard, vous pouvez :
- Voir toutes les applications Spring Boot disponibles dans l’espace de travail
- Démarrer/arrêter les applications en un seul clic
- Visualiser les journaux des applications en temps réel
- Surveiller l’état des applications

Il vous suffit de cliquer sur le bouton lecture à côté de « prompt-engineering » pour démarrer ce module, ou de lancer tous les modules en même temps.

<img src="../../../translated_images/fr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

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

Ou démarrer juste ce module :

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

> **Note :** Si vous préférez compiler tous les modules manuellement avant de lancer :
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

*Le tableau de bord principal affichant les 8 modèles d’ingénierie de prompt avec leurs caractéristiques et cas d’usage*

## Explorer les modèles

L’interface web vous permet d’expérimenter différentes stratégies de prompt. Chaque modèle résout des problèmes différents – essayez-les pour voir dans quels cas chaque approche brille.

> **Note : Streaming vs Non-Streaming** — Chaque page de modèle propose deux boutons : **🔴 Stream Response (Live)** et une option **Non-streaming**. Le streaming utilise les Server-Sent Events (SSE) pour afficher les jetons en temps réel pendant que le modèle les génère, vous permettant de voir la progression immédiatement. L’option non-streaming attend la réponse complète avant de l’afficher. Pour des prompts qui déclenchent un raisonnement profond (par exemple, High Eagerness, Self-Reflecting Code), l’appel non-streaming peut prendre très longtemps – parfois des minutes – sans aucun retour visible. **Utilisez le streaming lors de l’expérimentation avec des prompts complexes** pour voir le modèle en action et éviter de penser que la requête a expiré.
>
> **Note : Exigence du navigateur** — La fonctionnalité de streaming utilise l’API Fetch Streams (`response.body.getReader()`) qui requiert un navigateur complet (Chrome, Edge, Firefox, Safari). Elle ne fonctionne **pas** dans le navigateur Simple Browser intégré à VS Code, car sa webview ne supporte pas l’API ReadableStream. Si vous utilisez le Simple Browser, les boutons non-streaming fonctionneront normalement – seuls les boutons de streaming sont affectés. Ouvrez `http://localhost:8083` dans un navigateur externe pour une expérience complète.

### Faible vs Forte énergie (Eagerness)

Posez une question simple comme « Quelle est 15 % de 200 ? » avec Faible énergie. Vous obtiendrez une réponse directe et instantanée. Maintenant posez une question complexe comme « Concevez une stratégie de mise en cache pour une API à fort trafic » avec Forte énergie. Cliquez sur **🔴 Stream Response (Live)** et regardez le raisonnement détaillé du modèle apparaître jeton par jeton. Même modèle, même structure de question – mais le prompt lui dit combien de réflexion appliquer.

### Exécution de tâches (Préambules d’outils)

Les workflows en plusieurs étapes bénéficient d’une planification préalable et d’une narration des progrès. Le modèle décrit ce qu’il va faire, narre chaque étape, puis résume les résultats.

### Code à auto-réflexion

Essayez « Créez un service de validation d’email ». Au lieu de simplement générer du code et s’arrêter, le modèle génère, évalue selon des critères de qualité, identifie des faiblesses, et améliore. Vous le verrez itérer jusqu’à ce que le code réponde aux standards de production.

### Analyse structurée

Les revues de code nécessitent des cadres d’évaluation cohérents. Le modèle analyse le code selon des catégories fixes (justesse, pratiques, performance, sécurité) avec des niveaux de gravité.

### Chat multi-tour

Demandez « Qu’est-ce que Spring Boot ? » puis enchaînez immédiatement avec « Montre-moi un exemple ». Le modèle se rappelle de votre première question et vous donne un exemple Spring Boot spécifique. Sans mémoire, cette deuxième question serait trop vague.

### Raisonnement pas à pas

Choisissez un problème mathématique et essayez-le avec Raisonnement pas à pas et Faible énergie. Faible énergie vous donne juste la réponse – rapide mais opaque. Le raisonnement pas à pas vous montre chaque calcul et décision.

### Sortie contrainte

Lorsque vous avez besoin de formats ou de compte de mots spécifiques, ce modèle impose un strict respect. Essayez de générer un résumé avec exactement 100 mots sous forme de points.

## Ce que vous apprenez vraiment

**L’effort de raisonnement change tout**

GPT-5.2 vous permet de contrôler l’effort computationnel via vos prompts. Un faible effort signifie des réponses rapides avec une exploration minimale. Un effort élevé signifie que le modèle prend du temps pour réfléchir en profondeur. Vous apprenez à adapter l’effort à la complexité de la tâche – ne perdez pas de temps sur des questions simples, mais ne précipitez pas les décisions complexes non plus.

**La structure guide le comportement**

Vous remarquez les balises XML dans les prompts ? Elles ne sont pas décoratives. Les modèles suivent des instructions structurées plus fiablement que du texte libre. Quand vous avez besoin de processus multi-étapes ou de logique complexe, la structure aide le modèle à suivre où il en est et ce qui vient ensuite.

<img src="../../../translated_images/fr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomie d’un prompt bien structuré avec des sections claires et une organisation de style XML*

**Qualité par auto-évaluation**

Les modèles auto-réfléchissants fonctionnent en rendant explicites les critères de qualité. Au lieu d’espérer que le modèle « fasse bien », vous lui dites exactement ce que « bien » signifie : logique correcte, gestion des erreurs, performance, sécurité. Le modèle peut alors évaluer sa propre sortie et s’améliorer. Cela transforme la génération de code en un processus au lieu d’une loterie.

**Le contexte est fini**

Les conversations multi-tours fonctionnent en incluant l’historique des messages à chaque requête. Mais il y a une limite – chaque modèle a un nombre maximal de jetons. À mesure que les conversations grandissent, vous aurez besoin de stratégies pour garder le contexte pertinent sans atteindre ce plafond. Ce module vous montre comment la mémoire fonctionne ; plus tard vous apprendrez quand résumer, quand oublier, et quand récupérer.

## Prochaines étapes

**Module suivant :** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation :** [← Précédent : Module 01 - Introduction](../01-introduction/README.md) | [Retour au principal](../README.md) | [Suivant : Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :  
Ce document a été traduit à l’aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforcions d’assurer la précision, veuillez noter que les traductions automatiques peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue d’origine doit être considéré comme la source officielle. Pour les informations critiques, une traduction professionnelle humaine est recommandée. Nous déclinons toute responsabilité en cas de malentendus ou de mauvaises interprétations résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->