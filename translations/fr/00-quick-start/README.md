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
  - [1. Chat basique](../../../00-quick-start)
  - [2. Modèles de prompt](../../../00-quick-start)
  - [3. Appel de fonction](../../../00-quick-start)
  - [4. Questions/Réponses sur documents (Easy RAG)](../../../00-quick-start)
  - [5. IA responsable](../../../00-quick-start)
- [Ce que chaque exemple montre](../../../00-quick-start)
- [Prochaines étapes](../../../00-quick-start)
- [Dépannage](../../../00-quick-start)

## Introduction

Ce démarrage rapide est destiné à vous permettre de commencer à utiliser LangChain4j le plus rapidement possible. Il couvre les bases absolues de la construction d’applications IA avec LangChain4j et les modèles GitHub. Dans les modules suivants, vous utiliserez Azure OpenAI avec LangChain4j pour créer des applications plus avancées.

## Qu'est-ce que LangChain4j ?

LangChain4j est une bibliothèque Java qui simplifie la création d’applications alimentées par l’IA. Au lieu de gérer les clients HTTP et l’analyse JSON, vous travaillez avec des APIs Java propres.

La "chaîne" dans LangChain fait référence à l’enchaînement de plusieurs composants – vous pouvez enchaîner un prompt à un modèle, puis à un parseur, ou enchaîner plusieurs appels IA où la sortie d’un sert d’entrée au suivant. Ce démarrage rapide se concentre sur les fondamentaux avant d’explorer des chaînes plus complexes.

<img src="../../../translated_images/fr/langchain-concept.ad1fe6cf063515e1.webp" alt="Concept d’enchaînement LangChain4j" width="800"/>

*Enchaînement des composants dans LangChain4j – les blocs de construction se connectent pour créer des flux de travail IA puissants*

Nous utiliserons trois composants principaux :

**ChatModel** – L’interface pour les interactions avec le modèle IA. Appelez `model.chat("prompt")` et obtenez une chaîne de réponse. Nous utilisons `OpenAiOfficialChatModel` qui fonctionne avec des points de terminaison compatibles OpenAI comme les modèles GitHub.

**AiServices** – Crée des interfaces de service IA typées. Définissez des méthodes, annotez-les avec `@Tool`, et LangChain4j s’occupe de l’orchestration. L’IA appelle automatiquement vos méthodes Java quand c’est nécessaire.

**MessageWindowChatMemory** – Maintient l’historique de la conversation. Sans cela, chaque requête est indépendante. Avec, l’IA se souvient des messages précédents et maintient le contexte sur plusieurs tours.

<img src="../../../translated_images/fr/architecture.eedc993a1c576839.webp" alt="Architecture LangChain4j" width="800"/>

*Architecture LangChain4j – les composants principaux travaillent ensemble pour alimenter vos applications IA*

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
  
Le module `langchain4j-open-ai-official` fournit la classe `OpenAiOfficialChatModel` qui se connecte aux APIs compatibles OpenAI. Les modèles GitHub utilisent le même format d’API, donc aucun adaptateur spécial n’est nécessaire – il suffit de pointer l’URL de base vers `https://models.github.ai/inference`.

Le module `langchain4j-easy-rag` fournit la division automatique des documents, l’intégration et la récupération, vous permettant de construire des applications RAG sans configurer manuellement chaque étape.

## Prérequis

**Utilisez-vous le conteneur Dev ?** Java et Maven sont déjà installés. Vous avez seulement besoin d’un token d’accès personnel GitHub.

**Développement local :**
- Java 21+, Maven 3.9+
- Token d’accès personnel GitHub (instructions ci-dessous)

> **Note :** Ce module utilise `gpt-4.1-nano` des modèles GitHub. Ne modifiez pas le nom du modèle dans le code – il est configuré pour fonctionner avec les modèles disponibles de GitHub.

## Installation

### 1. Obtenez votre token GitHub

