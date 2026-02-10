# Azure-Infrastruktur für LangChain4j Einstieg

## Inhaltsverzeichnis

- [Voraussetzungen](../../../../01-introduction/infra)
- [Architektur](../../../../01-introduction/infra)
- [Erstellte Ressourcen](../../../../01-introduction/infra)
- [Schnellstart](../../../../01-introduction/infra)
- [Konfiguration](../../../../01-introduction/infra)
- [Verwaltungsbefehle](../../../../01-introduction/infra)
- [Kostenoptimierung](../../../../01-introduction/infra)
- [Überwachung](../../../../01-introduction/infra)
- [Fehlerbehebung](../../../../01-introduction/infra)
- [Infrastruktur aktualisieren](../../../../01-introduction/infra)
- [Aufräumen](../../../../01-introduction/infra)
- [Verzeichnisstruktur](../../../../01-introduction/infra)
- [Sicherheitsempfehlungen](../../../../01-introduction/infra)
- [Zusätzliche Ressourcen](../../../../01-introduction/infra)

Dieses Verzeichnis enthält die Azure-Infrastruktur als Code (IaC) mit Bicep und Azure Developer CLI (azd) zum Bereitstellen von Azure OpenAI-Ressourcen.

## Voraussetzungen

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (Version 2.50.0 oder höher)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (Version 1.5.0 oder höher)
- Ein Azure-Abonnement mit Berechtigungen zur Erstellung von Ressourcen

## Architektur

**Vereinfachte lokale Entwicklungsumgebung** – Nur Azure OpenAI bereitstellen, alle Apps lokal ausführen.

Die Infrastruktur stellt folgende Azure-Ressourcen bereit:

### KI-Dienste
- **Azure OpenAI**: Cognitive Services mit zwei Modellbereitstellungen:
  - **gpt-5.2**: Chat Completion-Modell (Kapazität 20K TPM)
  - **text-embedding-3-small**: Embedding-Modell für RAG (Kapazität 20K TPM)

### Lokale Entwicklung
Alle Spring Boot-Anwendungen laufen lokal auf Ihrem Rechner:
- 01-introduction (Port 8080)
- 02-prompt-engineering (Port 8083)
- 03-rag (Port 8081)
- 04-tools (Port 8084)

## Erstellte Ressourcen

| Ressourcentyp | Namensmuster der Ressource | Zweck |
|--------------|----------------------------|-------|
| Ressourcengruppe | `rg-{environmentName}` | Enthält alle Ressourcen |
| Azure OpenAI | `aoai-{resourceToken}` | Hosting der KI-Modelle |

> **Hinweis:** `{resourceToken}` ist eine eindeutige Zeichenfolge, die aus der Abonnement-ID, dem Umgebungsnamen und dem Standort generiert wird

## Schnellstart

### 1. Azure OpenAI bereitstellen

**Bash:**
```bash
cd 01-introduction
azd up
```

**PowerShell:**
```powershell
cd 01-introduction
azd up
```

Wenn Sie dazu aufgefordert werden:
- Wählen Sie Ihr Azure-Abonnement aus
- Wählen Sie einen Standort (empfohlen: `eastus2` für GPT-5.2 Verfügbarkeit)
- Bestätigen Sie den Umgebungsnamen (Standard: `langchain4j-dev`)

Dies erstellt:
- Azure OpenAI-Ressource mit GPT-5.2 und text-embedding-3-small
- Ausgabe der Verbindungsdetails

