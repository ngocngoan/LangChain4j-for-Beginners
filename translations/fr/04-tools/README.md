# Module 04 : Agents IA avec Outils

## Table des matières

- [Ce que vous allez apprendre](../../../04-tools)
- [Prérequis](../../../04-tools)
- [Comprendre les agents IA avec outils](../../../04-tools)
- [Comment fonctionne l'appel d'outils](../../../04-tools)
  - [Définitions des outils](../../../04-tools)
  - [Prise de décision](../../../04-tools)
  - [Exécution](../../../04-tools)
  - [Génération de la réponse](../../../04-tools)
  - [Architecture : Spring Boot auto-câblage](../../../04-tools)
- [Chaînage d'outils](../../../04-tools)
- [Exécuter l'application](../../../04-tools)
- [Utilisation de l'application](../../../04-tools)
  - [Essayer une utilisation simple d'outil](../../../04-tools)
  - [Tester le chaînage d'outils](../../../04-tools)
  - [Voir le flux de conversation](../../../04-tools)
  - [Expérimenter avec différentes requêtes](../../../04-tools)
- [Concepts clés](../../../04-tools)
  - [Pattern ReAct (Raisonner et Agir)](../../../04-tools)
  - [Les descriptions d'outils sont importantes](../../../04-tools)
  - [Gestion de session](../../../04-tools)
  - [Gestion des erreurs](../../../04-tools)
- [Outils disponibles](../../../04-tools)
- [Quand utiliser des agents basés sur des outils](../../../04-tools)
- [Outils vs RAG](../../../04-tools)
- [Prochaines étapes](../../../04-tools)

## Ce que vous allez apprendre

Jusqu'à présent, vous avez appris à avoir des conversations avec l'IA, à structurer efficacement les invites et à ancrer les réponses dans vos documents. Mais il y a encore une limitation fondamentale : les modèles de langage ne peuvent générer que du texte. Ils ne peuvent pas vérifier la météo, effectuer des calculs, interroger des bases de données ou interagir avec des systèmes externes.

Les outils changent cela. En donnant au modèle accès à des fonctions qu'il peut appeler, vous le transformez d'un générateur de texte en un agent capable d'agir. Le modèle décide quand il a besoin d'un outil, quel outil utiliser et quels paramètres passer. Votre code exécute la fonction et retourne le résultat. Le modèle intègre ce résultat dans sa réponse.

## Prérequis

- Module [01 - Introduction](../01-introduction/README.md) complété (ressources Azure OpenAI déployées)
- Modules précédents recommandés (ce module fait référence aux [concepts RAG du Module 03](../03-rag/README.md) dans la comparaison Outils vs RAG)
- Fichier `.env` à la racine avec les identifiants Azure (créé par `azd up` dans le Module 01)

> **Note :** Si vous n'avez pas terminé le Module 01, suivez d'abord ses instructions de déploiement.

## Comprendre les agents IA avec outils

> **📝 Note :** Le terme « agents » dans ce module fait référence aux assistants IA enrichis par des capacités d'appel d'outils. Cela diffère des modèles **Agentic AI** (agents autonomes avec planification, mémoire et raisonnement multi-étapes) que nous aborderons dans le [Module 05 : MCP](../05-mcp/README.md).

Sans outils, un modèle de langage ne peut que générer du texte à partir de ses données d'entraînement. Demandez-lui la météo actuelle, il doit deviner. Donnez-lui des outils, et il peut appeler une API météo, effectuer des calculs ou interroger une base de données — puis intégrer ces résultats réels dans sa réponse.

<img src="../../../translated_images/fr/what-are-tools.724e468fc4de64da.webp" alt="Sans outils vs Avec outils" width="800"/>

*Sans outils, le modèle ne peut que deviner — avec outils, il peut appeler des API, faire des calculs et retourner des données en temps réel.*

Un agent IA avec outils suit un pattern **Reasoning and Acting (ReAct)**. Le modèle ne se contente pas de répondre — il réfléchit à ce dont il a besoin, agit en appelant un outil, observe le résultat, puis décide s’il doit agir à nouveau ou fournir la réponse finale :

1. **Raisonner** — L'agent analyse la question de l'utilisateur et détermine les informations nécessaires
2. **Agir** — L'agent sélectionne l’outil adapté, génère les bons paramètres, puis l’appelle
3. **Observer** — L’agent reçoit la sortie de l’outil et évalue le résultat
4. **Répéter ou répondre** — S’il faut plus d’informations, l’agent boucle ; sinon, il compose une réponse en langage naturel

<img src="../../../translated_images/fr/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Pattern ReAct" width="800"/>

*Le cycle ReAct — l’agent raisonne sur ce qu’il doit faire, agit en appelant un outil, observe le résultat et boucle jusqu’à pouvoir fournir la réponse finale.*

Cela se produit automatiquement. Vous définissez les outils et leurs descriptions. Le modèle gère la décision de quand et comment les utiliser.

## Comment fonctionne l'appel d'outils

### Définitions des outils

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Vous définissez des fonctions avec des descriptions claires et une spécification des paramètres. Le modèle voit ces descriptions dans son prompt système et comprend ce que fait chaque outil.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Votre logique de recherche météo
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// L'assistant est automatiquement relié par Spring Boot avec :
// - Bean ChatModel
// - Toutes les méthodes @Tool des classes @Component
// - ChatMemoryProvider pour la gestion des sessions
```

Le diagramme ci-dessous décompose chaque annotation et montre comment chaque élément aide l’IA à comprendre quand appeler l’outil et quels arguments passer :

<img src="../../../translated_images/fr/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomie des définitions d'outils" width="800"/>

*Anatomie d’une définition d’outil — @Tool indique à l’IA quand l’utiliser, @P décrit chaque paramètre, et @AiService assemble tout au démarrage.*

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) et demandez :
> - « Comment intégrer une vraie API météo comme OpenWeatherMap au lieu de données simulées ? »
> - « Qu’est-ce qui fait une bonne description d’outil pour aider l’IA à l’utiliser correctement ? »
> - « Comment gérer les erreurs d’API et les limites de débit dans les implémentations d’outils ? »

### Prise de décision

Quand un utilisateur demande « Quelle est la météo à Seattle ? », le modèle ne choisit pas un outil au hasard. Il compare l’intention de l’utilisateur à chaque description d’outil accessible, attribue un score de pertinence à chacune, et sélectionne la meilleure correspondance. Il génère ensuite un appel de fonction structuré avec les bons paramètres — ici, avec `location` à `"Seattle"`.

Si aucun outil ne correspond à la requête, le modèle répond avec ses propres connaissances. Si plusieurs outils correspondent, il choisit le plus spécifique.

<img src="../../../translated_images/fr/decision-making.409cd562e5cecc49.webp" alt="Comment l'IA décide quel outil utiliser" width="800"/>

*Le modèle évalue chaque outil disponible par rapport à l’intention de l’utilisateur et choisit la meilleure correspondance — c’est pourquoi il est important d’écrire des descriptions d’outils claires et précises.*

### Exécution

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot injecte automatiquement l’interface déclarative `@AiService` avec tous les outils enregistrés, et LangChain4j exécute les appels d’outils automatiquement. En coulisses, un appel complet d’outil passe par six étapes — de la question en langage naturel utilisateur jusqu’à la réponse finale :

<img src="../../../translated_images/fr/tool-calling-flow.8601941b0ca041e6.webp" alt="Flux d'appel d'outil" width="800"/>

*Le flux de bout en bout — l’utilisateur pose une question, le modèle sélectionne un outil, LangChain4j l’exécute, et le modèle intègre le résultat dans une réponse naturelle.*

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) et demandez :
> - « Comment fonctionne le pattern ReAct et pourquoi est-il efficace pour les agents IA ? »
> - « Comment l’agent décide quel outil utiliser et dans quel ordre ? »
> - « Que se passe-t-il si une exécution d’outil échoue - comment gérer les erreurs de manière robuste ? »

### Génération de la réponse

Le modèle reçoit les données météo et les formate en une réponse en langage naturel pour l’utilisateur.

### Architecture : Spring Boot auto-câblage

Ce module utilise l’intégration Spring Boot de LangChain4j avec des interfaces déclaratives `@AiService`. Au démarrage, Spring Boot découvre chaque `@Component` qui contient des méthodes `@Tool`, votre bean `ChatModel`, et le `ChatMemoryProvider` — puis les connecte tous ensemble dans une interface `Assistant` unique sans aucun code répétitif.

<img src="../../../translated_images/fr/spring-boot-wiring.151321795988b04e.webp" alt="Architecture auto-câblage Spring Boot" width="800"/>

*L’interface @AiService rassemble le ChatModel, les composants outils et le fournisseur de mémoire — Spring Boot gère automatiquement tout le câblage.*

Les avantages clés de cette approche :

- **Auto-câblage Spring Boot** — ChatModel et outils injectés automatiquement
- **Pattern @MemoryId** — Gestion automatique de la mémoire basée sur la session
- **Instance unique** — Assistant créé une seule fois et réutilisé pour de meilleures performances
- **Exécution typée** — Appels directs aux méthodes Java avec conversion de types
- **Orchestration multi-étapes** — Gère automatiquement le chaînage d’outils
- **Zéro code répétitif** — Pas d’utilisation manuelle de `AiServices.builder()` ou de HashMap pour la mémoire

Les approches alternatives (construction manuelle via `AiServices.builder()`) demandent plus de code et ne bénéficient pas de l’intégration Spring Boot.

## Chaînage d'outils

**Chaînage d'outils** — La vraie puissance des agents basés sur outils apparaît lorsqu’une seule question nécessite plusieurs outils. Demandez « Quelle est la météo à Seattle en Fahrenheit ? » et l’agent enchaîne automatiquement deux outils : d’abord il appelle `getCurrentWeather` pour obtenir la température en Celsius, puis passe cette valeur à `celsiusToFahrenheit` pour la conversion — tout cela dans un seul tour de conversation.

<img src="../../../translated_images/fr/tool-chaining-example.538203e73d09dd82.webp" alt="Exemple de chaînage d'outils" width="800"/>

*Chaînage d’outils en action — l’agent appelle d’abord getCurrentWeather, puis transmet le résultat en Celsius à celsiusToFahrenheit, et fournit une réponse combinée.*

**Échecs gracieux** — Demandez la météo d’une ville absente des données simulées. L’outil retourne un message d’erreur, et l’IA explique qu’elle ne peut pas aider au lieu de planter. Les outils échouent en toute sécurité. Le diagramme ci-dessous compare les deux approches — avec une gestion d’erreur adéquate, l’agent attrape l’exception et répond utilement, sinon toute l’application plante :

<img src="../../../translated_images/fr/error-handling-flow.9a330ffc8ee0475c.webp" alt="Flux de gestion des erreurs" width="800"/>

*Quand un outil échoue, l’agent intercepte l’erreur et répond avec une explication utile au lieu de planter.*

Cela se produit en un seul tour de conversation. L’agent orchestre plusieurs appels d’outils de manière autonome.

## Exécuter l'application

**Vérifier le déploiement :**

Assurez-vous que le fichier `.env` existe à la racine avec les identifiants Azure (créé au cours du Module 01). Exécutez ceci depuis le dossier du module (`04-tools/`) :

**Bash :**
```bash
cat ../.env  # Devrait afficher AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell :**
```powershell
Get-Content ..\.env  # Doit afficher AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Démarrer l'application :**

> **Note :** Si vous avez déjà démarré toutes les applications avec `./start-all.sh` depuis la racine (comme décrit dans le Module 01), ce module est déjà en fonctionnement sur le port 8084. Vous pouvez sauter les commandes de démarrage ci-dessous et accéder directement à http://localhost:8084.

**Option 1 : Utiliser le Spring Boot Dashboard (recommandé pour les utilisateurs VS Code)**

Le conteneur de dev inclut l’extension Spring Boot Dashboard, qui fournit une interface visuelle pour gérer toutes les applications Spring Boot. Vous la trouverez dans la barre d’activité à gauche dans VS Code (cherchez l’icône Spring Boot).

Depuis le Spring Boot Dashboard, vous pouvez :
- Voir toutes les applications Spring Boot disponibles dans l’espace de travail
- Démarrer/arrêter les applications en un clic
- Visualiser les logs en temps réel
- Surveiller le statut des applications

Cliquez simplement sur le bouton play à côté de « tools » pour démarrer ce module, ou démarrez tous les modules en une fois.

Voici à quoi ressemble le Spring Boot Dashboard dans VS Code :

<img src="../../../translated_images/fr/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Le Spring Boot Dashboard dans VS Code — démarrer, arrêter et surveiller tous les modules depuis un seul endroit*

**Option 2 : Utiliser les scripts shell**

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
cd 04-tools
./start.sh
```

**PowerShell :**
```powershell
cd 04-tools
.\start.ps1
```

Les deux scripts chargent automatiquement les variables d’environnement du fichier `.env` racine et reconstruiront les JARs s’ils n’existent pas.

> **Note :** Si vous préférez compiler manuellement tous les modules avant de démarrer :
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

Ouvrez http://localhost:8084 dans votre navigateur.

**Pour arrêter :**

**Bash :**
```bash
./stop.sh  # Ce module seulement
# Ou
cd .. && ./stop-all.sh  # Tous les modules
```

**PowerShell :**
```powershell
.\stop.ps1  # Ce module uniquement
# Ou
cd ..; .\stop-all.ps1  # Tous les modules
```

## Utilisation de l'application

L’application fournit une interface web où vous pouvez interagir avec un agent IA ayant accès aux outils météo et conversion de température. Voici à quoi ressemble l’interface — elle inclut des exemples rapides et un panneau de discussion pour envoyer des requêtes :
<a href="images/tools-homepage.png"><img src="../../../translated_images/fr/tools-homepage.4b4cd8b2717f9621.webp" alt="Interface des Outils Agent IA" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*L'interface des Outils Agent IA - exemples rapides et interface de chat pour interagir avec les outils*

### Essayez l'utilisation simple d'un outil

Commencez par une demande simple : "Convertir 100 degrés Fahrenheit en Celsius". L'agent reconnaît qu'il a besoin de l'outil de conversion de température, l'appelle avec les bons paramètres, et renvoie le résultat. Remarquez comme cela semble naturel - vous n'avez pas spécifié quel outil utiliser ni comment l'appeler.

### Testez l'enchaînement d'outils

Essayez maintenant quelque chose de plus complexe : "Quel temps fait-il à Seattle et convertir cela en Fahrenheit ?" Regardez l'agent travailler cela en étapes. Il obtient d'abord la météo (qui renvoie en Celsius), reconnaît qu'il doit convertir en Fahrenheit, appelle l'outil de conversion, et combine les deux résultats en une seule réponse.

### Voir le déroulement de la conversation

L'interface de chat conserve l'historique de la conversation, vous permettant d'avoir des interactions multi-tours. Vous pouvez voir toutes les requêtes et réponses précédentes, ce qui facilite le suivi de la conversation et la compréhension de la manière dont l'agent construit le contexte sur plusieurs échanges.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/fr/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation avec Appels Multiples d'Outils" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Conversation multi-tours montrant des conversions simples, des recherches météo, et l'enchaînement d'outils*

### Expérimentez avec différentes demandes

Essayez diverses combinaisons :
- Recherches météo : "Quel temps fait-il à Tokyo ?"
- Conversions de température : "Quel est 25°C en Kelvin ?"
- Requêtes combinées : "Vérifiez la météo à Paris et dites-moi si elle est au-dessus de 20°C"

Remarquez comment l'agent interprète le langage naturel et le mappe aux appels d'outils appropriés.

## Concepts Clés

### Modèle ReAct (Raisonner et Agir)

L'agent alterne entre raisonnement (décider quoi faire) et action (utiliser les outils). Ce modèle permet une résolution autonome des problèmes plutôt que de simplement répondre aux instructions.

### Les descriptions des outils comptent

La qualité de vos descriptions d'outils affecte directement la manière dont l'agent les utilise. Des descriptions claires et spécifiques aident le modèle à comprendre quand et comment appeler chaque outil.

### Gestion des sessions

L'annotation `@MemoryId` permet une gestion automatique de la mémoire basée sur les sessions. Chaque ID de session obtient sa propre instance `ChatMemory` gérée par le bean `ChatMemoryProvider`, permettant à plusieurs utilisateurs d'interagir simultanément avec l'agent sans que leurs conversations ne se mélangent. Le schéma suivant montre comment plusieurs utilisateurs sont dirigés vers des mémoires isolées en fonction de leurs IDs session :

<img src="../../../translated_images/fr/session-management.91ad819c6c89c400.webp" alt="Gestion des sessions avec @MemoryId" width="800"/>

*Chaque ID de session correspond à un historique de conversation isolé — les utilisateurs ne voient jamais les messages des autres.*

### Gestion des erreurs

Les outils peuvent échouer — les API peuvent expirer, les paramètres être invalides, les services externes tomber en panne. Les agents en production ont besoin d’une gestion d’erreurs afin que le modèle puisse expliquer les problèmes ou essayer des alternatives plutôt que de faire planter toute l’application. Quand un outil lance une exception, LangChain4j la capture et renvoie le message d’erreur au modèle, qui peut alors expliquer le problème en langage naturel.

## Outils Disponibles

Le schéma ci-dessous montre l'écosystème large des outils que vous pouvez construire. Ce module démontre des outils météo et de température, mais le même modèle `@Tool` fonctionne pour n’importe quelle méthode Java — des requêtes base de données au traitement des paiements.

<img src="../../../translated_images/fr/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Écosystème des Outils" width="800"/>

*Toute méthode Java annotée avec @Tool devient disponible pour l'IA — le modèle s'étend aux bases de données, APIs, email, opérations sur fichiers, et plus encore.*

## Quand utiliser des agents basés sur les outils

Toutes les demandes ne nécessitent pas d’outils. La décision dépend si l’IA doit interagir avec des systèmes externes ou peut répondre grâce à ses propres connaissances. Le guide suivant résume quand les outils apportent de la valeur et quand ils sont inutiles :

<img src="../../../translated_images/fr/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Quand utiliser les outils" width="800"/>

*Un guide décisionnel rapide — les outils servent pour les données en temps réel, calculs et actions ; les connaissances générales et tâches créatives n'en ont pas besoin.*

## Outils vs RAG

Les modules 03 et 04 étendent tous deux ce que l'IA peut faire, mais de façons fondamentalement différentes. Le RAG donne accès au modèle à la **connaissance** en récupérant des documents. Les outils donnent au modèle la capacité de prendre des **actions** en appelant des fonctions. Le schéma ci-dessous compare ces deux approches côte à côte — du fonctionnement de chaque flux jusqu'aux compromis entre elles :

<img src="../../../translated_images/fr/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Comparaison Outils vs RAG" width="800"/>

*RAG récupère des informations depuis des documents statiques — les Outils exécutent des actions et récupèrent des données dynamiques en temps réel. Beaucoup de systèmes de production combinent les deux.*

En pratique, de nombreux systèmes de production combinent les deux approches : RAG pour ancrer les réponses dans votre documentation, et les Outils pour récupérer des données en direct ou effectuer des opérations.

## Prochaines Étapes

**Module suivant :** [05-mcp - Protocol de Contexte Modèle (MCP)](../05-mcp/README.md)

---

**Navigation :** [← Précédent : Module 03 - RAG](../03-rag/README.md) | [Retour au Principal](../README.md) | [Suivant : Module 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :  
Ce document a été traduit à l’aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforcions d’assurer la précision, veuillez noter que les traductions automatisées peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue native doit être considéré comme la source faisant autorité. Pour les informations critiques, il est recommandé de faire appel à une traduction professionnelle réalisée par un humain. Nous déclinons toute responsabilité en cas de malentendus ou de mauvaises interprétations résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->