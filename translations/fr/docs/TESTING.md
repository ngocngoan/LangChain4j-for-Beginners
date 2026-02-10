# Tester les applications LangChain4j

## Table des matières

- [Démarrage rapide](../../../docs)
- [Ce que couvrent les tests](../../../docs)
- [Exécution des tests](../../../docs)
- [Exécution des tests dans VS Code](../../../docs)
- [Modèles de tests](../../../docs)
- [Philosophie de test](../../../docs)
- [Étapes suivantes](../../../docs)

Ce guide vous guide à travers les tests qui démontrent comment tester des applications IA sans nécessiter de clés API ou de services externes.

## Démarrage rapide

Exécutez tous les tests avec une seule commande :

**Bash :**
```bash
mvn test
```

**PowerShell :**
```powershell
mvn --% test
```

<img src="../../../translated_images/fr/test-results.ea5c98d8f3642043.webp" alt="Résultats de tests réussis" width="800"/>

*Exécution réussie des tests montrant que tous les tests passent sans aucune erreur*

## Ce que couvrent les tests

Ce cours se concentre sur les **tests unitaires** qui s'exécutent localement. Chaque test démontre un concept spécifique de LangChain4j en isolation.

<img src="../../../translated_images/fr/testing-pyramid.2dd1079a0481e53e.webp" alt="Pyramide de test" width="800"/>

*Pyramide de test montrant l'équilibre entre tests unitaires (rapides, isolés), tests d'intégration (composants réels) et tests de bout en bout. Cette formation couvre les tests unitaires.*

| Module | Tests | Focus | Fichiers clés |
|--------|-------|-------|---------------|
| **00 - Démarrage rapide** | 6 | Modèles de prompt et substitution de variables | `SimpleQuickStartTest.java` |
| **01 - Introduction** | 8 | Mémoire de conversation et chat avec état | `SimpleConversationTest.java` |
| **02 - Ingénierie des prompts** | 12 | Modèles GPT-5.2, niveaux d'empressement, sortie structurée | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Ingestion de documents, embeddings, recherche par similarité | `DocumentServiceTest.java` |
| **04 - Outils** | 12 | Appel de fonctions et chaînage d'outils | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Protocole de contexte du modèle avec transport Stdio | `SimpleMcpTest.java` |

## Exécution des tests

**Exécuter tous les tests depuis la racine :**

**Bash :**
```bash
mvn test
```

**PowerShell :**
```powershell
mvn --% test
```

**Exécuter les tests pour un module spécifique :**

**Bash :**
```bash
cd 01-introduction && mvn test
# Ou depuis root
mvn test -pl 01-introduction
```

**PowerShell :**
```powershell
cd 01-introduction; mvn --% test
# Ou depuis root
mvn --% test -pl 01-introduction
```

**Exécuter une seule classe de test :**

**Bash :**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell :**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Exécuter une méthode de test spécifique :**

**Bash :**
```bash
mvn test -Dtest=SimpleConversationTest#doitMaintenirLhistoriqueDeConversation
```

**PowerShell :**
```powershell
mvn --% test -Dtest=SimpleConversationTest#doitMaintenirLHistoriqueDeConversation
```

## Exécution des tests dans VS Code

Si vous utilisez Visual Studio Code, l’Explorateur de tests offre une interface graphique pour exécuter et déboguer les tests.

<img src="../../../translated_images/fr/vscode-testing.f02dd5917289dced.webp" alt="Explorateur de tests VS Code" width="800"/>

*Explorateur de tests VS Code montrant l’arborescence des tests avec toutes les classes de test Java et les méthodes de test individuelles*

**Pour exécuter des tests dans VS Code :**

1. Ouvrez l’Explorateur de tests en cliquant sur l’icône du bécher dans la barre d’activité
2. Développez l’arborescence des tests pour voir tous les modules et classes de test
3. Cliquez sur le bouton lecture à côté de n’importe quel test pour l’exécuter individuellement
4. Cliquez sur « Exécuter tous les tests » pour lancer toute la suite
5. Cliquez-droit sur un test et sélectionnez « Déboguer le test » pour définir des points d’arrêt et faire de l’étape à pas

L’Explorateur de tests affiche des coches vertes pour les tests réussis et fournit des messages d’échec détaillés en cas d’échec.

## Modèles de tests

### Modèle 1 : tester les modèles de prompt

Le modèle le plus simple teste les modèles de prompt sans appeler aucun modèle IA. Vous vérifiez que la substitution des variables fonctionne correctement et que les prompts sont formatés comme attendu.

<img src="../../../translated_images/fr/prompt-template-testing.b902758ddccc8dee.webp" alt="Test des modèles de prompt" width="800"/>

