# Infrastructure Azure pour LangChain4j Guide de démarrage

## Table des matières

- [Prérequis](../../../../01-introduction/infra)
- [Architecture](../../../../01-introduction/infra)
- [Ressources créées](../../../../01-introduction/infra)
- [Démarrage rapide](../../../../01-introduction/infra)
- [Configuration](../../../../01-introduction/infra)
- [Commandes de gestion](../../../../01-introduction/infra)
- [Optimisation des coûts](../../../../01-introduction/infra)
- [Surveillance](../../../../01-introduction/infra)
- [Dépannage](../../../../01-introduction/infra)
- [Mise à jour de l’infrastructure](../../../../01-introduction/infra)
- [Nettoyage](../../../../01-introduction/infra)
- [Structure des fichiers](../../../../01-introduction/infra)
- [Recommandations de sécurité](../../../../01-introduction/infra)
- [Ressources supplémentaires](../../../../01-introduction/infra)

Ce répertoire contient l’infrastructure Azure en tant que code (IaC) utilisant Bicep et Azure Developer CLI (azd) pour le déploiement des ressources Azure OpenAI.

## Prérequis

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (version 2.50.0 ou ultérieure)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (version 1.5.0 ou ultérieure)
- Un abonnement Azure avec les permissions pour créer des ressources

## Architecture

**Configuration simplifiée pour développement local** - Déployer uniquement Azure OpenAI, exécuter toutes les applications localement.

L’infrastructure déploie les ressources Azure suivantes :

### Services IA
- **Azure OpenAI** : Services cognitifs avec deux déploiements de modèles :
  - **gpt-5.2** : Modèle de complétion conversationnelle (capacité 20K TPM)
  - **text-embedding-3-small** : Modèle d’encodage pour RAG (capacité 20K TPM)

### Développement local
Toutes les applications Spring Boot tournent localement sur votre machine :
- 01-introduction (port 8080)
- 02-prompt-engineering (port 8083)
- 03-rag (port 8081)
- 04-tools (port 8084)

## Ressources créées

| Type de ressource | Format du nom de ressource | But |
|-------------------|----------------------------|-----|
| Groupe de ressources | `rg-{environmentName}` | Contient toutes les ressources |
| Azure OpenAI | `aoai-{resourceToken}` | Hébergement des modèles IA |

> **Note :** `{resourceToken}` est une chaîne unique générée à partir de l’ID d’abonnement, du nom d’environnement, et de la localisation

## Démarrage rapide

### 1. Déployer Azure OpenAI

**Bash :**
```bash
cd 01-introduction
azd up
```

**PowerShell :**
```powershell
cd 01-introduction
azd up
```

Lorsqu’on vous le demande :
- Sélectionnez votre abonnement Azure
- Choisissez une région (recommandé : `eastus2` pour disponibilité GPT-5.2)
- Confirmez le nom de l’environnement (par défaut : `langchain4j-dev`)

Cela créera :
- La ressource Azure OpenAI avec GPT-5.2 et text-embedding-3-small
- Les détails de connexion en sortie

### 2. Obtenir les détails de connexion

**Bash :**
```bash
azd env get-values
```

**PowerShell :**
```powershell
azd env get-values
```

