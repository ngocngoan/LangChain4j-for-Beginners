# Module 03 : RAG (Génération Augmentée par Recherche)

## Table des matières

- [Présentation vidéo](../../../03-rag)
- [Ce que vous apprendrez](../../../03-rag)
- [Prérequis](../../../03-rag)
- [Comprendre le RAG](../../../03-rag)
  - [Quelle approche RAG ce tutoriel utilise-t-il ?](../../../03-rag)
- [Comment ça marche](../../../03-rag)
  - [Traitement des documents](../../../03-rag)
  - [Création des embeddings](../../../03-rag)
  - [Recherche sémantique](../../../03-rag)
  - [Génération de réponses](../../../03-rag)
- [Exécuter l’application](../../../03-rag)
- [Utiliser l’application](../../../03-rag)
  - [Téléverser un document](../../../03-rag)
  - [Poser des questions](../../../03-rag)
  - [Vérifier les références sources](../../../03-rag)
  - [Expérimenter avec les questions](../../../03-rag)
- [Concepts clés](../../../03-rag)
  - [Stratégie de découpage](../../../03-rag)
  - [Scores de similarité](../../../03-rag)
  - [Stockage en mémoire](../../../03-rag)
  - [Gestion de la fenêtre de contexte](../../../03-rag)
- [Quand le RAG est important](../../../03-rag)
- [Étapes suivantes](../../../03-rag)

## Présentation vidéo

Regardez cette session en direct qui explique comment commencer avec ce module :

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## Ce que vous apprendrez

Dans les modules précédents, vous avez appris à converser avec l’IA et à structurer efficacement vos invites. Mais il y a une limitation fondamentale : les modèles de langage ne savent que ce qu’ils ont appris pendant leur entraînement. Ils ne peuvent pas répondre aux questions sur les politiques de votre entreprise, la documentation de vos projets ou toute information qu’ils n’ont pas apprise.

Le RAG (Génération Augmentée par Recherche) résout ce problème. Au lieu d’essayer d’enseigner vos informations au modèle (ce qui est coûteux et peu pratique), vous lui donnez la capacité de chercher dans vos documents. Lorsqu’une question est posée, le système trouve les informations pertinentes et les inclut dans l’invite. Le modèle répond alors en se basant sur ce contexte récupéré.

Pensez au RAG comme à une bibliothèque de référence pour le modèle. Lorsque vous posez une question, le système :

1. **Requête utilisateur** — Vous posez une question  
2. **Embedding** — Convertit votre question en vecteur  
3. **Recherche vectorielle** — Trouve des morceaux de documents similaires  
4. **Assemblage du contexte** — Ajoute des morceaux pertinents à l’invite  
5. **Réponse** — Le LLM génère une réponse basée sur le contexte  

Cela ancre les réponses du modèle dans vos données réelles au lieu de se fier uniquement à ses connaissances d’entraînement ou d’inventer des réponses.

## Prérequis