### 2. Verbindungsdetails abrufen

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Dies zeigt an:
- `AZURE_OPENAI_ENDPOINT`: Ihre Azure OpenAI-Endpunkt-URL
- `AZURE_OPENAI_KEY`: API-Schlüssel für die Authentifizierung
- `AZURE_OPENAI_DEPLOYMENT`: Name des Chat-Modells (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Name des Embedding-Modells

### 3. Anwendungen lokal ausführen

Der `azd up`-Befehl erstellt automatisch eine `.env`-Datei im Stammverzeichnis mit allen notwendigen Umgebungsvariablen.

**Empfohlen:** Starten Sie alle Webanwendungen:

**Bash:**
```bash
# Vom Stammverzeichnis
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Vom Stammverzeichnis
cd ../..
.\start-all.ps1
```

Oder starten Sie ein einzelnes Modul:

**Bash:**
```bash
# Beispiel: Nur das Einführungsmodul starten
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Beispiel: Starte nur das Einführungsmodul
cd ../01-introduction
.\start.ps1
```

Beide Skripte laden automatisch die Umgebungsvariablen aus der im Stammverzeichnis durch `azd up` erstellten `.env`-Datei.

## Konfiguration

### Modellbereitstellungen anpassen

Um Modellbereitstellungen zu ändern, bearbeiten Sie `infra/main.bicep` und passen Sie den Parameter `openAiDeployments` an:

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

Verfügbare Modelle und Versionen: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Azure-Regionen ändern

Um in einer anderen Region bereitzustellen, bearbeiten Sie `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Verfügbarkeit von GPT-5.2 prüfen: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Die Infrastruktur nach Änderungen an Bicep-Dateien aktualisieren:

**Bash:**
```bash
# ARM-Vorlage neu erstellen
az bicep build --file infra/main.bicep

# Änderungen vorschau
azd provision --preview

# Änderungen anwenden
azd provision
```

**PowerShell:**
```powershell
# ARM-Vorlage neu erstellen
az bicep build --file infra/main.bicep

# Änderungen anzeigen
azd provision --preview

# Änderungen anwenden
azd provision
```


## Aufräumen

Um alle Ressourcen zu löschen:

**Bash:**
```bash
# Alle Ressourcen löschen
azd down

# Alles löschen, einschließlich der Umgebung
azd down --purge
```

**PowerShell:**
```powershell
# Alle Ressourcen löschen
azd down

# Alles löschen, einschließlich der Umgebung
azd down --purge
```

**Achtung**: Dies wird alle Azure-Ressourcen dauerhaft löschen.

## Verzeichnisstruktur

## Kostenoptimierung

### Entwicklung/Test
Für Dev/Test-Umgebungen können Sie Kosten reduzieren:
- Verwenden Sie die Standardstufe (S0) für Azure OpenAI
- Setzen Sie eine niedrigere Kapazität (10K TPM statt 20K) in `infra/core/ai/cognitiveservices.bicep`
- Löschen Sie Ressourcen, wenn nicht in Gebrauch: `azd down`

### Produktion
Für die Produktion:
- Erhöhen Sie die OpenAI-Kapazität basierend auf Nutzung (50K+ TPM)
- Aktivieren Sie Zonendualität für höhere Verfügbarkeit
- Implementieren Sie ordnungsgemäßes Monitoring und Kostenwarnungen

### Kostenschätzung
- Azure OpenAI: Zahlung pro Token (Eingabe + Ausgabe)
- GPT-5.2: ca. 3-5 $ pro 1 Mio. Tokens (aktuellen Preis prüfen)
- text-embedding-3-small: ca. 0,02 $ pro 1 Mio. Tokens

Preisrechner: https://azure.microsoft.com/pricing/calculator/

## Überwachung

### Azure OpenAI-Metriken anzeigen

Gehen Sie im Azure-Portal zu Ihrer OpenAI-Ressource → Metriken:
- Token-basierte Auslastung
- HTTP-Anforderungsrate
- Antwortzeit
- Aktive Tokens

## Fehlerbehebung

### Problem: Namenskonflikt des Azure OpenAI Subdomains

**Fehlermeldung:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Ursache:**
Der aus Ihrem Abonnement/Umgebung generierte Subdomain-Name wird bereits verwendet, möglicherweise von einer vorherigen Bereitstellung, die nicht vollständig entfernt wurde.

**Lösung:**
1. **Option 1 - Verwenden Sie einen anderen Umgebungsnamen:**
   
   **Bash:**
   ```bash
   azd env new my-unique-env-name
   azd up
   ```
   
   **PowerShell:**
   ```powershell
   azd env new my-unique-env-name
   azd up
   ```

2. **Option 2 - Manuelle Bereitstellung über Azure-Portal:**
   - Gehen Sie zu Azure-Portal → Ressource erstellen → Azure OpenAI
   - Wählen Sie einen eindeutigen Namen für Ihre Ressource
   - Bereitstellen der folgenden Modelle:
     - **GPT-5.2**
     - **text-embedding-3-small** (für RAG-Module)
   - **Wichtig:** Notieren Sie Ihre Bereitstellungsnamen – sie müssen mit `.env`-Konfiguration übereinstimmen
   - Nach der Bereitstellung Endpoint und API-Schlüssel unter „Schlüssel und Endpunkt“ abrufen
   - Erstellen Sie eine `.env`-Datei im Projektstamm mit:
     
     **Beispiel `.env`-Datei:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Richtlinien zum Benennen der Modellbereitstellungen:**
- Verwenden Sie einfache, konsistente Namen: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Die Bereitstellungsnamen müssen genau mit der `.env`-Konfiguration übereinstimmen
- Häufiger Fehler: Modell unter einem Namen erstellen, aber im Code einen anderen Namen referenzieren

### Problem: GPT-5.2 in der ausgewählten Region nicht verfügbar

**Lösung:**
- Wählen Sie eine Region mit GPT-5.2-Zugang (z.B. eastus2)
- Verfügbarkeit prüfen: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Problem: Unzureichliches Kontingent für Bereitstellung

**Lösung:**
1. Kontingentserhöhung im Azure-Portal anfordern
2. Oder niedrigere Kapazität in `main.bicep` verwenden (z.B. capacity: 10)

### Problem: „Resource not found“ bei lokalem Betrieb

**Lösung:**
1. Bereitstellung überprüfen: `azd env get-values`
2. Endpoint und Schlüssel auf Richtigkeit prüfen
3. Sicherstellen, dass die Ressourcengruppe im Azure-Portal existiert

### Problem: Authentifizierung fehlgeschlagen

**Lösung:**
- Prüfen, ob `AZURE_OPENAI_API_KEY` korrekt gesetzt ist
- Schlüssel muss ein 32-stelliger Hexadezimalwert sein
- Bei Bedarf neuen Schlüssel im Azure-Portal generieren

### Bereitstellung schlägt fehl

**Problem**: `azd provision` schlägt mit Quoten- oder Kapazitätsfehlern fehl

**Lösung**: 
1. Versuchen Sie eine andere Region – siehe Abschnitt [Azure-Regionen ändern](../../../../01-introduction/infra)
2. Prüfen Sie, ob Ihr Abonnement über Azure OpenAI-Kontingent verfügt:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Anwendung verbindet nicht

**Problem**: Java-Anwendung zeigt Verbindungsfehler

**Lösung**:
1. Überprüfen, dass Umgebungsvariablen exportiert sind:
   
   **Bash:**
   ```bash
   echo $AZURE_OPENAI_ENDPOINT
   echo $AZURE_OPENAI_API_KEY
   ```
   
   **PowerShell:**
   ```powershell
   Write-Host $env:AZURE_OPENAI_ENDPOINT
   Write-Host $env:AZURE_OPENAI_API_KEY
   ```

2. Endpoint-Format prüfen (sollte `https://xxx.openai.azure.com` sein)
3. Prüfen, ob der API-Schlüssel der primäre oder sekundäre Schlüssel aus Azure-Portal ist

**Problem**: 401 Unauthorized von Azure OpenAI

**Lösung**:
1. Neuen API-Schlüssel aus Azure Portal → Schlüssel und Endpunkt holen
2. `AZURE_OPENAI_API_KEY` Umgebungsvariable erneut exportieren
3. Sicherstellen, dass Modellbereitstellungen abgeschlossen sind (Azure-Portal prüfen)

### Performance-Probleme

**Problem**: Langsame Antwortzeiten

**Lösung**:
1. Überprüfen Sie OpenAI-Token-Nutzung und Drosselung in Azure-Portal-Metriken
2. Erhöhen Sie die TPM-Kapazität, wenn Sie Limits erreichen
3. Erwägen Sie, einen höheren Reasoning-Consciousness-Level zu verwenden (niedrig/mittel/hoch)

## Infrastruktur aktualisieren

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


## Sicherheitsempfehlungen

1. **API-Schlüssel niemals commiten** – Verwenden Sie Umgebungsvariablen
2. **.env-Dateien lokal verwenden** – Fügen Sie `.env` zu `.gitignore` hinzu
3. **Schlüssel regelmäßig rotieren** – Neue Schlüssel im Azure-Portal erzeugen
4. **Zugriff beschränken** – Verwenden Sie Azure RBAC zur Zugriffskontrolle
5. **Nutzung überwachen** – Richten Sie Kostenwarnungen im Azure-Portal ein

## Zusätzliche Ressourcen

- [Azure OpenAI Service Dokumentation](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 Modell-Dokumentation](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI Dokumentation](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep Dokumentation](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI Offizielle Integration](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Support

Bei Problemen:
1. Prüfen Sie den Abschnitt [Fehlerbehebung](../../../../01-introduction/infra) oben
2. Überwachen Sie die Azure OpenAI Service-Status im Azure-Portal
3. Öffnen Sie ein Issue im Repository

## Lizenz

Details im Root-[LICENSE](../../../../LICENSE)-Datei.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:
Dieses Dokument wurde mithilfe des KI-Übersetzungsdienstes [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, bitten wir zu beachten, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Originalsprache ist als maßgebliche Quelle zu betrachten. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Für Missverständnisse oder Fehlinterpretationen, die durch die Verwendung dieser Übersetzung entstehen, übernehmen wir keine Haftung.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->