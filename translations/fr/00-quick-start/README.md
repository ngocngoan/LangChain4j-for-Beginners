# Module 00 : Démarrage rapide

## Table des matières

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
  - [3. Appel de fonctions](../../../00-quick-start)
  - [4. Questions/Réponses sur documents (Easy RAG)](../../../00-quick-start)
  - [5. IA responsable](../../../00-quick-start)
- [Ce que chaque exemple montre](../../../00-quick-start)
- [Étapes suivantes](../../../00-quick-start)
- [Dépannage](../../../00-quick-start)

## Introduction

Ce démarrage rapide vise à vous mettre en route avec LangChain4j le plus rapidement possible. Il couvre les bases absolues de la création d’applications d’IA avec LangChain4j et GitHub Models. Dans les modules suivants, vous passerez à Azure OpenAI et GPT-5.2 et approfondirez chaque concept.

## Qu'est-ce que LangChain4j ?

LangChain4j est une bibliothèque Java qui simplifie la création d’applications alimentées par l’IA. Au lieu de gérer des clients HTTP et le parsing JSON, vous travaillez avec des API Java propres.

Le terme « chaîne » dans LangChain fait référence au chaînage de plusieurs composants – vous pouvez enchaîner un prompt à un modèle puis à un parseur, ou enchaîner plusieurs appels IA où une sortie sert d’entrée au suivant. Ce démarrage rapide se concentre sur les fondamentaux avant d’explorer des chaînes plus complexes.

<img src="../../../translated_images/fr/langchain-concept.ad1fe6cf063515e1.webp" alt="Concept de chaînage LangChain4j" width="800"/>

*Chaînage des composants dans LangChain4j – les blocs de construction se connectent pour créer des workflows IA puissants*

Nous utiliserons trois composants principaux :

**ChatModel** – Interface pour les interactions avec les modèles IA. Appelez `model.chat("prompt")` et obtenez une chaîne de réponse. Nous utilisons `OpenAiOfficialChatModel` qui fonctionne avec les points d'entrée compatibles OpenAI comme GitHub Models.

**AiServices** – Crée des interfaces de service IA typées. Définissez des méthodes, annotées avec `@Tool`, et LangChain4j gère l’orchestration. L’IA appelle automatiquement vos méthodes Java quand nécessaire.

**MessageWindowChatMemory** – Maintient l’historique de conversation. Sans cela, chaque requête est indépendante. Avec, l’IA se souvient des messages précédents et maintient le contexte sur plusieurs tours.

<img src="../../../translated_images/fr/architecture.eedc993a1c576839.webp" alt="Architecture LangChain4j" width="800"/>

*Architecture LangChain4j – composants principaux travaillant ensemble pour alimenter vos applications IA*

## Dépendances LangChain4j

Ce démarrage rapide utilise trois dépendances Maven dans le [`pom.xml`](../../../00-quick-start/pom.xml) :

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

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

Le module `langchain4j-open-ai-official` fournit la classe `OpenAiOfficialChatModel` qui se connecte aux API compatibles OpenAI. GitHub Models utilise le même format d’API, donc aucun adaptateur spécial n’est nécessaire – il suffit de pointer l’URL de base vers `https://models.github.ai/inference`.

Le module `langchain4j-easy-rag` fournit la découpe automatique des documents, l’intégration et la recherche, vous permettant de construire des applications RAG sans configurer manuellement chaque étape.

## Prérequis

**Utilisez-vous le conteneur de développement ?** Java et Maven sont déjà installés. Vous avez seulement besoin d’un token d'accès personnel GitHub.

**Développement local :**
- Java 21+, Maven 3.9+
- Token d'accès personnel GitHub (instructions ci-dessous)

> **Note :** Ce module utilise `gpt-4.1-nano` de GitHub Models. Ne modifiez pas le nom du modèle dans le code – il est configuré pour fonctionner avec les modèles disponibles chez GitHub.

## Installation

### 1. Obtenez votre token GitHub