- Avoir complété le [Module 00 - Démarrage rapide](../00-quick-start/README.md) (pour l'exemple Easy RAG cité ci-dessus)  
- Avoir complété le [Module 01 - Introduction](../01-introduction/README.md) (ressources Azure OpenAI déployées, incluant le modèle d’embedding `text-embedding-3-small`)  
- Fichier `.env` à la racine avec les identifiants Azure (créé via `azd up` dans le Module 01)  

> **Note :** Si vous n'avez pas encore complété le Module 01, suivez d'abord les instructions de déploiement qui s’y trouvent. La commande `azd up` déploie à la fois le modèle GPT chat et le modèle d’embedding utilisé dans ce module.

## Comprendre le RAG

Le schéma ci-dessous illustre le concept clé : au lieu de s’appuyer uniquement sur les données d’entraînement du modèle, le RAG lui fournit une bibliothèque de référence de vos documents à consulter avant de générer chaque réponse.

<img src="../../../translated_images/fr/what-is-rag.1f9005d44b07f2d8.webp" alt="Qu’est-ce que le RAG" width="800"/>

*Ce diagramme montre la différence entre un LLM standard (qui devine d’après les données d’entraînement) et un LLM amélioré par RAG (qui consulte d’abord vos documents).*

Voici comment les éléments s’enchaînent de bout en bout. La question de l’utilisateur traverse quatre étapes : embedding, recherche vectorielle, assemblage du contexte et génération de réponse — chaque étape reposant sur la précédente :

<img src="../../../translated_images/fr/rag-architecture.ccb53b71a6ce407f.webp" alt="Architecture RAG" width="800"/>

*Ce diagramme montre la pipeline complète du RAG — une question utilisateur passe par l’embeddding, la recherche vectorielle, l’assemblage du contexte et la génération de réponse.*

Le reste du module détaille chaque étape, avec du code que vous pouvez exécuter et modifier.

### Quelle approche RAG ce tutoriel utilise-t-il ?

LangChain4j propose trois façons d’implémenter le RAG, avec différents niveaux d’abstraction. Le schéma ci-dessous les compare côte à côte :

<img src="../../../translated_images/fr/rag-approaches.5b97fdcc626f1447.webp" alt="Trois approches RAG dans LangChain4j" width="800"/>

*Ce diagramme compare les trois approches LangChain4j pour le RAG — Easy, Native et Advanced — montrant leurs composants clés et quand utiliser chacune.*

| Approche | Ce qu’elle fait | Compromis |
|---|---|---|
| **Easy RAG** | Connecte automatiquement tout via `AiServices` et `ContentRetriever`. Vous annotez une interface, attachez un retriever, et LangChain4j gère embeddding, recherche et assemblage d’invites en coulisse. | Peu de code, mais vous ne voyez pas les étapes en détail. |
| **Native RAG** | Vous appelez explicitement le modèle d’embedding, recherchez dans le store, construisez l’invite et générez la réponse — étape par étape. | Plus de code, mais chaque étape est visible et modifiable. |
| **Advanced RAG** | Utilise le framework `RetrievalAugmentor` avec des transformateurs de requêtes, routeurs, re-classeurs et injecteurs de contenu modulaires pour des pipelines en production. | Flexibilité maximale, mais complexité nettement plus élevée. |

**Ce tutoriel utilise l’approche Native.** Chaque étape de la pipeline RAG — encodage de la requête, recherche dans le store vectoriel, assemblage du contexte et génération de la réponse — est explicitement codée dans [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). C’est intentionnel : en tant que ressource pédagogique, il est plus important que vous voyiez et compreniez chaque étape que d’avoir un code minimaliste. Une fois que vous êtes à l’aise avec la structure, vous pouvez passer à Easy RAG pour des prototypes rapides ou Advanced RAG pour des systèmes en production.

> **💡 Vous avez déjà vu Easy RAG en action ?** Le [module Démarrage rapide](../00-quick-start/README.md) inclut un exemple Q&R sur documents ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) qui utilise l’approche Easy RAG — LangChain4j gère automatiquement embedding, recherche et assemblage d’invites. Ce module va plus loin en ouvrant cette pipeline pour que vous puissiez voir et contrôler chaque étape vous-même.

<img src="../../../translated_images/fr/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Pipeline Easy RAG - LangChain4j" width="800"/>

*Ce diagramme montre la pipeline Easy RAG issue de `SimpleReaderDemo.java`. Comparez avec l’approche Native utilisée dans ce module : Easy RAG cache l’embeddding, la récupération et l’assemblage des invites derrière `AiServices` et `ContentRetriever` — vous chargez un document, attachez un retriever et obtenez les réponses. L’approche Native ici ouvre cette pipeline afin que vous appeliez chaque étape (embed, recherche, assemblage de contexte, génération) vous-même, vous donnant visibilité complète et contrôle.*

## Comment ça marche

