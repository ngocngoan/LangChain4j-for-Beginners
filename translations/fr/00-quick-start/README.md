# Module 00 : Démarrage Rapide

## Table des Matières

- [Introduction](../../../00-quick-start)
- [Qu'est-ce que LangChain4j ?](../../../00-quick-start)
- [Dépendances LangChain4j](../../../00-quick-start)
- [Prérequis](../../../00-quick-start)
- [Installation](../../../00-quick-start)
  - [1. Obtenez votre token GitHub](../../../00-quick-start)
  - [2. Configurez votre token](../../../00-quick-start)
- [Exécuter les exemples](../../../00-quick-start)
  - [1. Chat de base](../../../00-quick-start)
  - [2. Modèles de prompt](../../../00-quick-start)
  - [3. Appel de fonction](../../../00-quick-start)
  - [4. Q&R sur document (RAG)](../../../00-quick-start)
  - [5. IA responsable](../../../00-quick-start)
- [Ce que montre chaque exemple](../../../00-quick-start)
- [Étapes suivantes](../../../00-quick-start)
- [Dépannage](../../../00-quick-start)

## Introduction

Ce démarrage rapide est conçu pour vous permettre de commencer à utiliser LangChain4j le plus rapidement possible. Il couvre les bases absolues de la création d'applications d'IA avec LangChain4j et GitHub Models. Dans les modules suivants, vous utiliserez Azure OpenAI avec LangChain4j pour créer des applications plus avancées.

## Qu'est-ce que LangChain4j ?

LangChain4j est une bibliothèque Java qui simplifie la création d'applications alimentées par l'IA. Plutôt que de gérer des clients HTTP et le parsing JSON, vous travaillez avec des APIs Java claires.

La "chaîne" dans LangChain fait référence à l'enchaînement de plusieurs composants - vous pouvez chaîner un prompt à un modèle puis à un parseur, ou enchaîner plusieurs appels d'IA où une sortie sert d'entrée au suivant. Ce démarrage rapide se concentre sur les fondamentaux avant d'explorer des chaînes plus complexes.

<img src="../../../translated_images/fr/langchain-concept.ad1fe6cf063515e1.webp" alt="Concept d'enchaînement LangChain4j" width="800"/>

*Enchaînement des composants dans LangChain4j - les blocs de construction se connectent pour créer des workflows IA puissants*

Nous utiliserons trois composants principaux :

**ChatLanguageModel** - L'interface pour les interactions avec le modèle IA. Appelez `model.chat("prompt")` et obtenez une chaîne de réponse. Nous utilisons `OpenAiOfficialChatModel` qui fonctionne avec des points d'accès compatibles OpenAI comme GitHub Models.

**AiServices** - Crée des interfaces de service IA typées. Définissez des méthodes, catégorisez-les avec `@Tool`, et LangChain4j gère l'orchestration. L'IA appelle automatiquement vos méthodes Java quand c'est nécessaire.

**MessageWindowChatMemory** - Maintient l'historique de la conversation. Sans cela, chaque requête est indépendante. Avec, l'IA se souvient des messages précédents et garde le contexte à travers plusieurs tours.

<img src="../../../translated_images/fr/architecture.eedc993a1c576839.webp" alt="Architecture LangChain4j" width="800"/>

*Architecture LangChain4j - composants fondamentaux travaillant ensemble pour alimenter vos applications IA*

## Dépendances LangChain4j

Ce démarrage rapide utilise deux dépendances Maven dans le fichier [`pom.xml`](../../../00-quick-start/pom.xml) :

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

Le module `langchain4j-open-ai-official` fournit la classe `OpenAiOfficialChatModel` qui se connecte aux API compatibles OpenAI. GitHub Models utilise le même format d'API, donc aucun adaptateur spécial n'est nécessaire - il suffit de pointer l'URL de base vers `https://models.github.ai/inference`.

## Prérequis

**Utilisez-vous le conteneur de développement ?** Java et Maven sont déjà installés. Vous n'avez besoin que d'un token d'accès personnel GitHub.

**Développement local :**
- Java 21+, Maven 3.9+
- Token d'accès personnel GitHub (instructions ci-dessous)

> **Remarque :** Ce module utilise `gpt-4.1-nano` de GitHub Models. Ne modifiez pas le nom du modèle dans le code – il est configuré pour fonctionner avec les modèles disponibles de GitHub.

## Installation

### 1. Obtenez votre token GitHub

1. Allez sur [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Cliquez sur "Generate new token"
3. Donnez un nom descriptif (ex : "LangChain4j Démo")
4. Choisissez une expiration (7 jours recommandé)
5. Sous "Account permissions", trouvez "Models" et mettez en lecture seule
6. Cliquez sur "Generate token"
7. Copiez et sauvegardez votre token - vous ne le verrez plus après

### 2. Configurez votre token

**Option 1 : Utiliser VS Code (recommandé)**

Si vous utilisez VS Code, ajoutez votre token dans le fichier `.env` à la racine du projet :

Si le fichier `.env` n'existe pas, copiez `.env.example` en `.env` ou créez un nouveau fichier `.env` à la racine du projet.

**Exemple de fichier `.env` :**
```bash
# Dans /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Ensuite, vous pouvez simplement faire un clic droit sur n'importe quel fichier de démonstration (ex : `BasicChatDemo.java`) dans l'explorateur et choisir **"Run Java"** ou utiliser les configurations de lancement depuis le panneau Run and Debug.

**Option 2 : Utiliser le terminal**

Définissez le token comme variable d'environnement :

**Bash :**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell :**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Exécuter les exemples

**Avec VS Code :** Faites un clic droit sur un fichier de démonstration dans l'explorateur et sélectionnez **"Run Java"**, ou utilisez les configurations de lancement depuis le panneau Run and Debug (assurez-vous d'avoir ajouté votre token dans le fichier `.env` auparavant).

**Avec Maven :** Vous pouvez aussi exécuter depuis la ligne de commande :

### 1. Chat de base

**Bash :**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell :**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Modèles de prompt

**Bash :**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell :**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Montre le zero-shot, few-shot, chaîne de réflexion (chain-of-thought) et prompt basé sur rôle.

### 3. Appel de fonction

**Bash :**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell :**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

L'IA appelle automatiquement vos méthodes Java quand nécessaire.

### 4. Q&R sur document (RAG)

**Bash :**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell :**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Posez des questions sur le contenu de `document.txt`.

### 5. IA responsable

**Bash :**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell :**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Voyez comment les filtres de sécurité AI bloquent les contenus nuisibles.

## Ce que montre chaque exemple

**Chat de base** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Commencez ici pour voir LangChain4j dans sa forme la plus simple. Vous créez un `OpenAiOfficialChatModel`, envoyez un prompt avec `.chat()`, et recevez une réponse. Cela montre les bases : comment initialiser un modèle avec des points d'accès personnalisés et clés API. Une fois que vous comprenez ce schéma, tout le reste s'appuie dessus.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) et demandez :
> - "Comment passer de GitHub Models à Azure OpenAI dans ce code ?"
> - "Quels autres paramètres puis-je configurer dans OpenAiOfficialChatModel.builder() ?"
> - "Comment ajouter des réponses en streaming au lieu d'attendre la réponse complète ?"

**Ingénierie de prompts** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Maintenant que vous savez comment parler à un modèle, explorons ce que vous lui dites. Cette démo utilise le même modèle mais montre cinq modèles différents de prompt. Essayez les prompts zero-shot pour des instructions directes, few-shot qui apprennent d'exemples, chain-of-thought qui révèlent les étapes de raisonnement, et prompts basés sur rôle qui posent le contexte. Vous verrez comment un même modèle donne des résultats très différents selon la formulation de votre demande.

La démo montre aussi les templates de prompt, une manière puissante de créer des prompts réutilisables avec des variables.
L'exemple ci-dessous montre un prompt utilisant LangChain4j `PromptTemplate` pour remplir des variables. L'IA répondra en fonction de la destination et de l'activité fournies.

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

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) et demandez :
> - "Quelle est la différence entre zero-shot et few-shot, et quand utiliser chacun ?"
> - "Comment le paramètre température affecte-t-il les réponses du modèle ?"
> - "Quelles techniques existent pour éviter les attaques par injection de prompt en production ?"
> - "Comment créer des objets PromptTemplate réutilisables pour des modèles courants ?"

**Intégration d'outils** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

C'est ici que LangChain4j devient puissant. Vous utiliserez `AiServices` pour créer un assistant IA qui peut appeler vos méthodes Java. Il suffit d'annoter les méthodes avec `@Tool("description")` et LangChain4j s'occupe du reste - l'IA décide automatiquement quand utiliser chaque outil en fonction de la demande utilisateur. Cela montre l'appel de fonction, une technique clé pour construire des IA capables d'agir, pas seulement de répondre.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) et demandez :
> - "Comment fonctionne l'annotation @Tool et que fait LangChain4j derrière ?"
> - "L'IA peut-elle appeler plusieurs outils en séquence pour résoudre des problèmes complexes ?"
> - "Que se passe-t-il si un outil lance une exception - comment gérer les erreurs ?"
> - "Comment intégrer une vraie API à la place de cet exemple de calculatrice ?"

**Q&R sur document (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Ici vous verrez la base du RAG (retrieval-augmented generation). Plutôt que de s’appuyer sur les données d’entraînement du modèle, vous chargez le contenu de [`document.txt`](../../../00-quick-start/document.txt) et l’incluez dans le prompt. L’IA répond en fonction de votre document, pas de ses connaissances générales. C’est la première étape pour construire des systèmes qui travaillent avec vos propres données.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Remarque :** Cette méthode simple charge tout le document dans le prompt. Pour les gros fichiers (>10KB), vous dépasserez les limites de contexte. Le Module 03 traite du découpage et de la recherche vectorielle pour des systèmes RAG en production.

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) et demandez :
> - "Comment le RAG empêche-t-il les hallucinations IA comparé aux données d'entraînement du modèle ?"
> - "Quelle est la différence entre cette approche simple et l’utilisation d’embeddings vectoriels pour la recherche ?"
> - "Comment passer à une échelle multi-documents ou à de plus grandes bases de connaissances ?"
> - "Quelles sont les meilleures pratiques pour structurer le prompt et s’assurer que l’IA utilise uniquement le contexte fourni ?"

**IA responsable** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Construisez la sécurité IA avec une défense en profondeur. Cette démo montre deux couches de protection fonctionnant ensemble :

**Partie 1 : LangChain4j Input Guardrails** - Bloquent les prompts dangereux avant qu’ils n’atteignent le LLM. Créez des garde-fous personnalisés qui vérifient les mots-clés ou motifs interdits. Ceux-ci sont exécutés dans votre code, donc rapides et gratuits.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**Partie 2 : Filtres de sécurité du fournisseur** - GitHub Models intègre des filtres qui attrapent ce que vos garde-fous pourraient manquer. Vous verrez des blocages durs (erreurs HTTP 400) pour violations graves et des refus doux où l’IA décline poliment.

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) et demandez :
> - "Qu’est-ce que InputGuardrail et comment créer le mien ?"
> - "Quelle différence entre un blocage dur et un refus doux ?"
> - "Pourquoi utiliser garde-fous et filtres fournisseur ensemble ?"

## Étapes suivantes

**Module suivant :** [01-introduction - Premiers pas avec LangChain4j et gpt-5 sur Azure](../01-introduction/README.md)

---

**Navigation :** [← Retour au principal](../README.md) | [Suivant : Module 01 - Introduction →](../01-introduction/README.md)

---

## Dépannage

### Première compilation Maven

**Problème** : `mvn clean compile` ou `mvn package` initial prend beaucoup de temps (10-15 minutes)

**Cause :** Maven doit télécharger toutes les dépendances du projet (Spring Boot, bibliothèques LangChain4j, SDK Azure, etc.) lors de la première compilation.

**Solution :** Ce comportement est normal. Les compilations suivantes seront bien plus rapides car les dépendances sont mises en cache localement. Le temps de téléchargement dépend de votre connexion réseau.
### Syntaxe de la commande Maven sous PowerShell

**Problème** : Les commandes Maven échouent avec l’erreur `Unknown lifecycle phase ".mainClass=..."`

**Cause** : PowerShell interprète `=` comme un opérateur d’affectation de variable, ce qui casse la syntaxe des propriétés Maven

**Solution** : Utilisez l’opérateur d’arrêt d’analyse `--%` avant la commande Maven :

**PowerShell :**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash :**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

L’opérateur `--%` indique à PowerShell de passer tous les arguments restants littéralement à Maven sans les interpréter.

### Affichage des emojis sous Windows PowerShell

**Problème** : Les réponses de l’IA affichent des caractères indésirables (par exemple, `????` ou `â??`) au lieu des emojis dans PowerShell

**Cause** : L’encodage par défaut de PowerShell ne prend pas en charge les emojis UTF-8

**Solution** : Exécutez cette commande avant de lancer des applications Java :
```cmd
chcp 65001
```

Cela force l’encodage UTF-8 dans le terminal. Vous pouvez aussi utiliser Windows Terminal qui offre un meilleur support Unicode.

### Débogage des appels API

**Problème** : Erreurs d’authentification, limites de taux, ou réponses inattendues du modèle IA

**Solution** : Les exemples incluent `.logRequests(true)` et `.logResponses(true)` pour afficher les appels API dans la console. Cela permet de diagnostiquer les erreurs d’authentification, les limites de taux ou les réponses inattendues. Supprimez ces options en production pour réduire le bruit dans les logs.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :  
Ce document a été traduit à l’aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforçons d’assurer l’exactitude, veuillez noter que les traductions automatiques peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue d’origine doit être considéré comme la source faisant foi. Pour les informations critiques, il est recommandé de faire appel à une traduction professionnelle réalisée par un humain. Nous ne saurions être tenus responsables des malentendus ou des interprétations erronées résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->