*Test des modèles de prompt montrant le flux de substitution des variables : modèle avec espaces réservés → valeurs appliquées → sortie formatée vérifiée*

```java
@Test
@DisplayName("Should format prompt template with variables")
void testPromptTemplateFormatting() {
    PromptTemplate template = PromptTemplate.from(
        "Best time to visit {{destination}} for {{activity}}?"
    );
    
    Prompt prompt = template.apply(Map.of(
        "destination", "Paris",
        "activity", "sightseeing"
    ));
    
    assertThat(prompt.text()).isEqualTo("Best time to visit Paris for sightseeing?");
}
```

Ce test se trouve dans `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Exécutez-le :**

**Bash :**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testFormatageDuModèleDeInvite
```

**PowerShell :**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#test de mise en forme du modèle d'invite
```

### Modèle 2 : simuler les modèles de langage

Pour tester la logique de conversation, utilisez Mockito pour créer des modèles factices qui retournent des réponses prédéterminées. Cela rend les tests rapides, gratuits et déterministes.

<img src="../../../translated_images/fr/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Comparaison Mock vs Réel API" width="800"/>

*Comparaison montrant pourquoi les mocks sont préférés pour les tests : ils sont rapides, gratuits, déterministes, et ne nécessitent pas de clés API*

```java
@ExtendWith(MockitoExtension.class)
class SimpleConversationTest {
    
    private ConversationService conversationService;
    
    @Mock
    private OpenAiOfficialChatModel mockChatModel;
    
    @BeforeEach
    void setUp() {
        ChatResponse mockResponse = ChatResponse.builder()
            .aiMessage(AiMessage.from("This is a test response"))
            .build();
        when(mockChatModel.chat(anyList())).thenReturn(mockResponse);
        
        conversationService = new ConversationService(mockChatModel);
    }
    
    @Test
    void shouldMaintainConversationHistory() {
        String conversationId = conversationService.startConversation();
        
        ChatResponse mockResponse1 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 1"))
            .build();
        ChatResponse mockResponse2 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 2"))
            .build();
        ChatResponse mockResponse3 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 3"))
            .build();
        
        when(mockChatModel.chat(anyList()))
            .thenReturn(mockResponse1)
            .thenReturn(mockResponse2)
            .thenReturn(mockResponse3);

        conversationService.chat(conversationId, "First message");
        conversationService.chat(conversationId, "Second message");
        conversationService.chat(conversationId, "Third message");

        List<ChatMessage> history = conversationService.getHistory(conversationId);
        assertThat(history).hasSize(6); // 3 messages utilisateur + 3 messages IA
    }
}
```

Ce modèle apparaît dans `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Le mock assure un comportement cohérent afin que vous puissiez vérifier que la gestion de mémoire fonctionne correctement.

### Modèle 3 : tester l’isolation des conversations

La mémoire de conversation doit maintenir plusieurs utilisateurs séparés. Ce test vérifie que les conversations ne mélangent pas les contextes.

<img src="../../../translated_images/fr/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Isolation des conversations" width="800"/>

*Test d’isolation des conversations montrant des mémoires séparées pour différents utilisateurs afin d’éviter le mélange de contexte*

```java
@Test
void shouldIsolateConversationsByid() {
    String conv1 = conversationService.startConversation();
    String conv2 = conversationService.startConversation();
    
    ChatResponse mockResponse = ChatResponse.builder()
        .aiMessage(AiMessage.from("Response"))
        .build();
    when(mockChatModel.chat(anyList())).thenReturn(mockResponse);

    conversationService.chat(conv1, "Message for conversation 1");
    conversationService.chat(conv2, "Message for conversation 2");

    List<ChatMessage> history1 = conversationService.getHistory(conv1);
    List<ChatMessage> history2 = conversationService.getHistory(conv2);
    
    assertThat(history1).hasSize(2);
    assertThat(history2).hasSize(2);
}
```

Chaque conversation conserve son propre historique indépendant. Dans les systèmes en production, cette isolation est cruciale pour les applications multi-utilisateurs.

### Modèle 4 : tester les outils indépendamment

Les outils sont des fonctions que l’IA peut appeler. Testez-les directement pour vous assurer qu’ils fonctionnent correctement indépendamment des décisions IA.

<img src="../../../translated_images/fr/tools-testing.3e1706817b0b3924.webp" alt="Test des outils" width="800"/>

*Test indépendant des outils montrant l’exécution d’un outil mock sans appels IA pour vérifier la logique métier*