La pipeline RAG de ce module se divise en quatre étapes exécutées en séquence à chaque question utilisateur. D’abord, un document téléversé est **parsé et découpé** en morceaux gérables. Ces morceaux sont ensuite convertis en **embeddings vectoriels** et stockés afin de permettre des comparaisons mathématiques. Lorsque la requête arrive, le système effectue une **recherche sémantique** pour trouver les morceaux les plus pertinents, puis les passe comme contexte au LLM pour la **génération de réponse**. Les sections ci-dessous détaillent chaque étape avec le code réel et des diagrammes. Commençons par la première étape.

### Traitement des documents

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Lorsque vous téléversez un document, le système le parse (PDF ou texte brut), ajoute des métadonnées comme le nom de fichier, puis le découpe en morceaux — des parties plus petites qui tiennent confortablement dans la fenêtre de contexte du modèle. Ces morceaux se chevauchent légèrement pour ne pas perdre de contexte aux frontières.

```java
// Analyser le fichier téléchargé et l'encapsuler dans un Document LangChain4j
Document document = Document.from(content, metadata);

// Diviser en morceaux de 300 tokens avec un chevauchement de 30 tokens
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Le diagramme ci-dessous montre visuellement comment cela fonctionne. Notez que chaque morceau partage certains tokens avec ses voisins — le chevauchement de 30 tokens garantit qu’aucun contexte important ne tombe entre les mailles du filet :

<img src="../../../translated_images/fr/document-chunking.a5df1dd1383431ed.webp" alt="Découpage de document" width="800"/>

*Ce diagramme montre un document divisé en morceaux de 300 tokens avec un chevauchement de 30 tokens, préservant le contexte aux limites.*

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) et demandez :  
> - « Comment LangChain4j découpe-t-il les documents en morceaux et pourquoi le chevauchement est-il important ? »  
> - « Quelle est la taille optimale des morceaux selon les types de documents et pourquoi ? »  
> - « Comment gérer des documents multilingues ou avec une mise en forme particulière ? »

### Création des embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Chaque morceau est converti en une représentation numérique appelée embedding — essentiellement un convertisseur de sens en nombres. Le modèle d’embedding n’est pas « intelligent » comme un modèle de chat ; il ne peut pas suivre des instructions, raisonner ou répondre à des questions. Ce qu’il peut faire, c’est mapper du texte dans un espace mathématique où des sens similaires se retrouvent proches — « voiture » près de « automobile », « politique de remboursement » près de « rendre mon argent ». Pensez au modèle de chat comme à une personne avec qui vous parlez ; un modèle d’embedding est un système de classement ultra-efficace.

<img src="../../../translated_images/fr/embedding-model-concept.90760790c336a705.webp" alt="Concept de modèle d’embedding" width="800"/>

*Ce diagramme montre comment un modèle d’embedding convertit du texte en vecteurs numériques, plaçant des significations similaires — comme « voiture » et « automobile » — proches dans l’espace vectoriel.*

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
  
Le diagramme de classes ci-dessous montre les deux flux séparés dans une pipeline RAG et les classes LangChain4j qui les implémentent. Le **flux ingestion** (exécuté une fois au téléversement) découpe le document, embedde les morceaux, et les stocke via `.addAll()`. Le **flux requête** (exécuté à chaque question utilisateur) embedde la question, recherche dans le magasin via `.search()`, et passe le contexte trouvé au modèle de chat. Les deux flux se rejoignent via l’interface partagée `EmbeddingStore<TextSegment>` :

<img src="../../../translated_images/fr/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="Classes LangChain4j pour RAG" width="800"/>

*Ce diagramme montre les deux flux dans une pipeline RAG — ingestion et requête — et leur connexion via un EmbeddingStore partagé.*

Une fois les embeddings stockés, les contenus similaires se regroupent naturellement dans l’espace vectoriel. La visualisation ci-dessous montre comment les documents sur des sujets apparentés se retrouvent proches, ce qui rend la recherche sémantique possible :

<img src="../../../translated_images/fr/vector-embeddings.2ef7bdddac79a327.webp" alt="Espace des embeddings vectoriels" width="800"/>

*Cette visualisation montre comment des documents connexes se regroupent dans un espace vectoriel 3D, avec des sujets comme documentation technique, règles métier et FAQ formant des groupes distincts.*

Quand un utilisateur recherche, le système suit quatre étapes : embedder les documents une fois, embedder la requête à chaque recherche, comparer le vecteur de la requête à tous les vecteurs stockés via la similarité cosinus, et retourner les K meilleurs morceaux. Le diagramme ci-dessous détaille chaque étape ainsi que les classes LangChain4j impliquées :

<img src="../../../translated_images/fr/embedding-search-steps.f54c907b3c5b4332.webp" alt="Étapes de la recherche par embedding" width="800"/>

*Ce diagramme montre le processus en quatre étapes de la recherche par embedding : embedder les documents, embedder la requête, comparer les vecteurs via similarité cosinus, et retourner les top-K résultats.*

### Recherche sémantique

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Quand vous posez une question, celle-ci devient aussi un embedding. Le système compare l’embedding de votre question à tous les embeddings des morceaux de documents. Il trouve les morceaux ayant les significations les plus proches — pas seulement des correspondances de mots-clés, mais une similarité sémantique réelle.

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
  
Le diagramme ci-dessous compare la recherche sémantique à la recherche traditionnelle par mots-clés. Une recherche par mot-clé pour « véhicule » ne trouve pas un morceau sur « voitures et camions », mais la recherche sémantique comprend qu’ils veulent dire la même chose et le retourne comme correspondance forte :

<img src="../../../translated_images/fr/semantic-search.6b790f21c86b849d.webp" alt="Recherche sémantique" width="800"/>

*Ce diagramme compare les recherches par mots-clés et sémantiques, montrant comment la recherche sémantique récupère un contenu conceptuellement lié même si les mots-clés exacts diffèrent.*

En interne, la similarité est mesurée avec la similarité cosinus — qui revient à demander « est-ce que ces deux flèches pointent dans la même direction ? » Deux morceaux peuvent utiliser des mots complètement différents, mais s’ils veulent dire la même chose, leurs vecteurs pointent dans la même direction et obtiennent un score proche de 1.0 :

<img src="../../../translated_images/fr/cosine-similarity.9baeaf3fc3336abb.webp" alt="Similarité cosinus" width="800"/>
*Ce diagramme illustre la similarité cosinus comme l'angle entre les vecteurs d'embedding — des vecteurs plus alignés obtiennent un score proche de 1.0, indiquant une plus grande similarité sémantique.*

> **🤖 Essayez avec [GitHub Copilot](https://github.com/features/copilot) Chat :** Ouvrez [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) et demandez :
> - « Comment fonctionne la recherche de similarité avec les embeddings et qu'est-ce qui détermine le score ? »
> - « Quel seuil de similarité devrais-je utiliser et comment cela affecte-t-il les résultats ? »
> - « Comment gérer les cas où aucun document pertinent n'est trouvé ? »

### Génération de Réponse

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Les segments les plus pertinents sont assemblés dans une invite structurée qui inclut des instructions explicites, le contexte récupéré, et la question de l'utilisateur. Le modèle lit ces segments spécifiques et répond en fonction de cette information — il ne peut utiliser que ce qui est devant lui, ce qui évite les hallucinations.

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

Le diagramme ci-dessous montre cet assemblage en action — les segments les mieux notés issus de l'étape de recherche sont injectés dans le modèle d'invite, et le `OpenAiOfficialChatModel` génère une réponse fondée :

<img src="../../../translated_images/fr/context-assembly.7e6dd60c31f95978.webp" alt="Assemblage du Contexte" width="800"/>

*Ce diagramme montre comment les segments les mieux notés sont assemblés dans une invite structurée, permettant au modèle de générer une réponse fondée à partir de vos données.*

## Exécuter l'Application

**Vérifiez le déploiement :**

Assurez-vous que le fichier `.env` existe dans le répertoire racine avec les identifiants Azure (créés pendant le Module 01) :

**Bash :**
```bash
cat ../.env  # Devrait afficher AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell :**
```powershell
Get-Content ..\.env  # Devrait afficher AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOIEMENT
```

