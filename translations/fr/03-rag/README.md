# Module 03 : RAG (Génération Augmentée par Recherche)

## Table des matières

- [Parcours vidéo](../../../03-rag)
- [Ce que vous apprendrez](../../../03-rag)
- [Prérequis](../../../03-rag)
- [Comprendre RAG](../../../03-rag)
  - [Quelle approche RAG ce tutoriel utilise-t-il ?](../../../03-rag)
- [Comment ça fonctionne](../../../03-rag)
  - [Traitement des documents](../../../03-rag)
  - [Création des embeddings](../../../03-rag)
  - [Recherche sémantique](../../../03-rag)
  - [Génération de réponse](../../../03-rag)
- [Lancer l'application](../../../03-rag)
- [Utiliser l'application](../../../03-rag)
  - [Télécharger un document](../../../03-rag)
  - [Poser des questions](../../../03-rag)
  - [Vérifier les références des sources](../../../03-rag)
  - [Expérimenter avec les questions](../../../03-rag)
- [Concepts clés](../../../03-rag)
  - [Stratégie de découpage](../../../03-rag)
  - [Scores de similarité](../../../03-rag)
  - [Stockage en mémoire](../../../03-rag)
  - [Gestion de la fenêtre de contexte](../../../03-rag)
- [Quand RAG est important](../../../03-rag)
- [Prochaines étapes](../../../03-rag)

## Parcours vidéo

Regardez cette session en direct qui explique comment démarrer avec ce module :

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG avec LangChain4j - Session en direct" width="800"/></a>

## Ce que vous apprendrez

Dans les modules précédents, vous avez appris à converser avec l'IA et à structurer vos invites de manière efficace. Mais il y a une limitation fondamentale : les modèles de langue ne connaissent que ce qu'ils ont appris lors de leur entraînement. Ils ne peuvent pas répondre à des questions sur les politiques de votre entreprise, votre documentation de projet, ou toute information sur laquelle ils n'ont pas été entraînés.

RAG (Génération Augmentée par Recherche) résout ce problème. Plutôt que d'essayer d'enseigner vos informations au modèle (ce qui est coûteux et peu pratique), vous lui donnez la capacité de chercher dans vos documents. Lorsqu'une question est posée, le système trouve des informations pertinentes et les inclut dans l'invite. Le modèle répond ensuite en se basant sur ce contexte récupéré.

Pensez à RAG comme donnant au modèle une bibliothèque de référence. Quand vous posez une question, le système :

1. **Question utilisateur** – Vous posez une question  
2. **Embedding** – Convertit votre question en vecteur  
3. **Recherche vectorielle** – Trouve des morceaux de documents similaires  
4. **Assemblage du contexte** – Ajoute les morceaux pertinents à l'invite  
5. **Réponse** – Le LLM génère une réponse basée sur le contexte  

Cela ancre les réponses du modèle dans vos données réelles plutôt que de se fier à ses connaissances d'entraînement ou d'inventer des réponses.

## Prérequis

- Avoir terminé le [Module 00 - Démarrage rapide](../00-quick-start/README.md) (pour l'exemple Easy RAG mentionné plus tard dans ce module)  
- Avoir terminé le [Module 01 - Introduction](../01-introduction/README.md) (ressources Azure OpenAI déployées, y compris le modèle d'embedding `text-embedding-3-small`)  
- Fichier `.env` dans le répertoire racine avec les identifiants Azure (créé par `azd up` dans le Module 01)  

> **Note :** Si vous n'avez pas terminé le Module 01, suivez d'abord les instructions de déploiement là-bas. La commande `azd up` déploie à la fois le modèle de chat GPT et le modèle d'embedding utilisé dans ce module.

## Comprendre RAG

Le schéma ci-dessous illustre le concept clé : au lieu de s'appuyer uniquement sur les données d'entraînement du modèle, RAG lui fournit une bibliothèque de référence de vos documents à consulter avant de générer chaque réponse.

<img src="../../../translated_images/fr/what-is-rag.1f9005d44b07f2d8.webp" alt="Qu'est-ce que RAG" width="800"/>

*Ce schéma montre la différence entre un LLM standard (qui devine à partir des données d'entraînement) et un LLM amélioré par RAG (qui consulte d'abord vos documents).*

Voici comment les éléments se connectent de bout en bout. La question d'un utilisateur passe par quatre étapes — embedding, recherche vectorielle, assemblage du contexte, et génération de réponse — chacune bâtie sur la précédente :

<img src="../../../translated_images/fr/rag-architecture.ccb53b71a6ce407f.webp" alt="Architecture RAG" width="800"/>

*Ce schéma montre la chaîne complète du pipeline RAG — une question utilisateur passe par embedding, recherche vectorielle, assemblage du contexte et génération de réponse.*

Le reste de ce module détaille chaque étape en détail, avec du code que vous pouvez exécuter et modifier.

### Quelle approche RAG ce tutoriel utilise-t-il ?

LangChain4j propose trois façons d'implémenter RAG, chacune avec un niveau d'abstraction différent. Le schéma ci-dessous les compare côte à côte :

<img src="../../../translated_images/fr/rag-approaches.5b97fdcc626f1447.webp" alt="Trois approches RAG dans LangChain4j" width="800"/>

*Ce schéma compare les trois approches LangChain4j RAG — Easy, Native, et Avancé — montrant leurs composants clés et quand les utiliser.*

| Approche | Ce qu'elle fait | Compromis |
|---|---|---|
| **Easy RAG** | Connecte automatiquement tout via `AiServices` et `ContentRetriever`. Vous annotez une interface, attachez un retriever, et LangChain4j gère l'embedding, la recherche, et l'assemblage d'invite en coulisses. | Code minimal, mais vous ne voyez pas ce qui se passe à chaque étape. |
| **Native RAG** | Vous appelez le modèle d'embedding, cherchez dans le magasin, construisez l'invite et générez la réponse vous-même — étape explicite par étape. | Plus de code, mais chaque étape est visible et modifiable. |
| **Advanced RAG** | Utilise le framework `RetrievalAugmentor` avec des transformeurs de requêtes, des routeurs, des re-classeurs, et des injecteurs de contenu modulaires pour des pipelines de production. | Flexibilité maximale, mais complexité nettement supérieure. |

**Ce tutoriel utilise l'approche Native.** Chaque étape du pipeline RAG — embedding de la requête, recherche dans le magasin vectoriel, assemblage du contexte, et génération de réponse — est explicitement écrite dans [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). C'est intentionnel : en tant que ressource pédagogique, il est plus important que vous voyiez et compreniez chaque étape plutôt que le code soit minimisé. Une fois à l'aise avec les pièces, vous pouvez passer à Easy RAG pour des prototypes rapides ou Advanced RAG pour des systèmes en production.

> **💡 Vous avez déjà vu Easy RAG en action ?** Le [module Démarrage Rapide](../00-quick-start/README.md) inclut un exemple Document Q&A ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) qui utilise l'approche Easy RAG — LangChain4j gère automatiquement l'embedding, la recherche, et l'assemblage de l'invite. Ce module fait un pas de plus en ouvrant ce pipeline pour que vous puissiez voir et contrôler chaque étape vous-même.

Le schéma ci-dessous montre le pipeline Easy RAG de cet exemple Démarrage Rapide. Notez comment `AiServices` et `EmbeddingStoreContentRetriever` cachent toute la complexité — vous chargez un document, attachez un retriever, et obtenez des réponses. L'approche Native dans ce module ouvre chacune de ces étapes cachées :

<img src="../../../translated_images/fr/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Pipeline Easy RAG - LangChain4j" width="800"/>

*Ce schéma montre le pipeline Easy RAG de `SimpleReaderDemo.java`. Comparez-le à l'approche Native utilisée ici : Easy RAG cache l'embedding, la récupération et l'assemblage de l'invite derrière `AiServices` et `ContentRetriever` — vous chargez un document, attachez un retriever, et obtenez des réponses. L'approche Native ouvre ce pipeline afin que vous appeliez chaque étape (embedder, chercher, assembler le contexte, générer) vous-même, vous donnant une visibilité et un contrôle complets.*

## Comment ça fonctionne

Le pipeline RAG dans ce module est divisé en quatre étapes qui s'exécutent en séquence à chaque fois qu'un utilisateur pose une question. D'abord, un document chargé est **analysé et découpé en morceaux** exploitables. Ces morceaux sont ensuite convertis en **embeddings vecteurs** et stockés pour être comparés mathématiquement. Lorsqu'une requête arrive, le système effectue une **recherche sémantique** pour trouver les morceaux les plus pertinents, puis les passe en contexte au LLM pour la **génération de réponse**. Les sections suivantes parcourent chaque étape avec le code réel et des diagrammes. Regardons la première étape.

### Traitement des documents

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Lorsque vous téléchargez un document, le système l'analyse (PDF ou texte brut), attache des métadonnées telles que le nom de fichier, puis le découpe en morceaux — des segments plus petits qui tiennent confortablement dans la fenêtre de contexte du modèle. Ces morceaux se chevauchent légèrement pour éviter de perdre du contexte aux limites.

```java
// Analyser le fichier téléchargé et l'encapsuler dans un Document LangChain4j
Document document = Document.from(content, metadata);

// Diviser en morceaux de 300 jetons avec un chevauchement de 30 jetons
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Le schéma ci-dessous montre comment cela fonctionne visuellement. Notez que chaque morceau partage certains tokens avec ses voisins — le chevauchement de 30 tokens garantit qu'aucun contexte important ne se perd entre les morceaux :

<img src="../../../translated_images/fr/document-chunking.a5df1dd1383431ed.webp" alt="Découpage du document" width="800"/>

*Ce schéma montre un document découpé en morceaux de 300 tokens avec un chevauchement de 30 tokens, préservant le contexte aux frontières des morceaux.*

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) et demandez :  
> - « Comment LangChain4j découpe-t-il les documents en morceaux et pourquoi le chevauchement est-il important ? »  
> - « Quelle est la taille optimale des morceaux pour différents types de documents et pourquoi ? »  
> - « Comment gérer des documents en plusieurs langues ou avec une mise en forme spéciale ? »

### Création des embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Chaque morceau est converti en une représentation numérique appelée embedding — essentiellement un convertisseur de sens en nombres. Le modèle d'embedding n'est pas "intelligent" comme un modèle de chat ; il ne peut pas suivre des instructions, raisonner ou répondre à des questions. Ce qu'il fait, c'est mapper le texte dans un espace mathématique où des sens similaires se retrouvent proches — « voiture » près de « automobile », « politique de remboursement » près de « retourner mon argent ». Pensez à un modèle de chat comme une personne à qui parler ; un modèle d'embedding est un système de classement ultra-efficace.

Le schéma ci-dessous visualise ce concept — du texte entre, des vecteurs numériques sortent, et des sens similaires produisent des vecteurs proches :

<img src="../../../translated_images/fr/embedding-model-concept.90760790c336a705.webp" alt="Concept du modèle d'embedding" width="800"/>

*Ce schéma montre comment un modèle d'embedding convertit le texte en vecteurs numériques, plaçant des sens similaires — comme « voiture » et « automobile » — proches dans l'espace vectoriel.*

```java
@Bean
public EmbeddingModel embeddingModel() {
    return OpenAiOfficialEmbeddingModel.builder()
        .baseUrl(azureOpenAiEndpoint)
        .apiKey(azureOpenAiKey)
        .modelName(azureEmbeddingDeploymentName)
        .build();
}

EmbeddingStore<TextSegment> embeddingStore = 
    new InMemoryEmbeddingStore<>();
```
  
Le diagramme de classes ci-dessous montre les deux flux séparés dans un pipeline RAG et les classes LangChain4j qui les implémentent. Le **flux d'ingestion** (exécuté une fois au moment du chargement) découpe le document, embedde les morceaux, et les stocke via `.addAll()`. Le **flux de requête** (exécuté à chaque question utilisateur) embedde la question, cherche dans le magasin via `.search()`, et passe le contexte trouvé au modèle de chat. Les deux flux se rejoignent sur l'interface partagée `EmbeddingStore<TextSegment>` :

<img src="../../../translated_images/fr/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="Classes LangChain4j RAG" width="800"/>

*Ce schéma montre les deux flux dans un pipeline RAG — ingestion et requête — et comment ils se connectent via un EmbeddingStore partagé.*

Une fois les embeddings stockés, les contenus similaires se regroupent naturellement dans l'espace vectoriel. La visualisation ci-dessous montre comment les documents sur des sujets liés forment des points proches, ce qui rend la recherche sémantique possible :

<img src="../../../translated_images/fr/vector-embeddings.2ef7bdddac79a327.webp" alt="Espace des embeddings vectoriels" width="800"/>

*Cette visualisation montre comment des documents liés se regroupent en 3D dans l'espace vectoriel, avec des sujets comme Documents Techniques, Règles d'Entreprise, et FAQ formant des regroupements distincts.*

Lorsqu'un utilisateur effectue une recherche, le système suit quatre étapes : embedder les documents une fois, embedder la requête à chaque recherche, comparer le vecteur de requête à tous les vecteurs stockés via la similarité cosinus, et retourner les meilleurs K morceaux. Le schéma ci-dessous parcourt chaque étape et les classes LangChain4j impliquées :

<img src="../../../translated_images/fr/embedding-search-steps.f54c907b3c5b4332.webp" alt="Étapes de recherche par embedding" width="800"/>

*Ce schéma montre le processus de recherche en quatre étapes : embedder les documents, embedder la requête, comparer les vecteurs avec la similarité cosinus, et retourner les meilleurs résultats.*

### Recherche sémantique

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Lorsque vous posez une question, votre question devient aussi un embedding. Le système compare l'embedding de votre question avec tous les embeddings des morceaux de documents. Il trouve les morceaux aux significations les plus similaires — pas seulement par correspondance de mots-clés, mais par similarité sémantique réelle.

```java
Embedding queryEmbedding = embeddingModel.embed(question).content();

EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
    .queryEmbedding(queryEmbedding)
    .maxResults(5)
    .minScore(0.5)
    .build();

EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);
List<EmbeddingMatch<TextSegment>> matches = searchResult.matches();

for (EmbeddingMatch<TextSegment> match : matches) {
    String relevantText = match.embedded().text();
    double score = match.score();
}
```
  
Le schéma ci-dessous contraste la recherche sémantique avec la recherche traditionnelle par mots-clés. Une recherche par mot-clé pour « véhicule » manque un morceau parlant de « voitures et camions », mais la recherche sémantique comprend qu'ils veulent dire la même chose et le retourne comme un résultat classé haut :

<img src="../../../translated_images/fr/semantic-search.6b790f21c86b849d.webp" alt="Recherche sémantique" width="800"/>

*Ce schéma compare la recherche basée sur mots-clés à la recherche sémantique, montrant comment la recherche sémantique récupère un contenu conceptuellement lié même lorsque les mots exacts diffèrent.*
Sous le capot, la similarité est mesurée en utilisant la similarité cosinus — se demandant essentiellement « ces deux flèches pointent-elles dans la même direction ? » Deux segments peuvent utiliser des mots complètement différents, mais s'ils signifient la même chose, leurs vecteurs pointent dans la même direction et obtiennent un score proche de 1.0 :

<img src="../../../translated_images/fr/cosine-similarity.9baeaf3fc3336abb.webp" alt="Similarité Cosinus" width="800"/>

*Ce diagramme illustre la similarité cosinus comme l’angle entre les vecteurs d’embeddding — des vecteurs plus alignés obtiennent un score proche de 1.0, indiquant une plus grande similarité sémantique.*

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) et demandez :
> - « Comment fonctionne la recherche par similarité avec les embeddings et qu’est-ce qui détermine le score ? »
> - « Quel seuil de similarité dois-je utiliser et comment cela influence-t-il les résultats ? »
> - « Comment gérer les cas où aucun document pertinent n’est trouvé ? »

### Génération de Réponse

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Les segments les plus pertinents sont assemblés dans un prompt structuré comprenant des instructions explicites, le contexte récupéré, et la question de l’utilisateur. Le modèle lit ces segments spécifiques et répond sur cette base — il ne peut utiliser que ce qui est devant lui, ce qui évite les hallucinations.

```java
String context = matches.stream()
    .map(match -> match.embedded().text())
    .collect(Collectors.joining("\n\n"));

String prompt = String.format("""
    Answer the question based on the following context.
    If the answer cannot be found in the context, say so.

    Context:
    %s

    Question: %s

    Answer:""", context, request.question());

String answer = chatModel.chat(prompt);
```

Le diagramme ci-dessous montre cette assemblée en action — les segments les mieux notés de l’étape de recherche sont injectés dans le modèle de prompt, et `OpenAiOfficialChatModel` génère une réponse fondée :

<img src="../../../translated_images/fr/context-assembly.7e6dd60c31f95978.webp" alt="Assemblage du Contexte" width="800"/>

*Ce diagramme montre comment les segments les mieux notés sont assemblés dans un prompt structuré, permettant au modèle de générer une réponse fondée à partir de vos données.*

## Lancer l’Application

**Vérifier le déploiement :**

Assurez-vous que le fichier `.env` existe dans le répertoire racine avec les identifiants Azure (créés pendant le Module 01). Exécutez cette commande depuis le répertoire du module (`03-rag/`) :

**Bash :**
```bash
cat ../.env  # Doit afficher AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell :**
```powershell
Get-Content ..\.env  # Devrait afficher AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Démarrer l’application :**

> **Note :** Si vous avez déjà lancé toutes les applications avec `./start-all.sh` depuis le répertoire racine (comme décrit dans le Module 01), ce module est déjà en fonctionnement sur le port 8081. Vous pouvez sauter les commandes de démarrage ci-dessous et aller directement sur http://localhost:8081.

**Option 1 : Utiliser Spring Boot Dashboard (recommandé pour les utilisateurs de VS Code)**

Le conteneur de développement inclut l’extension Spring Boot Dashboard, qui fournit une interface visuelle pour gérer toutes les applications Spring Boot. Vous pouvez la trouver dans la barre d’activités à gauche dans VS Code (cherchez l’icône Spring Boot).

Depuis le Spring Boot Dashboard, vous pouvez :
- Voir toutes les applications Spring Boot disponibles dans l’espace de travail
- Démarrer/arrêter les applications d’un simple clic
- Consulter les journaux en temps réel
- Surveiller l’état des applications

Cliquez simplement sur le bouton lecture à côté de « rag » pour démarrer ce module, ou lancez tous les modules en une fois.

<img src="../../../translated_images/fr/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Cette capture d’écran montre le Spring Boot Dashboard dans VS Code, où vous pouvez démarrer, arrêter et surveiller visuellement les applications.*

**Option 2 : Utiliser les scripts shell**

Lancez toutes les applications web (modules 01-04) :

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

Ou lancez uniquement ce module :

**Bash :**
```bash
cd 03-rag
./start.sh
```

**PowerShell :**
```powershell
cd 03-rag
.\start.ps1
```

Les deux scripts chargent automatiquement les variables d’environnement depuis le fichier `.env` racine et construiront les JARs s’ils n’existent pas.

> **Note :** Si vous préférez construire manuellement tous les modules avant de lancer :
>
> **Bash :**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **PowerShell :**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Ouvrez http://localhost:8081 dans votre navigateur.

**Pour arrêter :**

**Bash :**
```bash
./stop.sh  # Ce module seulement
# Ou
cd .. && ./stop-all.sh  # Tous les modules
```

**PowerShell :**
```powershell
.\stop.ps1  # Ce module seulement
# Ou
cd ..; .\stop-all.ps1  # Tous les modules
```

## Utiliser l’Application

L’application fournit une interface web pour le téléversement de documents et la pose de questions.

<a href="images/rag-homepage.png"><img src="../../../translated_images/fr/rag-homepage.d90eb5ce1b3caa94.webp" alt="Interface de l’Application RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Cette capture d’écran montre l’interface de l’application RAG où vous téléversez des documents et posez des questions.*

### Téléverser un Document

Commencez par téléverser un document - les fichiers TXT fonctionnent le mieux pour tester. Un `sample-document.txt` est fourni dans ce répertoire, contenant des informations sur les fonctionnalités de LangChain4j, la mise en œuvre RAG, et les bonnes pratiques — parfait pour tester le système.

Le système traite votre document, le divise en segments, et crée des embeddings pour chaque segment. Cela se fait automatiquement lors du téléversement.

### Poser des Questions

Posez maintenant des questions spécifiques sur le contenu du document. Essayez quelque chose de factuel et clairement indiqué dans le document. Le système recherche les segments pertinents, les inclut dans le prompt, et génère une réponse.

### Vérifier les Références Sources

Remarquez que chaque réponse inclut des références sources avec des scores de similarité. Ces scores (entre 0 et 1) montrent à quel point chaque segment était pertinent pour votre question. Des scores élevés signifient de meilleures correspondances. Cela vous permet de vérifier la réponse par rapport à la source.

<a href="images/rag-query-results.png"><img src="../../../translated_images/fr/rag-query-results.6d69fcec5397f355.webp" alt="Résultats de Requête RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Cette capture d’écran montre les résultats d’une requête avec la réponse générée, les références sources, et les scores de pertinence pour chaque segment récupéré.*

### Expérimentez avec les Questions

Essayez différents types de questions :
- Faits spécifiques : « Quel est le sujet principal ? »
- Comparaisons : « Quelle est la différence entre X et Y ? »
- Résumés : « Résumez les points clés sur Z »

Observez comment les scores de pertinence changent selon la qualité de la correspondance entre votre question et le contenu du document.

## Concepts Clés

### Stratégie de Chunking

Les documents sont découpés en segments de 300 tokens avec un chevauchement de 30 tokens. Ce compromis garantit que chaque segment contient suffisamment de contexte pour être significatif tout en restant suffisamment petit pour inclure plusieurs segments dans un prompt.

### Scores de Similarité

Chaque segment récupéré est accompagné d’un score de similarité entre 0 et 1 indiquant à quel point il correspond à la question de l’utilisateur. Le diagramme ci-dessous visualise les plages de score et la manière dont le système les utilise pour filtrer les résultats :

<img src="../../../translated_images/fr/similarity-scores.b0716aa911abf7f0.webp" alt="Scores de Similarité" width="800"/>

*Ce diagramme montre les plages de scores de 0 à 1, avec un seuil minimum de 0,5 qui filtre les segments non pertinents.*

Les scores varient de 0 à 1 :
- 0,7-1,0 : Très pertinent, correspondance exacte
- 0,5-0,7 : Pertinent, bon contexte
- En dessous de 0,5 : Filtré, trop dissemblable

Le système ne récupère que les segments au-dessus du seuil minimal pour garantir la qualité.

Les embeddings fonctionnent bien lorsque le sens est clairement clusterisé, mais ont des angles morts. Le diagramme ci-dessous montre les modes d’échec courants — les segments trop grands produisent des vecteurs flous, les segments trop petits manquent de contexte, les termes ambigus pointent vers plusieurs clusters, et les recherches par correspondance exacte (IDs, numéros de pièces) ne fonctionnent pas du tout avec les embeddings :

<img src="../../../translated_images/fr/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Modes d’Échec des Embeddings" width="800"/>

*Ce diagramme montre les modes d’échec communs des embeddings : segments trop grands, segments trop petits, termes ambigus dirigés vers plusieurs clusters, et recherches par correspondance exacte comme les IDs.*

### Stockage en Mémoire

Ce module utilise un stockage en mémoire pour la simplicité. Quand vous redémarrez l’application, les documents téléversés sont perdus. Les systèmes en production utilisent des bases de données vectorielles persistantes comme Qdrant ou Azure AI Search.

### Gestion de la Fenêtre de Contexte

Chaque modèle a une fenêtre de contexte maximale. Vous ne pouvez pas inclure tous les segments d’un grand document. Le système récupère les N segments les plus pertinents (par défaut 5) pour rester dans les limites tout en fournissant suffisamment de contexte pour des réponses précises.

## Quand le RAG est Important

Le RAG n’est pas toujours la bonne approche. Le guide de décision ci-dessous vous aide à déterminer quand le RAG apporte une valeur ajoutée versus quand des approches plus simples — comme inclure le contenu directement dans le prompt ou s’appuyer sur les connaissances intégrées du modèle — suffisent :

<img src="../../../translated_images/fr/when-to-use-rag.1016223f6fea26bc.webp" alt="Quand Utiliser le RAG" width="800"/>

*Ce diagramme montre un guide décisionnel pour savoir quand le RAG ajoute de la valeur par rapport à des approches plus simples.*

## Étapes Suivantes

**Module suivant :** [04-tools - Agents IA avec Outils](../04-tools/README.md)

---

**Navigation :** [← Précédent : Module 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Retour à l’Accueil](../README.md) | [Suivant : Module 04 - Outils →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :  
Ce document a été traduit à l’aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforcions d’assurer l’exactitude, veuillez noter que les traductions automatiques peuvent contenir des erreurs ou des imprécisions. Le document original dans sa langue d’origine doit être considéré comme la source faisant foi. Pour les informations critiques, il est recommandé de recourir à une traduction professionnelle effectuée par un humain. Nous déclinons toute responsabilité en cas de malentendus ou de mauvaises interprétations résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->