# Module 01 : Démarrage avec LangChain4j

## Table des matières

- [Présentation vidéo](../../../01-introduction)
- [Ce que vous apprendrez](../../../01-introduction)
- [Prérequis](../../../01-introduction)
- [Comprendre le problème central](../../../01-introduction)
- [Comprendre les tokens](../../../01-introduction)
- [Comment fonctionne la mémoire](../../../01-introduction)
- [Comment cela utilise LangChain4j](../../../01-introduction)
- [Déployer l'infrastructure Azure OpenAI](../../../01-introduction)
- [Exécuter l'application localement](../../../01-introduction)
- [Utiliser l'application](../../../01-introduction)
  - [Chat sans état (panneau de gauche)](../../../01-introduction)
  - [Conversation avec état (panneau de droite)](../../../01-introduction)
- [Étapes suivantes](../../../01-introduction)

## Présentation vidéo

Regardez cette session en direct qui explique comment commencer avec ce module :

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Ce que vous apprendrez

Dans le démarrage rapide, vous avez utilisé GitHub Models pour envoyer des invites, appeler des outils, construire un pipeline RAG et tester des garde-fous. Ces démonstrations montraient ce qui est possible — maintenant nous passons à Azure OpenAI et GPT-5.2 pour commencer à construire des applications de style production. Ce module se concentre sur l'IA conversationnelle qui se souvient du contexte et maintient l'état — les concepts que ces démonstrations en démarrage rapide utilisaient en coulisses mais n'expliquaient pas.

Nous utiliserons Azure OpenAI GPT-5.2 tout au long de ce guide car ses capacités avancées de raisonnement rendent le comportement de différents modèles plus apparent. Lorsque vous ajoutez de la mémoire, vous verrez clairement la différence. Cela facilite la compréhension de ce que chaque composant apporte à votre application.

Vous construirez une application démontrant les deux modèles :

**Chat sans état** - Chaque requête est indépendante. Le modèle n'a aucun souvenir des messages précédents. C'est le modèle que vous avez utilisé dans le démarrage rapide.

**Conversation avec état** - Chaque requête inclut l'historique de la conversation. Le modèle maintient le contexte à travers plusieurs échanges. C'est ce que nécessitent les applications en production.

## Prérequis

- Abonnement Azure avec accès à Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Note :** Java, Maven, Azure CLI et Azure Developer CLI (azd) sont préinstallés dans le devcontainer fourni.

> **Note :** Ce module utilise GPT-5.2 sur Azure OpenAI. Le déploiement est configuré automatiquement via `azd up` - ne modifiez pas le nom du modèle dans le code.

## Comprendre le problème central

Les modèles de langage sont sans état. Chaque appel API est indépendant. Si vous envoyez "Je m'appelle John" puis demandez "Comment je m'appelle ?", le modèle ne sait pas que vous venez de vous présenter. Il traite chaque requête comme si c'était la première conversation que vous avez jamais eue.

C'est suffisant pour des questions-réponses simples mais inutilisable pour de vraies applications. Les bots de service client doivent se souvenir de ce que vous leur avez dit. Les assistants personnels ont besoin de contexte. Toute conversation à plusieurs tours nécessite de la mémoire.

Le schéma suivant contraste les deux approches — à gauche, un appel sans état qui oublie votre nom ; à droite, un appel avec état soutenu par ChatMemory qui s'en souvient.

<img src="../../../translated_images/fr/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*La différence entre les conversations sans état (appels indépendants) et avec état (contexte pris en compte)*

## Comprendre les tokens

Avant d'entrer dans les conversations, il est important de comprendre les tokens - les unités de base de texte que les modèles de langage traitent :

<img src="../../../translated_images/fr/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Exemple de comment un texte est découpé en tokens - "I love AI!" devient 4 unités de traitement distinctes*

Les tokens sont la manière dont les modèles d'IA mesurent et traitent le texte. Les mots, la ponctuation et même les espaces peuvent être des tokens. Votre modèle a une limite sur le nombre de tokens qu'il peut traiter à la fois (400 000 pour GPT-5.2, avec jusqu'à 272 000 tokens d'entrée et 128 000 tokens de sortie). Comprendre les tokens vous aide à gérer la longueur de la conversation et les coûts.

## Comment fonctionne la mémoire

La mémoire de conversation résout le problème de l'absence d'état en maintenant l'historique des échanges. Avant d'envoyer votre requête au modèle, le cadre ajoute en préfixe les messages précédents pertinents. Quand vous demandez "Comment je m'appelle ?", le système envoie en réalité l’historique complet des messages, permettant au modèle de voir que vous avez précédemment dit "Je m'appelle John".

LangChain4j fournit des implémentations de mémoire qui gèrent cela automatiquement. Vous choisissez combien de messages conserver et le cadre gère la fenêtre de contexte. Le schéma ci-dessous montre comment MessageWindowChatMemory maintient une fenêtre coulissante des messages récents.

<img src="../../../translated_images/fr/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory maintient une fenêtre coulissante des messages récents, supprimant automatiquement les anciens*

## Comment cela utilise LangChain4j

Ce module étend le démarrage rapide en intégrant Spring Boot et en ajoutant la mémoire de conversation. Voici comment les éléments s'assemblent :

**Dépendances** — Ajoutez deux bibliothèques LangChain4j :

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

**Modèle de chat** — Configurez Azure OpenAI comme bean Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)) :

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

Le builder lit les identifiants depuis les variables d’environnement définies par `azd up`. Le paramètre `baseUrl` pointant vers votre point de terminaison Azure permet au client OpenAI de fonctionner avec Azure OpenAI.

**Mémoire de conversation** — Suivez l’historique du chat avec MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)) :

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Créez la mémoire avec `withMaxMessages(10)` pour conserver les 10 derniers messages. Ajoutez les messages utilisateur et IA avec des wrappers typés : `UserMessage.from(text)` et `AiMessage.from(text)`. Récupérez l’historique avec `memory.messages()` et envoyez-le au modèle. Le service stocke des instances de mémoire séparées par ID de conversation, permettant à plusieurs utilisateurs de discuter simultanément.

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) et demandez :
> - « Comment MessageWindowChatMemory décide-t-il quels messages supprimer lorsque la fenêtre est pleine ? »
> - « Puis-je implémenter un stockage mémoire personnalisé utilisant une base de données au lieu de la mémoire en RAM ? »
> - « Comment ajouter un résumé pour compresser l'historique ancien des conversations ? »