```java
@Test
void shouldConvertCelsiusToFahrenheit() {
    TemperatureTool tempTool = new TemperatureTool();
    String result = tempTool.celsiusToFahrenheit(25.0);
    assertThat(result).containsPattern("77[.,]0°F");
}

@Test
void shouldDemonstrateToolChaining() {
    WeatherTool weatherTool = new WeatherTool();
    TemperatureTool tempTool = new TemperatureTool();

    String weatherResult = weatherTool.getCurrentWeather("Seattle");
    assertThat(weatherResult).containsPattern("\\d+°C");

    String conversionResult = tempTool.celsiusToFahrenheit(22.0);
    assertThat(conversionResult).containsPattern("71[.,]6°F");
}
```

Ces tests de `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` valident la logique des outils sans intervention IA. L’exemple de chaînage montre comment la sortie d’un outil alimente l’entrée d’un autre.

### Modèle 5 : test RAG en mémoire

Les systèmes RAG nécessitent traditionnellement des bases de données vectorielles et des services d’embedding. Le modèle en mémoire vous permet de tester toute la chaîne sans dépendances externes.

<img src="../../../translated_images/fr/rag-testing.ee7541b1e23934b1.webp" alt="Test RAG en mémoire" width="800"/>

*Flux de test RAG en mémoire montrant l’analyse de documents, le stockage d’embeddings et la recherche par similarité sans base de données*

```java
@Test
void testProcessTextDocument() {
    String content = "This is a test document.\nIt has multiple lines.";
    InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    
    DocumentService.ProcessedDocument result = 
        documentService.processDocument(inputStream, "test.txt");

    assertNotNull(result);
    assertTrue(result.segments().size() > 0);
    assertEquals("test.txt", result.segments().get(0).metadata().getString("filename"));
}
```

Ce test de `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` crée un document en mémoire et vérifie la segmentation et la gestion des métadonnées.

### Modèle 6 : test d’intégration MCP

Le module MCP teste l’intégration du protocole Model Context Protocol via le transport stdio. Ces tests vérifient que votre application peut lancer et communiquer avec des serveurs MCP en sous-processus.

Les tests dans `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` valident le comportement du client MCP.

**Lancez-les :**

**Bash :**
```bash
cd 05-mcp && mvn test
```

**PowerShell :**
```powershell
cd 05-mcp; mvn --% test
```

## Philosophie de test

Testez votre code, pas l’IA. Vos tests doivent valider le code que vous écrivez en vérifiant comment les prompts sont construits, comment la mémoire est gérée, et comment les outils s’exécutent. Les réponses IA varient et ne devraient pas faire partie des assertions de test. Demandez-vous si votre modèle de prompt substitue correctement les variables, pas si l’IA donne la bonne réponse.

Utilisez des mocks pour les modèles de langage. Ce sont des dépendances externes qui sont lentes, coûteuses et non déterministes. Le mocking rend les tests rapides avec des millisecondes au lieu de secondes, gratuits sans frais d’API, et déterministes avec le même résultat à chaque fois.

Gardez les tests indépendants. Chaque test doit configurer ses propres données, ne pas dépendre d’autres tests, et nettoyer après lui-même. Les tests doivent réussir quel que soit l’ordre d’exécution.

Testez les cas limites au-delà du chemin heureux. Essayez des entrées vides, des entrées très grandes, des caractères spéciaux, des paramètres invalides et des conditions frontalières. Ceux-ci révèlent souvent des bugs que l’usage normal ne révèle pas.

Utilisez des noms descriptifs. Comparez `shouldMaintainConversationHistoryAcrossMultipleMessages()` avec `test1()`. Le premier vous dit exactement ce qui est testé, ce qui facilite grandement le débogage des échecs.

## Étapes suivantes

Maintenant que vous comprenez les modèles de tests, approfondissez chaque module :

- **[00 - Démarrage rapide](../00-quick-start/README.md)** - Commencez par les bases des modèles de prompt
- **[01 - Introduction](../01-introduction/README.md)** - Apprenez la gestion de la mémoire de conversation
- **[02 - Ingénierie des prompts](../02/prompt-engineering/README.md)** - Maîtrisez les modèles GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Construisez des systèmes de génération augmentée par récupération
- **[04 - Outils](../04-tools/README.md)** - Implémentez l’appel de fonctions et le chaînage d’outils
- **[05 - MCP](../05-mcp/README.md)** - Intégrez le protocole Model Context Protocol

Le README de chaque module fournit des explications détaillées des concepts testés ici.

---

**Navigation :** [← Retour au principal](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :  
Ce document a été traduit à l’aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforcions d’assurer l’exactitude, veuillez noter que les traductions automatiques peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue natale doit être considéré comme la source faisant autorité. Pour les informations essentielles, une traduction professionnelle humaine est recommandée. Nous ne sommes pas responsables des malentendus ou interprétations erronées résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->