1. Allez sur [Paramètres GitHub → Tokens d’accès personnels](https://github.com/settings/personal-access-tokens)
2. Cliquez sur "Générer un nouveau token"
3. Donnez un nom descriptif (ex. « LangChain4j Démo »)
4. Choisissez une expiration (7 jours recommandé)
5. Sous « Permissions du compte », trouvez « Models » et mettez « Lecture seule »
6. Cliquez sur « Générer le token »
7. Copiez et enregistrez votre token – vous ne le reverrez pas

### 2. Configurez votre token

**Option 1 : Utiliser VS Code (recommandé)**

Si vous utilisez VS Code, ajoutez votre token dans le fichier `.env` à la racine du projet :

Si le fichier `.env` n’existe pas, copiez `.env.example` en `.env` ou créez un nouveau fichier `.env` à la racine du projet.

**Exemple de fichier `.env` :**
```bash
# Dans /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Ensuite, vous pouvez simplement cliquer-droit sur n’importe quel fichier demo (ex. `BasicChatDemo.java`) dans l’explorateur et sélectionner **"Run Java"** ou utiliser les configurations de lancement dans le panneau Run and Debug.

**Option 2 : Utiliser le terminal**

Définissez le token comme variable d’environnement :

**Bash :**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell :**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Exécuter les exemples

**Avec VS Code :** Cliquez simplement-droit sur un fichier demo dans l’explorateur et sélectionnez **"Run Java"**, ou utilisez les configurations de lancement dans le panneau Run and Debug (en vous assurant d’avoir ajouté votre token dans le fichier `.env` au préalable).

**Avec Maven :** Sinon, lancez depuis la ligne de commande :

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

Montre le zero-shot, few-shot, chain-of-thought et les prompts basés sur les rôles.

### 3. Appel de fonctions

**Bash :**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell :**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

L’IA appelle automatiquement vos méthodes Java lorsque nécessaire.

### 4. Questions/Réponses sur documents (Easy RAG)

**Bash :**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell :**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Posez des questions sur vos documents utilisant Easy RAG avec intégration et recherche automatiques.

### 5. IA responsable

**Bash :**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell :**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Voyez comment les filtres de sécurité IA bloquent les contenus nuisibles.

## Ce que chaque exemple montre

**Chat de base** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Commencez ici pour voir LangChain4j dans sa forme la plus simple. Vous créerez un `OpenAiOfficialChatModel`, enverrez un prompt avec `.chat()`, et obtiendrez une réponse. Cela montre les bases : comment initialiser les modèles avec des points d’entrée et clés API personnalisés. Une fois ce schéma compris, tout le reste s’appuie dessus.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) et demandez :
> - « Comment passer de GitHub Models à Azure OpenAI dans ce code ? »
> - « Quels autres paramètres puis-je configurer dans OpenAiOfficialChatModel.builder() ? »
> - « Comment ajouter des réponses en streaming au lieu d’attendre la réponse complète ? »

**Ingénierie des prompts** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Maintenant que vous savez comment parler à un modèle, explorons ce que vous lui dites. Cette démo utilise la même configuration de modèle mais montre cinq modèles de prompt différents. Essayez les prompts zero-shot pour des instructions directes, few-shot qui apprennent à partir d’exemples, chain-of-thought qui révèlent les étapes de raisonnement, et les prompts basés sur les rôles qui définissent le contexte. Vous verrez comment le même modèle donne des résultats très différents selon la formulation de la demande.

La démo montre aussi des templates de prompt, une façon puissante de créer des prompts réutilisables avec des variables.
L’exemple ci-dessous montre un prompt utilisant `PromptTemplate` LangChain4j pour remplir des variables. L’IA répondra en fonction de la destination et de l’activité indiquées.

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
> - « Quelle est la différence entre les prompts zero-shot et few-shot, et quand utiliser chacun ? »
> - « Comment le paramètre temperature influence-t-il les réponses du modèle ? »
> - « Quelles sont les techniques pour prévenir les attaques d’injection de prompt en production ? »
> - « Comment créer des objets PromptTemplate réutilisables pour des modèles courants ? »

**Intégration d’outils** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

C’est ici que LangChain4j devient puissant. Vous utiliserez `AiServices` pour créer un assistant IA qui peut appeler vos méthodes Java. Il suffit d’annoter les méthodes avec `@Tool("description")` et LangChain4j gère le reste – l’IA décide automatiquement quand utiliser chaque outil selon la demande de l’utilisateur. Cela illustre l’appel de fonction, une technique clé pour construire une IA capable d’agir, pas seulement répondre.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) et demandez :
> - « Comment fonctionne l’annotation @Tool et que fait LangChain4j en coulisses ? »
> - « L’IA peut-elle appeler plusieurs outils en séquence pour résoudre des problèmes complexes ? »
> - « Que se passe-t-il si un outil lance une exception – comment gérer les erreurs ? »
> - « Comment intégrer une vraie API au lieu de cet exemple de calculatrice ? »

**Questions/Réponses sur documents (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Ici vous verrez RAG (retrieval-augmented generation) avec l’approche « Easy RAG » de LangChain4j. Les documents sont chargés, automatiquement découpés et intégrés dans un magasin en mémoire, puis un récupérateur de contenu fournit les morceaux pertinents à l’IA au moment de la requête. L’IA répond en fonction de vos documents, non de ses connaissances générales.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) et demandez :
> - « Comment RAG prévient-il les hallucinations IA comparé à l’utilisation des données d’entraînement du modèle ? »
> - « Quelle est la différence entre cette approche facile et un pipeline RAG personnalisé ? »
> - « Comment évoluer pour gérer plusieurs documents ou des bases de connaissances plus larges ? »

**IA responsable** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Construisez la sécurité IA avec une défense en profondeur. Cette démo montre deux couches de protection fonctionnant ensemble :

**Partie 1 : LangChain4j Input Guardrails** – Bloque les prompts dangereux avant qu’ils n’atteignent le LLM. Créez des garde-fous personnalisés qui vérifient la présence de mots-clés ou motifs interdits. Ils tournent dans votre code, donc rapides et gratuits.

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

**Partie 2 : Filtres de sécurité du fournisseur** – GitHub Models dispose de filtres intégrés qui attrapent ce que vos garde-fous pourraient manquer. Vous verrez des blocages stricts (erreurs HTTP 400) pour violations graves et des refus doux où l’IA décline poliment.

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) et demandez :
> - « Qu’est-ce qu’InputGuardrail et comment en créer un ? »
> - « Quelle est la différence entre un blocage dur et un refus doux ? »
> - « Pourquoi utiliser à la fois garde-fous et filtres fournisseur ? »

## Étapes suivantes

**Module suivant :** [01-introduction – Premiers pas avec LangChain4j](../01-introduction/README.md)

---

**Navigation :** [← Retour au principal](../README.md) | [Suivant : Module 01 – Introduction →](../01-introduction/README.md)

---

## Dépannage

### Première compilation Maven

**Problème :** Le premier `mvn clean compile` ou `mvn package` prend beaucoup de temps (10-15 minutes)

**Cause :** Maven doit télécharger toutes les dépendances du projet (Spring Boot, bibliothèques LangChain4j, SDK Azure, etc.) lors de la première compilation.

**Solution :** C’est un comportement normal. Les compilations suivantes seront beaucoup plus rapides car les dépendances sont mises en cache localement. Le temps de téléchargement dépend de la vitesse de votre réseau.

### Syntaxe des commandes Maven sous PowerShell

**Problème :** Les commandes Maven échouent avec l’erreur `Unknown lifecycle phase ".mainClass=..."`
**Cause** : PowerShell interprète `=` comme un opérateur d'affectation de variable, ce qui casse la syntaxe des propriétés Maven

**Solution** : Utilisez l'opérateur d'arrêt d'analyse `--%` avant la commande Maven :

**PowerShell :**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash :**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

L'opérateur `--%` indique à PowerShell de passer tous les arguments restants littéralement à Maven sans interprétation.

### Affichage des emojis dans Windows PowerShell

**Problème** : Les réponses de l'IA affichent des caractères indésirables (par exemple, `????` ou `â??`) au lieu des emojis dans PowerShell

**Cause** : L'encodage par défaut de PowerShell ne supporte pas les emojis UTF-8

**Solution** : Exécutez cette commande avant de lancer les applications Java :
```cmd
chcp 65001
```

Cela force l'encodage UTF-8 dans le terminal. Sinon, utilisez Windows Terminal qui offre une meilleure prise en charge de l'Unicode.

### Débogage des appels API

**Problème** : Erreurs d'authentification, limites de taux ou réponses inattendues du modèle d'IA

**Solution** : Les exemples incluent `.logRequests(true)` et `.logResponses(true)` pour afficher les appels API dans la console. Cela aide à dépanner les erreurs d'authentification, les limites de taux ou les réponses inattendues. Supprimez ces options en production pour réduire les logs.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :  
Ce document a été traduit à l’aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforçons d’assurer l’exactitude, veuillez noter que les traductions automatisées peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue d’origine doit être considéré comme la source faisant foi. Pour les informations cruciales, il est recommandé de recourir à une traduction professionnelle réalisée par un humain. Nous déclinons toute responsabilité en cas de malentendus ou de mauvaises interprétations résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->