Le point de terminaison de chat sans état ignore la mémoire — juste `chatModel.chat(prompt)` comme dans le démarrage rapide. Le point de terminaison avec état ajoute les messages à la mémoire, récupère l’historique et inclut ce contexte à chaque requête. Même configuration de modèle, modèles différents.

## Déployer l'infrastructure Azure OpenAI

**Bash :**
```bash
cd 01-introduction
azd up  # Sélectionnez l'abonnement et l'emplacement (eastus2 recommandé)
```

**PowerShell :**
```powershell
cd 01-introduction
azd up  # Sélectionnez l'abonnement et l'emplacement (estus2 recommandé)
```

> **Note :** Si vous rencontrez une erreur de type timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), exécutez simplement `azd up` de nouveau. Les ressources Azure peuvent être encore en cours de provisionnement en arrière-plan, et réessayer permet au déploiement de se terminer une fois que les ressources sont dans un état terminal.

Cela va :
1. Déployer la ressource Azure OpenAI avec les modèles GPT-5.2 et text-embedding-3-small
2. Générer automatiquement le fichier `.env` à la racine du projet avec les identifiants
3. Configurer toutes les variables d’environnement nécessaires

**Vous avez des problèmes de déploiement ?** Consultez le [README Infrastructure](infra/README.md) pour un dépannage détaillé incluant les conflits de noms de sous-domaines, les étapes manuelles via le portail Azure, et des conseils sur la configuration du modèle.

**Vérifiez que le déploiement a réussi :**

**Bash :**
```bash
cat ../.env  # Devrait afficher AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

**PowerShell :**
```powershell
Get-Content ..\.env  # Devrait afficher AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