1. Rendez-vous sur [Paramètres GitHub → Tokens d'accès personnel](https://github.com/settings/personal-access-tokens)
2. Cliquez sur "Générer un nouveau token"
3. Donnez un nom descriptif (ex. "LangChain4j Demo")
4. Définissez une expiration (7 jours recommandé)
5. Sous "Autorisations du compte", trouvez "Models" et mettez en "Lecture seule"
6. Cliquez sur "Générer le token"
7. Copiez et sauvegardez votre token – vous ne le reverrez plus

### 2. Configurez votre token

**Option 1 : Utiliser VS Code (recommandé)**

Si vous utilisez VS Code, ajoutez votre token dans le fichier `.env` à la racine du projet :

Si le fichier `.env` n’existe pas, copiez `.env.example` en `.env` ou créez un nouveau fichier `.env` à la racine.

**Exemple de fichier `.env` :**
```bash
# Dans /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```
  
Vous pouvez ensuite simplement faire un clic droit sur n’importe quel fichier de démo (ex. `BasicChatDemo.java`) dans l’Explorateur et sélectionner **"Run Java"**, ou utiliser les configurations de lancement dans le panneau Exécuter et Déboguer.

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

**Avec VS Code :** Faites un clic droit sur n’importe quel fichier de démo dans l’Explorateur et sélectionnez **"Run Java"**, ou utilisez les configurations de lancement dans le panneau Exécuter et Déboguer (assurez-vous d’abord d’avoir ajouté votre token dans `.env`).

**Avec Maven :** Vous pouvez aussi exécuter en ligne de commande :

### 1. Chat basique

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
  
Montre les prompts zero-shot, few-shot, chain-of-thought, et basés sur des rôles.

### 3. Appel de fonction

**Bash :**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
**PowerShell :**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
L’IA appelle automatiquement vos méthodes Java quand c’est nécessaire.

### 4. Questions/Réponses sur documents (Easy RAG)

**Bash :**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
**PowerShell :**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
Posez des questions sur vos documents avec Easy RAG, intégration et récupération automatiques.

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

**Chat basique** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Commencez ici pour voir LangChain4j dans sa forme la plus simple. Vous créerez un `OpenAiOfficialChatModel`, enverrez un prompt avec `.chat()`, et recevrez une réponse. Cela montre les bases : comment initialiser les modèles avec des points de terminaison personnalisés et des clés API. Une fois que vous comprenez ce modèle, tout le reste s’appuie dessus.

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
> - « Comment passer des modèles GitHub à Azure OpenAI dans ce code ? »
> - « Quels autres paramètres puis-je configurer dans OpenAiOfficialChatModel.builder() ? »
> - « Comment ajouter des réponses en streaming au lieu d’attendre la réponse complète ? »

**Ingénierie du prompt** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Maintenant que vous savez comment parler à un modèle, explorons ce que vous lui dites. Cette démo utilise la même configuration de modèle mais montre cinq modèles différents de prompt. Essayez les prompts zero-shot pour des instructions directes, few-shot qui apprennent à partir d’exemples, chain-of-thought qui exposent le raisonnement, et basés sur des rôles qui mettent en contexte. Vous verrez comment le même modèle donne des résultats très différents selon la formulation de la demande.

La démo montre aussi les modèles de templates de prompt, un moyen puissant de créer des prompts réutilisables avec des variables.  
L’exemple ci-dessous montre un prompt utilisant le `PromptTemplate` de LangChain4j pour remplir des variables. L’IA répondra en fonction de la destination et de l’activité fournies.

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
> - « Quelle est la différence entre zero-shot et few-shot prompting, et quand utiliser chacun ? »
> - « Comment le paramètre température affecte-t-il les réponses du modèle ? »
> - « Quelles techniques prévenir contre les attaques d’injection de prompt en production ? »
> - « Comment créer des objets PromptTemplate réutilisables pour des modèles fréquents ? »

**Intégration d’outil** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

C’est là que LangChain4j devient puissant. Vous utiliserez `AiServices` pour créer un assistant IA qui peut appeler vos méthodes Java. Il suffit d’annoter les méthodes avec `@Tool("description")` et LangChain4j gère le reste – l’IA décide automatiquement quand utiliser chaque outil selon ce que demande l’utilisateur. Cela montre l’appel de fonction, une technique clé pour construire une IA capable d’agir, pas seulement de répondre à des questions.

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

Ici vous verrez le RAG (retrieval-augmented generation) avec l’approche "Easy RAG" de LangChain4j. Les documents sont chargés, automatiquement divisés et intégrés dans un magasin en mémoire, puis un récupérateur fournit les fragments pertinents à l’IA lors de la requête. L’IA répond en fonction de vos documents, pas de ses connaissances générales.

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
> - « Comment le RAG empêche-t-il les hallucinations IA par rapport à l’utilisation des données d’entraînement du modèle ? »
> - « Quelle est la différence entre cette approche simple et un pipeline RAG personnalisé ? »
> - « Comment scalabiliser ceci pour gérer plusieurs documents ou des bases de connaissances plus grandes ? »

**IA responsable** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Construisez la sécurité IA avec une défense en profondeur. Cette démo montre deux couches de protection en action :

**Partie 1 : LangChain4j Input Guardrails** – Bloquez les prompts dangereux avant qu’ils atteignent le LLM. Créez des garde-fous personnalisés qui vérifient les mots-clés ou motifs interdits. Ceux-ci tournent dans votre code, donc ils sont rapides et gratuits.

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
  
**Partie 2 : Filtres de sécurité du fournisseur** – Les modèles GitHub possèdent des filtres intégrés qui interceptent ce que vos garde-fous pourraient manquer. Vous verrez des blocages durs (erreurs HTTP 400) pour les violations graves et des refus doux où l’IA décline poliment.

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) et demandez :
> - « Qu’est-ce que InputGuardrail et comment en créer un ? »
> - « Quelle est la différence entre un blocage dur et un refus doux ? »
> - « Pourquoi utiliser à la fois des garde-fous et des filtres fournisseur ? »