**Démarrez l'application :**

> **Note :** Si vous avez déjà démarré toutes les applications avec `./start-all.sh` du Module 01, ce module est déjà en cours d'exécution sur le port 8081. Vous pouvez ignorer les commandes de démarrage ci-dessous et aller directement à http://localhost:8081.

**Option 1 : Utiliser le Spring Boot Dashboard (recommandé pour les utilisateurs VS Code)**

Le conteneur de développement inclut l'extension Spring Boot Dashboard, qui offre une interface visuelle pour gérer toutes les applications Spring Boot. Vous pouvez la trouver dans la barre d'activité sur le côté gauche de VS Code (cherchez l'icône Spring Boot).

Depuis le Spring Boot Dashboard, vous pouvez :
- Voir toutes les applications Spring Boot disponibles dans l'espace de travail
- Démarrer/arrêter les applications d'un simple clic
- Consulter les journaux d'application en temps réel
- Surveiller l'état des applications

Cliquez simplement sur le bouton lecture à côté de "rag" pour démarrer ce module, ou démarrez tous les modules à la fois.

<img src="../../../translated_images/fr/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Cette capture d'écran montre le Spring Boot Dashboard dans VS Code, où vous pouvez démarrer, arrêter et surveiller les applications visuellement.*

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

Ou démarrez uniquement ce module :

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

Les deux scripts chargent automatiquement les variables d'environnement depuis le fichier `.env` racine et construiront les JARs s'ils n'existent pas.

> **Note :** Si vous préférez construire manuellement tous les modules avant de démarrer :
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

## Utilisation de l'Application

L'application fournit une interface web pour le téléchargement de documents et la pose de questions.

<a href="images/rag-homepage.png"><img src="../../../translated_images/fr/rag-homepage.d90eb5ce1b3caa94.webp" alt="Interface de l'Application RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Cette capture d'écran montre l'interface de l'application RAG où vous téléchargez des documents et posez des questions.*

### Télécharger un Document

Commencez par télécharger un document - les fichiers TXT fonctionnent le mieux pour les tests. Un fichier `sample-document.txt` est fourni dans ce répertoire, contenant des informations sur les fonctionnalités de LangChain4j, l'implémentation RAG, et les bonnes pratiques - parfait pour tester le système.

Le système traite votre document, le découpe en segments, et crée des embeddings pour chaque segment. Cela se produit automatiquement lors du téléchargement.

### Poser des Questions

Posez maintenant des questions spécifiques sur le contenu du document. Essayez quelque chose de factuel clairement indiqué dans le document. Le système recherche des segments pertinents, les intègre dans l'invite, et génère une réponse.

### Vérifier les Références Sources

Notez que chaque réponse inclut des références sources avec des scores de similarité. Ces scores (de 0 à 1) montrent à quel point chaque segment était pertinent pour votre question. Des scores plus élevés signifient de meilleures correspondances. Cela vous permet de vérifier la réponse avec le matériel source.

<a href="images/rag-query-results.png"><img src="../../../translated_images/fr/rag-query-results.6d69fcec5397f355.webp" alt="Résultats de Requête RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Cette capture d'écran montre les résultats d'une requête avec la réponse générée, les références sources, et les scores de pertinence pour chaque segment récupéré.*

### Expérimentez avec les Questions

Essayez différents types de questions :
- Faits spécifiques : « Quel est le sujet principal ? »
- Comparaisons : « Quelle est la différence entre X et Y ? »
- Résumés : « Résumez les points clés concernant Z »

Observez comment les scores de pertinence changent selon le degré de correspondance de votre question avec le contenu du document.

## Concepts Clés

### Stratégie de Découpage

Les documents sont divisés en segments de 300 tokens avec un chevauchement de 30 tokens. Cet équilibre garantit que chaque segment dispose d'un contexte suffisant pour être significatif tout en restant assez petit pour inclure plusieurs segments dans une invite.

### Scores de Similarité

Chaque segment récupéré est accompagné d'un score de similarité compris entre 0 et 1 indiquant la proximité avec la question de l'utilisateur. Le diagramme ci-dessous visualise les plages de scores et comment le système les utilise pour filtrer les résultats :

<img src="../../../translated_images/fr/similarity-scores.b0716aa911abf7f0.webp" alt="Scores de Similarité" width="800"/>

*Ce diagramme montre les plages de scores de 0 à 1, avec un seuil minimum de 0.5 qui filtre les segments non pertinents.*

Les scores vont de 0 à 1 :
- 0.7-1.0 : Très pertinent, correspondance exacte
- 0.5-0.7 : Pertinent, bon contexte
- En dessous de 0.5 : Filtré, trop dissemblable

Le système ne récupère que les segments au-dessus du seuil minimum pour garantir la qualité.

Les embeddings fonctionnent bien quand le sens se regroupe clairement, mais ils ont des zones d’ombre. Le diagramme ci-dessous montre les modes d’échec courants — des segments trop gros produisent des vecteurs confus, des segments trop petits manquent de contexte, des termes ambigus renvoient à plusieurs clusters, et les recherches par correspondance exacte (IDs, numéros de pièce) ne fonctionnent pas du tout avec les embeddings :

<img src="../../../translated_images/fr/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Modes d'Échec des Embeddings" width="800"/>

*Ce diagramme montre les modes d’échec courants des embeddings : segments trop gros, segments trop petits, termes ambigus pointant vers plusieurs clusters, et recherches par correspondance exacte comme les IDs.*

### Stockage en Mémoire

Ce module utilise un stockage en mémoire pour plus de simplicité. Quand vous redémarrez l'application, les documents téléchargés sont perdus. Les systèmes en production utilisent des bases de données vectorielles persistantes comme Qdrant ou Azure AI Search.

### Gestion de la Fenêtre de Contexte

Chaque modèle a une fenêtre de contexte maximale. Vous ne pouvez pas inclure tous les segments d’un grand document. Le système récupère les N segments les plus pertinents (par défaut 5) pour rester dans les limites tout en fournissant suffisamment de contexte pour des réponses précises.

## Quand RAG est Important

RAG n'est pas toujours la bonne approche. Le guide de décision ci-dessous vous aide à déterminer quand RAG apporte de la valeur versus quand des approches plus simples — comme inclure directement le contenu dans l'invite ou s'appuyer sur la connaissance intégrée du modèle — suffisent :

<img src="../../../translated_images/fr/when-to-use-rag.1016223f6fea26bc.webp" alt="Quand utiliser RAG" width="800"/>

*Ce diagramme montre un guide de décision pour quand RAG apporte de la valeur versus quand des approches plus simples suffisent.*

**Utilisez RAG lorsque :**
- Vous répondez à des questions sur des documents propriétaires
- L'information change fréquemment (politiques, prix, spécifications)
- La précision nécessite une attribution des sources
- Le contenu est trop volumineux pour tenir dans une seule invite
- Vous avez besoin de réponses vérifiables et fondées

**N’utilisez pas RAG lorsque :**
- Les questions nécessitent des connaissances générales déjà présentes dans le modèle
- Vous avez besoin de données en temps réel (RAG fonctionne sur des documents téléchargés)
- Le contenu est assez petit pour être inclus directement dans les invites

## Étapes Suivantes

**Module suivant :** [04-tools - Agents IA avec Outils](../04-tools/README.md)

---

**Navigation :** [← Précédent : Module 02 - Ingénierie des invites](../02-prompt-engineering/README.md) | [Retour au principal](../README.md) | [Suivant : Module 04 - Outils →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :  
Ce document a été traduit à l’aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforcions d’assurer l’exactitude, veuillez noter que les traductions automatisées peuvent comporter des erreurs ou des inexactitudes. Le document original dans sa langue native doit être considéré comme la source faisant foi. Pour les informations cruciales, il est recommandé de faire appel à une traduction professionnelle humaine. Nous déclinons toute responsabilité en cas de malentendus ou d’interprétations erronées résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->