> **Note :** La commande `azd up` génère automatiquement le fichier `.env`. Si vous devez le mettre à jour plus tard, vous pouvez soit éditer le fichier `.env` manuellement, soit le régénérer en exécutant :
>
> **Bash :**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell :**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## Exécuter l'application localement

**Vérifiez le déploiement :**

Assurez-vous que le fichier `.env` existe à la racine avec les identifiants Azure. Exécutez ceci depuis le répertoire du module (`01-introduction/`) :

**Bash :**
```bash
cat ../.env  # Devrait afficher AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell :**
```powershell
Get-Content ..\.env  # Devrait afficher AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Démarrer les applications :**

**Option 1 : Utiliser le Spring Boot Dashboard (recommandé pour les utilisateurs de VS Code)**

Le conteneur de développement inclut l’extension Spring Boot Dashboard, qui fournit une interface visuelle pour gérer toutes les applications Spring Boot. Vous pouvez le trouver dans la barre d’activité à gauche de VS Code (recherchez l’icône Spring Boot).

Depuis le Spring Boot Dashboard, vous pouvez :
- Voir toutes les applications Spring Boot disponibles dans l’espace de travail
- Démarrer/arrêter les applications en un clic
- Voir les logs d’application en temps réel
- Surveiller l'état des applications

Il suffit de cliquer sur le bouton play à côté de "introduction" pour démarrer ce module, ou de lancer tous les modules à la fois.

<img src="../../../translated_images/fr/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Le Spring Boot Dashboard dans VS Code — démarrez, arrêtez et surveillez tous les modules depuis un seul endroit*

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

Ou démarrez juste ce module :

**Bash :**
```bash
cd 01-introduction
./start.sh
```

**PowerShell :**
```powershell
cd 01-introduction
.\start.ps1
```

Les deux scripts chargent automatiquement les variables d'environnement à partir du fichier `.env` à la racine et construiront les JARs s'ils n'existent pas.

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

Ouvrez http://localhost:8080 dans votre navigateur.

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

## Utiliser l'application

L'application fournit une interface web avec deux implémentations de chat côte à côte.

<img src="../../../translated_images/fr/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Tableau de bord montrant à la fois Simple Chat (sans état) et Conversational Chat (avec état)*

### Chat sans état (panneau de gauche)

Testez ceci en premier. Dites « Je m'appelle John » puis demandez immédiatement « Comment je m'appelle ? » Le modèle ne se souviendra pas car chaque message est indépendant. Cela démontre le problème central avec l’intégration basique des modèles de langage – absence de contexte de conversation.

<img src="../../../translated_images/fr/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*L’IA ne se souvient pas de votre nom du message précédent*

### Conversation avec état (panneau de droite)

Essayez maintenant la même séquence ici. Dites « Je m'appelle John » puis « Comment je m'appelle ? » Cette fois il s’en souvient. La différence est MessageWindowChatMemory – il maintient l’historique de la conversation et l’inclut dans chaque requête. C’est ainsi que fonctionne l’IA conversationnelle en production.

<img src="../../../translated_images/fr/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*L’IA se souvient de votre nom plus tôt dans la conversation*

Les deux panneaux utilisent le même modèle GPT-5.2. La seule différence est la mémoire. Cela montre clairement ce que la mémoire apporte à votre application et pourquoi elle est essentielle pour les cas d’utilisation réels.

## Étapes suivantes

**Module suivant :** [02-prompt-engineering - Création d'invites avec GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigation :** [← Précédent : Module 00 - Démarrage rapide](../00-quick-start/README.md) | [Retour au principal](../README.md) | [Suivant : Module 02 - Création d'invites →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Clause de non-responsabilité** :  
Ce document a été traduit à l'aide du service de traduction automatisée [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforçons d'assurer l'exactitude, veuillez noter que les traductions automatiques peuvent comporter des erreurs ou des inexactitudes. Le document original dans sa langue d'origine doit être considéré comme la source faisant autorité. Pour toute information critique, il est recommandé de faire appel à une traduction professionnelle réalisée par un humain. Nous ne saurions être tenus responsables des malentendus ou erreurs d'interprétation résultant de l'utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->