## Prochaines étapes

**Module suivant :** [01-introduction - Premiers pas avec LangChain4j et gpt-5 sur Azure](../01-introduction/README.md)

---

**Navigation :** [← Retour au principal](../README.md) | [Suivant : Module 01 - Introduction →](../01-introduction/README.md)

---

## Dépannage

### Première compilation Maven

**Problème :** Le premier `mvn clean compile` ou `mvn package` prend beaucoup de temps (10-15 minutes)

**Cause :** Maven doit télécharger toutes les dépendances du projet (Spring Boot, bibliothèques LangChain4j, SDK Azure, etc.) lors de la première compilation.

**Solution :** Ce comportement est normal. Les compilations suivantes seront beaucoup plus rapides car les dépendances sont mises en cache localement. La durée de téléchargement dépend de votre connexion réseau.

### Syntaxe des commandes Maven dans PowerShell

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

L'opérateur `--%` indique à PowerShell de passer littéralement tous les arguments restants à Maven sans les interpréter.

### Affichage des emojis dans Windows PowerShell

**Problème** : Les réponses de l'IA affichent des caractères illisibles (par exemple, `????` ou `â??`) au lieu des emojis dans PowerShell

**Cause** : L'encodage par défaut de PowerShell ne supporte pas les emojis UTF-8

**Solution** : Exécutez cette commande avant de lancer les applications Java :
```cmd
chcp 65001
```

Cela force l'encodage UTF-8 dans le terminal. Sinon, utilisez Windows Terminal qui offre un meilleur support Unicode.

### Débogage des appels API

**Problème** : Erreurs d'authentification, limites de taux ou réponses inattendues du modèle d'IA

**Solution** : Les exemples incluent `.logRequests(true)` et `.logResponses(true)` pour afficher les appels API dans la console. Cela aide à diagnostiquer les erreurs d'authentification, les limites de taux ou les réponses inattendues. Supprimez ces indicateurs en production pour réduire le bruit des logs.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :  
Ce document a été traduit à l’aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforcions d’assurer l’exactitude, veuillez noter que les traductions automatisées peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue d’origine doit être considéré comme la source faisant foi. Pour les informations critiques, il est recommandé de recourir à une traduction professionnelle humaine. Nous déclinons toute responsabilité en cas de malentendus ou de mauvaise interprétation résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->