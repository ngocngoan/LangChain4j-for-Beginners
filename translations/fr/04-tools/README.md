# Module 04 : Agents IA avec des Outils

## Table des Matières

- [Parcours Vidéo](../../../04-tools)
- [Ce que vous apprendrez](../../../04-tools)
- [Prérequis](../../../04-tools)
- [Comprendre les agents IA avec des outils](../../../04-tools)
- [Comment fonctionne l'appel d'outils](../../../04-tools)
  - [Définitions d'outils](../../../04-tools)
  - [Prise de décision](../../../04-tools)
  - [Exécution](../../../04-tools)
  - [Génération de la réponse](../../../04-tools)
  - [Architecture : Auto-wiring Spring Boot](../../../04-tools)
- [Chaînage d'outils](../../../04-tools)
- [Lancer l'application](../../../04-tools)
- [Utilisation de l'application](../../../04-tools)
  - [Essayer un usage simple d'un outil](../../../04-tools)
  - [Tester le chaînage d'outils](../../../04-tools)
  - [Voir le déroulement de la conversation](../../../04-tools)
  - [Expérimenter avec différentes requêtes](../../../04-tools)
- [Concepts clés](../../../04-tools)
  - [Pattern ReAct (Raisonnement et Action)](../../../04-tools)
  - [Les descriptions d'outils comptent](../../../04-tools)
  - [Gestion des sessions](../../../04-tools)
  - [Gestion des erreurs](../../../04-tools)
- [Outils disponibles](../../../04-tools)
- [Quand utiliser des agents basés sur des outils](../../../04-tools)
- [Outils vs RAG](../../../04-tools)
- [Étapes suivantes](../../../04-tools)

## Parcours Vidéo

Regardez cette session en direct qui explique comment débuter avec ce module :

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="Agents IA avec outils et MCP - Session en direct" width="800"/></a>

## Ce que vous apprendrez

Jusqu'à présent, vous avez appris à converser avec une IA, à structurer efficacement des prompts, et à ancrer les réponses dans vos documents. Mais il existe toujours une limitation fondamentale : les modèles de langage ne peuvent générer que du texte. Ils ne peuvent pas consulter la météo, effectuer des calculs, interroger des bases de données ou interagir avec des systèmes externes.

Les outils changent cela. En donnant au modèle accès à des fonctions qu’il peut appeler, vous le transformez d’un générateur de texte en un agent capable d’agir. Le modèle décide quand il a besoin d’un outil, quel outil utiliser, et quels paramètres transmettre. Votre code exécute la fonction et renvoie le résultat. Le modèle incorpore ce résultat dans sa réponse.

## Prérequis

- Avoir terminé le [Module 01 - Introduction](../01-introduction/README.md) (ressources Azure OpenAI déployées)
- Avoir terminé les modules précédents recommandés (ce module fait référence aux [concepts RAG du Module 03](../03-rag/README.md) dans la comparaison Outils vs RAG)
- Fichier `.env` dans le répertoire racine avec les identifiants Azure (créé par `azd up` dans le Module 01)

> **Note :** Si vous n'avez pas terminé le Module 01, suivez d'abord les instructions de déploiement indiquées.

## Comprendre les agents IA avec des outils

> **📝 Note :** Le terme « agents » dans ce module désigne des assistants IA améliorés avec la capacité d’appeler des outils. Ceci est différent des modèles **Agentic AI** (agents autonomes avec planification, mémoire et raisonnement multi-étapes) que nous aborderons dans le [Module 05 : MCP](../05-mcp/README.md).

Sans outils, un modèle de langage peut uniquement générer du texte à partir de ses données d'entraînement. Demandez-lui la météo actuelle, il doit deviner. Donnez-lui des outils et il peut appeler une API météo, effectuer des calculs ou interroger une base de données — puis intégrer ces résultats réels dans sa réponse.

<img src="../../../translated_images/fr/what-are-tools.724e468fc4de64da.webp" alt="Sans outils vs Avec outils" width="800"/>

*Sans outils, le modèle ne peut que deviner — avec des outils, il peut appeler des API, exécuter des calculs et retourner des données en temps réel.*

Un agent IA avec des outils suit un pattern **Reasoning and Acting (ReAct)**. Le modèle ne se contente pas de répondre — il réfléchit à ce dont il a besoin, agit en appelant un outil, observe le résultat, puis décide s’il doit agir à nouveau ou livrer la réponse finale :

1. **Raisonner** — L’agent analyse la question de l’utilisateur et détermine quelles informations il lui faut  
2. **Agir** — L’agent sélectionne l’outil approprié, génère les paramètres corrects, et l’appelle  
3. **Observer** — L’agent reçoit la sortie de l’outil et évalue le résultat  
4. **Répéter ou Répondre** — Si plus de données sont nécessaires, l’agent recommence ; sinon, il compose une réponse en langage naturel

<img src="../../../translated_images/fr/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Pattern ReAct" width="800"/>

*Le cycle ReAct — l’agent raisonne sur ce qu’il doit faire, agit en appelant un outil, observe le résultat, et boucle jusqu’à pouvoir fournir la réponse finale.*

Cela se passe automatiquement. Vous définissez les outils et leurs descriptions. Le modèle gère la prise de décision pour savoir quand et comment les utiliser.

## Comment fonctionne l'appel d'outils

### Définitions d'outils

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Vous définissez des fonctions avec des descriptions claires et des spécifications de paramètres. Le modèle voit ces descriptions dans son prompt système et comprend ce que fait chaque outil.

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

// L'assistant est automatiquement configuré par Spring Boot avec :
// - Bean ChatModel
// - Toutes les méthodes @Tool des classes @Component
// - ChatMemoryProvider pour la gestion des sessions
```


Le schéma ci-dessous détaille chaque annotation et montre comment chaque élément aide l’IA à comprendre quand appeler l’outil et quels arguments transmettre :

<img src="../../../translated_images/fr/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomie des définitions d'outils" width="800"/>

*Anatomie d’une définition d’outil — @Tool indique à l’IA quand l’utiliser, @P décrit chaque paramètre, et @AiService connecte tout au démarrage.*

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) et demandez :  
> - « Comment intégrerais-je une vraie API météo comme OpenWeatherMap au lieu de données factices ? »  
> - « Qu’est-ce qui fait une bonne description d’outil qui aide l’IA à l’utiliser correctement ? »  
> - « Comment gérer les erreurs d’API et les limites de requêtes dans l’implémentation des outils ? »

### Prise de décision

Lorsqu’un utilisateur demande « Quelle est la météo à Seattle ? », le modèle ne choisit pas un outil au hasard. Il compare l’intention de l’utilisateur à chaque description d’outil accessible, évalue leur pertinence, et sélectionne la meilleure correspondance. Il génère alors un appel de fonction structuré avec les bons paramètres — ici, en définissant `location` à `"Seattle"`.

Si aucun outil ne correspond à la demande, le modèle répond depuis ses propres connaissances. Si plusieurs outils correspondent, il choisit le plus spécifique.

<img src="../../../translated_images/fr/decision-making.409cd562e5cecc49.webp" alt="Comment l'IA décide quel outil utiliser" width="800"/>

*Le modèle évalue chaque outil disponible par rapport à l’intention de l’utilisateur et sélectionne la meilleure correspondance — d’où l’importance d’écrire des descriptions d’outils claires et précises.*

### Exécution

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot connecte automatiquement l’interface déclarative `@AiService` avec tous les outils enregistrés, et LangChain4j exécute les appels aux outils automatiquement. Dans les coulisses, un appel complet d’outil passe par six étapes — depuis la question en langage naturel posée par l'utilisateur jusqu’à la réponse finale en langage naturel :

<img src="../../../translated_images/fr/tool-calling-flow.8601941b0ca041e6.webp" alt="Flux d'appel d'outils" width="800"/>

*Le flux de bout en bout — l’utilisateur pose une question, le modèle sélectionne un outil, LangChain4j l’exécute, et le modèle intègre le résultat dans une réponse naturelle.*

Si vous avez exécuté la [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) du Module 00, vous avez déjà vu ce pattern en action — les outils `Calculator` étaient appelés de la même manière. Le diagramme de séquence ci-dessous détaille exactement ce qui s’est passé sous le capot durant cette démo :

<img src="../../../translated_images/fr/tool-calling-sequence.94802f406ca26278.webp" alt="Diagramme de séquence d'appel d'outils" width="800"/>

*La boucle d’appel d’outils du démo Quick Start — `AiServices` envoie votre message et les schémas d’outils au LLM, le LLM répond par un appel de fonction comme `add(42, 58)`, LangChain4j exécute la méthode `Calculator` localement, et renvoie le résultat pour la réponse finale.*

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) et demandez :  
> - « Comment fonctionne le pattern ReAct et pourquoi est-il efficace pour les agents IA ? »  
> - « Comment l’agent décide quel outil utiliser et dans quel ordre ? »  
> - « Que se passe-t-il si l’exécution d’un outil échoue - comment gérer les erreurs de manière robuste ? »

### Génération de la réponse

Le modèle reçoit les données météo et les formate en une réponse en langage naturel pour l’utilisateur.

### Architecture : Auto-wiring Spring Boot

Ce module utilise l’intégration LangChain4j avec Spring Boot via des interfaces déclaratives `@AiService`. Au démarrage, Spring Boot découvre chaque `@Component` contenant des méthodes `@Tool`, votre bean `ChatModel`, et le `ChatMemoryProvider` — puis les connecte tous dans une seule interface `Assistant` sans aucun code répétitif.

<img src="../../../translated_images/fr/spring-boot-wiring.151321795988b04e.webp" alt="Architecture Auto-wiring Spring Boot" width="800"/>

*L’interface @AiService relie le ChatModel, les composants outils, et le fournisseur mémoire — Spring Boot gère automatiquement tout le câblage.*

Voici le cycle complet d’une requête sous forme de diagramme de séquence — depuis la requête HTTP via le contrôleur, le service, le proxy auto-câblé, jusqu’à l’exécution de l’outil et le retour :

<img src="../../../translated_images/fr/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Diagramme de séquence d'appel d'outil Spring Boot" width="800"/>

*Le cycle complet de la requête Spring Boot — la requête HTTP passe par le contrôleur et le service vers le proxy Assistant automonté, qui orchestre automatiquement le LLM et les appels aux outils.*

Les avantages clés de cette approche :

- **Auto-wiring Spring Boot** — injection automatique du ChatModel et des outils  
- **Pattern @MemoryId** — gestion automatique de la mémoire par session  
- **Instance unique** — Assistant créé une fois pour une meilleure performance  
- **Exécution typée** — méthodes Java appelées directement avec conversion de types  
- **Orchestration multi-étapes** — gestion automatique du chaînage d’outils  
- **Aucun code répétitif** — pas d’appel manuel à `AiServices.builder()` ni gestion explicite de HashMap mémoire  

Les approches alternatives (manuelles via `AiServices.builder()`) nécessitent plus de code et ne profitent pas des avantages de Spring Boot.

## Chaînage d'outils

**Chaînage d'outils** — La vraie puissance des agents basés sur des outils apparaît lorsqu’une seule question nécessite plusieurs outils. Demandez « Quelle est la météo à Seattle en Fahrenheit ? » et l’agent enchaîne automatiquement deux outils : d’abord il appelle `getCurrentWeather` pour obtenir la température en Celsius, puis il transmet cette valeur à `celsiusToFahrenheit` pour la conversion — tout cela en un seul tour de conversation.

<img src="../../../translated_images/fr/tool-chaining-example.538203e73d09dd82.webp" alt="Exemple de chaînage d'outils" width="800"/>

*Chaînage d’outils en action — l’agent appelle d’abord getCurrentWeather, puis passe le résultat en Celsius à celsiusToFahrenheit, et fournit une réponse combinée.*

**Gestion élégante des échecs** — Demandez la météo dans une ville qui n’est pas dans les données factices. L’outil renvoie un message d’erreur, et l’IA explique qu’elle ne peut pas aider au lieu de planter. Les outils échouent en toute sécurité. Le schéma ci-dessous contraste les deux approches — avec une gestion correcte des erreurs, l’agent intercepte l’exception et répond utilement, tandis que sans cela l’application entière plante :

<img src="../../../translated_images/fr/error-handling-flow.9a330ffc8ee0475c.webp" alt="Flux de gestion des erreurs" width="800"/>

*Quand un outil échoue, l’agent attrape l’erreur et répond avec une explication utile au lieu de planter.*

Cela se déroule en un seul tour de conversation. L’agent orchestre les appels multiples aux outils de manière autonome.

## Lancer l'application

**Vérifiez le déploiement :**

Assurez-vous que le fichier `.env` existe dans le répertoire racine avec les identifiants Azure (créé lors du Module 01). Lancez cette commande depuis le répertoire du module (`04-tools/`) :

**Bash :**  
```bash
cat ../.env  # Devrait afficher AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```


**PowerShell :**  
```powershell
Get-Content ..\.env  # Devrait afficher AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```


**Démarrez l’application :**

> **Note :** Si vous avez déjà démarré toutes les applications avec `./start-all.sh` depuis le répertoire racine (comme décrit dans le Module 01), ce module tourne déjà sur le port 8084. Vous pouvez passer les commandes de démarrage ci-dessous et aller directement à http://localhost:8084.

**Option 1 : Utiliser le Spring Boot Dashboard (recommandé pour les utilisateurs VS Code)**

Le conteneur de développement inclut l’extension Spring Boot Dashboard, qui offre une interface visuelle pour gérer toutes les applications Spring Boot. Vous la trouverez dans la barre d’activités à gauche dans VS Code (cherchez l’icône Spring Boot).

Depuis le Spring Boot Dashboard, vous pouvez :  
- Voir toutes les applications Spring Boot disponibles dans l’espace de travail  
- Démarrer/arrêter les applications en un clic  
- Visualiser les logs des applications en temps réel  
- Surveiller le statut des applications
Il suffit de cliquer sur le bouton de lecture à côté de "tools" pour démarrer ce module, ou démarrer tous les modules en même temps.

Voici à quoi ressemble le Spring Boot Dashboard dans VS Code :

<img src="../../../translated_images/fr/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

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

Ou démarrer seulement ce module :

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

Les deux scripts chargent automatiquement les variables d’environnement à partir du fichier `.env` racine et construiront les JARs s’ils n’existent pas.

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

Ouvrez http://localhost:8084 dans votre navigateur.

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

## Utilisation de l’Application

L’application fournit une interface web où vous pouvez interagir avec un agent IA qui a accès aux outils météo et de conversion de température. Voici à quoi ressemble l’interface — elle inclut des exemples rapides et un panneau de discussion pour envoyer des requêtes :

<a href="images/tools-homepage.png"><img src="../../../translated_images/fr/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*L’interface des outils de l’agent IA - exemples rapides et interface de chat pour interagir avec les outils*

### Essayez l’utilisation simple d’un outil

Commencez par une demande simple : « Convertir 100 degrés Fahrenheit en Celsius ». L’agent reconnaît qu’il a besoin de l’outil de conversion de température, l’appelle avec les bons paramètres et renvoie le résultat. Remarquez à quel point cela semble naturel — vous n’avez pas spécifié quel outil utiliser ni comment l’appeler.

### Testez la chaîne d’outils

Essayez maintenant quelque chose de plus complexe : « Quel temps fait-il à Seattle et convertis-le en Fahrenheit ? » Regardez l’agent traiter cela étape par étape. Il récupère d’abord la météo (qui renvoie en Celsius), reconnaît qu’il doit convertir en Fahrenheit, appelle l’outil de conversion, et combine les deux résultats en une seule réponse.

### Voir le déroulement de la conversation

L’interface de chat garde l’historique des conversations, ce qui vous permet d’avoir des interactions à plusieurs tours. Vous pouvez voir toutes les requêtes et réponses précédentes, facilitant le suivi de la conversation et la compréhension de la manière dont l’agent construit le contexte au fil des échanges.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/fr/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Conversation à plusieurs tours montrant des conversions simples, des recherches météo et des chaînes d’outils*

### Expérimentez avec différentes requêtes

Essayez différentes combinaisons :
- Recherches météo : « Quel temps fait-il à Tokyo ? »
- Conversions de température : « Que vaut 25°C en Kelvin ? »
- Requêtes combinées : « Vérifie le temps à Paris et dis-moi s’il fait plus de 20°C »

Observez comment l’agent interprète le langage naturel et le traduit en appels appropriés aux outils.

## Concepts Clés

### Modèle ReAct (Raisonnement et Action)

L’agent alterne entre le raisonnement (décider quoi faire) et l’action (utiliser les outils). Ce modèle permet une résolution autonome de problème plutôt que de simplement répondre à des instructions.

### Les descriptions des outils comptent

La qualité de vos descriptions d’outils influence directement la qualité de leur utilisation par l’agent. Des descriptions claires et spécifiques aident le modèle à comprendre quand et comment appeler chaque outil.

### Gestion de session

L’annotation `@MemoryId` permet une gestion automatique de la mémoire basée sur la session. Chaque ID de session obtient sa propre instance de `ChatMemory` gérée par le bean `ChatMemoryProvider`, permettant à plusieurs utilisateurs d’interagir avec l’agent simultanément sans mélanger leurs conversations. Le schéma suivant montre comment plusieurs utilisateurs sont dirigés vers des mémoires isolées en fonction de leur ID de session :

<img src="../../../translated_images/fr/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Chaque ID de session correspond à un historique de conversation isolé — les utilisateurs ne voient jamais les messages des autres.*

### Gestion des erreurs

Les outils peuvent échouer — les API peuvent expirer, des paramètres peuvent être invalides, des services externes peuvent tomber en panne. Les agents en production ont besoin de gestion des erreurs pour que le modèle puisse expliquer les problèmes ou essayer des alternatives au lieu de faire planter toute l’application. Lorsqu’un outil lance une exception, LangChain4j la capte et renvoie le message d’erreur au modèle, qui peut alors expliquer le problème en langage naturel.

## Outils Disponibles

Le diagramme ci-dessous montre l’écosystème large d’outils que vous pouvez construire. Ce module démontre les outils météo et de température, mais le même modèle `@Tool` fonctionne pour n’importe quelle méthode Java — des requêtes de base de données au traitement des paiements.

<img src="../../../translated_images/fr/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Toute méthode Java annotée avec @Tool devient disponible à l’IA — le modèle s’étend aux bases de données, APIs, e-mails, opérations sur fichiers, et plus.*

## Quand utiliser des agents basés sur des outils

Toutes les requêtes n’ont pas besoin d’outils. La décision dépend de si l’IA doit interagir avec des systèmes externes ou peut répondre avec ses propres connaissances. Le guide suivant résume quand les outils ajoutent de la valeur et quand ils sont inutiles :

<img src="../../../translated_images/fr/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*Un guide de décision rapide — les outils servent aux données en temps réel, calculs et actions ; les connaissances générales et tâches créatives n’en ont pas besoin.*

## Outils vs RAG

Les modules 03 et 04 étendent tous deux ce que l’IA peut faire, mais de manière fondamentalement différente. RAG donne au modèle accès à la **connaissance** en récupérant des documents. Les outils permettent au modèle de prendre des **actions** en appelant des fonctions. Le diagramme ci-dessous compare ces deux approches côte à côte — du fonctionnement du workflow aux compromis entre elles :

<img src="../../../translated_images/fr/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG récupère des informations à partir de documents statiques — Les outils exécutent des actions et récupèrent des données dynamiques en temps réel. Beaucoup de systèmes de production combinent les deux.*

En pratique, de nombreux systèmes de production combinent les deux approches : RAG pour ancrer les réponses dans votre documentation, et les outils pour récupérer des données en direct ou effectuer des opérations.

## Étapes Suivantes

**Module suivant :** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigation :** [← Précédent : Module 03 - RAG](../03-rag/README.md) | [Retour au principal](../README.md) | [Suivant : Module 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :  
Ce document a été traduit à l’aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforçons d’assurer l’exactitude, veuillez noter que les traductions automatiques peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue d’origine doit être considéré comme la source faisant foi. Pour des informations critiques, il est recommandé de recourir à une traduction humaine professionnelle. Nous déclinons toute responsabilité en cas de malentendus ou de mauvaises interprétations résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->