Cela affiche :
- `AZURE_OPENAI_ENDPOINT` : URL de votre endpoint Azure OpenAI
- `AZURE_OPENAI_KEY` : clé API pour l’authentification
- `AZURE_OPENAI_DEPLOYMENT` : nom du modèle de chat (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` : nom du modèle d’encodage

### 3. Exécuter les applications localement

La commande `azd up` crée automatiquement un fichier `.env` à la racine avec toutes les variables d’environnement nécessaires.

**Recommandé :** Démarrer toutes les applications web :

**Bash :**
```bash
# Depuis le répertoire racine
cd ../..
./start-all.sh
```

**PowerShell :**
```powershell
# Depuis le répertoire racine
cd ../..
.\start-all.ps1
```

Ou démarrer un seul module :

**Bash :**
```bash
# Exemple : Démarrer uniquement le module d'introduction
cd ../01-introduction
./start.sh
```

**PowerShell :**
```powershell
# Exemple : Lancer uniquement le module d'introduction
cd ../01-introduction
.\start.ps1
```

Les deux scripts chargent automatiquement les variables d’environnement depuis le fichier `.env` racine créé par `azd up`.

## Configuration

### Personnaliser les déploiements de modèles

Pour changer les déploiements des modèles, modifiez `infra/main.bicep` en ajustant le paramètre `openAiDeployments` :

```bicep
param openAiDeployments array = [
  {
    name: 'gpt-5.2'  // Model deployment name
    model: {
      format: 'OpenAI'
      name: 'gpt-5.2'
      version: '2025-12-11'  // Model version
    }
    sku: {
      name: 'GlobalStandard'
      capacity: 20  // TPM in thousands
    }
  }
  // Add more deployments...
]
```

Modèles et versions disponibles : https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Changer les régions Azure

Pour déployer dans une autre région, modifiez `infra/main.bicep` :

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Vérifiez la disponibilité de GPT-5.2 : https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Pour mettre à jour l’infrastructure après modification des fichiers Bicep :

**Bash :**
```bash
# Reconstruire le modèle ARM
az bicep build --file infra/main.bicep

# Prévisualiser les modifications
azd provision --preview

# Appliquer les modifications
azd provision
```

**PowerShell :**
```powershell
# Reconstruire le modèle ARM
az bicep build --file infra/main.bicep

# Prévisualiser les modifications
azd provision --preview

# Appliquer les modifications
azd provision
```

## Nettoyage

Pour supprimer toutes les ressources :

**Bash :**
```bash
# Supprimer toutes les ressources
azd down

# Tout supprimer y compris l'environnement
azd down --purge
```

**PowerShell :**
```powershell
# Supprimer toutes les ressources
azd down

# Tout supprimer, y compris l'environnement
azd down --purge
```

**Avertissement** : Cela supprimera définitivement toutes les ressources Azure.

## Structure des fichiers

## Optimisation des coûts

### Développement/Test
Pour les environnements dev/test, vous pouvez réduire les coûts :
- Utiliser le niveau Standard (S0) pour Azure OpenAI
- Mettre une capacité plus faible (10K TPM au lieu de 20K) dans `infra/core/ai/cognitiveservices.bicep`
- Supprimer les ressources lorsqu’elles ne sont pas utilisées : `azd down`

### Production
Pour la production :
- Augmenter la capacité OpenAI selon l’utilisation (50K+ TPM)
- Activer la redondance zonale pour une meilleure disponibilité
- Mettre en place une surveillance et des alertes de coûts

### Estimation des coûts
- Azure OpenAI : Paiement par token (entrée + sortie)
- GPT-5.2 : environ 3-5 $ par million de tokens (vérifiez les tarifs actuels)
- text-embedding-3-small : environ 0,02 $ par million de tokens

Calculateur de prix : https://azure.microsoft.com/pricing/calculator/

## Surveillance

### Voir les métriques Azure OpenAI

Allez dans le portail Azure → votre ressource OpenAI → Métriques :
- Utilisation basée sur les tokens
- Taux des requêtes HTTP
- Temps de réponse
- Tokens actifs

## Dépannage

### Problème : Conflit de nom de sous-domaine Azure OpenAI

**Message d’erreur :**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Cause :**
Le nom de sous-domaine généré à partir de votre abonnement/environnement est déjà utilisé, probablement suite à un déploiement précédent non complètement supprimé.

**Solution :**
1. **Option 1 - Utiliser un nom d’environnement différent :**
   
   **Bash :**
   ```bash
   azd env new my-unique-env-name
   azd up
   ```
   
   **PowerShell :**
   ```powershell
   azd env new my-unique-env-name
   azd up
   ```

2. **Option 2 - Déploiement manuel via le portail Azure :**
   - Allez dans le portail Azure → Créer une ressource → Azure OpenAI
   - Choisissez un nom unique pour votre ressource
   - Déployez les modèles suivants :
     - **GPT-5.2**
     - **text-embedding-3-small** (pour les modules RAG)
   - **Important :** Notez les noms de déploiement - ils doivent correspondre à la configuration dans `.env`
   - Après déploiement, récupérez votre endpoint et la clé API dans la section « Clés et endpoint »
   - Créez un fichier `.env` à la racine du projet avec :

     **Exemple de fichier `.env` :**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Conseils pour la nomination des déploiements de modèles :**
- Utilisez des noms simples et cohérents : `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Les noms de déploiement doivent correspondre exactement à ceux configurés dans `.env`
- Erreur fréquente : créer un modèle avec un nom et référencer un autre nom dans le code

### Problème : GPT-5.2 non disponible dans la région sélectionnée

**Solution :**
- Choisissez une région avec accès à GPT-5.2 (ex. : eastus2)
- Vérifiez la disponibilité : https://learn.microsoft.com/azure/ai-services/openai/concepts/models



### Problème : Quota insuffisant pour le déploiement

**Solution :**
1. Demandez une augmentation de quota dans le portail Azure
2. Ou utilisez une capacité inférieure dans `main.bicep` (ex. capacité : 10)

### Problème : "Ressource non trouvée" lors de l’exécution locale

**Solution :**
1. Vérifiez le déploiement : `azd env get-values`
2. Vérifiez que l’endpoint et la clé sont corrects
3. Assurez-vous que le groupe de ressources existe dans le portail Azure

### Problème : Échec d’authentification

**Solution :**
- Vérifiez que `AZURE_OPENAI_API_KEY` est correctement défini
- La clé doit être une chaîne hexadécimale de 32 caractères
- Obtenez une nouvelle clé dans le portail Azure si nécessaire

### Échec du déploiement

**Problème** : `azd provision` renvoie des erreurs de quota ou capacité

**Solution** : 
1. Essayez une autre région - Voir la section [Changer les régions Azure](../../../../01-introduction/infra) pour la configuration des régions
2. Vérifiez que votre abonnement dispose du quota Azure OpenAI :
   
   **Bash :**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell :**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Application ne se connecte pas

**Problème** : Application Java affiche des erreurs de connexion

**Solution** :
1. Vérifiez que les variables d’environnement sont exportées :
   
   **Bash :**
   ```bash
   echo $AZURE_OPENAI_ENDPOINT
   echo $AZURE_OPENAI_API_KEY
   ```
   
   **PowerShell :**
   ```powershell
   Write-Host $env:AZURE_OPENAI_ENDPOINT
   Write-Host $env:AZURE_OPENAI_API_KEY
   ```

2. Vérifiez que le format de l’endpoint est correct (doit être `https://xxx.openai.azure.com`)
3. Vérifiez que la clé API est la clé primaire ou secondaire du portail Azure

**Problème** : 401 Non autorisé depuis Azure OpenAI

**Solution** :
1. Obtenez une nouvelle clé API depuis Portail Azure → Clés et endpoint
2. Ré-exportez la variable d’environnement `AZURE_OPENAI_API_KEY`
3. Assurez-vous que les déploiements des modèles sont complets (vérifiez portail Azure)

### Problèmes de performance

**Problème** : Temps de réponse lents

**Solution** :
1. Vérifiez l’utilisation des tokens OpenAI et les limitations dans les métriques du portail Azure
2. Augmentez la capacité TPM si vous atteignez les limites
3. Envisagez d’utiliser un niveau d’effort de raisonnement plus élevé (faible/moyen/élevé)

## Mise à jour de l’infrastructure

```
infra/
├── main.bicep                       # Main infrastructure definition
├── main.json                        # Compiled ARM template (auto-generated)
├── main.bicepparam                  # Parameter file
├── README.md                        # This file
└── core/
    └── ai/
        └── cognitiveservices.bicep  # Azure OpenAI module
```

## Recommandations de sécurité

1. **Ne jamais committer les clés API** – Utilisez des variables d’environnement
2. **Utilisez des fichiers .env localement** – Ajoutez `.env` à `.gitignore`
3. **Faites une rotation régulière des clés** – Générez de nouvelles clés dans le portail Azure
4. **Limitez l’accès** – Utilisez Azure RBAC pour contrôler qui peut accéder aux ressources
5. **Surveillez l’utilisation** – Configurez des alertes de coûts dans le portail Azure

## Ressources supplémentaires

- [Documentation du service Azure OpenAI](https://learn.microsoft.com/azure/ai-services/openai/)
- [Documentation du modèle GPT-5.2](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Documentation Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Documentation Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [Intégration officielle LangChain4j OpenAI](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Support

Pour les problèmes :
1. Consultez la [section dépannage](../../../../01-introduction/infra) ci-dessus
2. Vérifiez l’état du service Azure OpenAI dans le portail Azure
3. Ouvrez un ticket dans le dépôt

## Licence

Voir le fichier [LICENSE](../../../../LICENSE) à la racine pour plus de détails.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Avertissement** :  
Ce document a été traduit à l’aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforcions d’assurer l’exactitude, veuillez noter que les traductions automatisées peuvent contenir des erreurs ou des imprécisions. Le document original dans sa langue native doit être considéré comme la source faisant foi. Pour les informations critiques, il est recommandé de recourir à une traduction humaine professionnelle. Nous ne sommes pas responsables des malentendus ou des mauvaises interprétations résultant de l’utilisation de cette traduction.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->