# Infrastruktura Azure dla LangChain4j - Wprowadzenie

## Spis treści

- [Wymagania wstępne](../../../../01-introduction/infra)
- [Architektura](../../../../01-introduction/infra)
- [Utworzone zasoby](../../../../01-introduction/infra)
- [Szybki start](../../../../01-introduction/infra)
- [Konfiguracja](../../../../01-introduction/infra)
- [Polecenia zarządzania](../../../../01-introduction/infra)
- [Optymalizacja kosztów](../../../../01-introduction/infra)
- [Monitorowanie](../../../../01-introduction/infra)
- [Rozwiązywanie problemów](../../../../01-introduction/infra)
- [Aktualizacja infrastruktury](../../../../01-introduction/infra)
- [Porządkowanie](../../../../01-introduction/infra)
- [Struktura plików](../../../../01-introduction/infra)
- [Zalecenia dotyczące bezpieczeństwa](../../../../01-introduction/infra)
- [Dodatkowe zasoby](../../../../01-introduction/infra)

Ten katalog zawiera infrastrukturę Azure jako kod (IaC) z użyciem Bicep i Azure Developer CLI (azd) do wdrażania zasobów Azure OpenAI.

## Wymagania wstępne

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (wersja 2.50.0 lub nowsza)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (wersja 1.5.0 lub nowsza)
- Subskrypcja Azure z uprawnieniami do tworzenia zasobów

## Architektura

**Uproszczony lokalny zestaw deweloperski** – Wdrażaj tylko Azure OpenAI, uruchamiaj wszystkie aplikacje lokalnie.

Infrastruktura wdraża następujące zasoby Azure:

### Usługi AI
- **Azure OpenAI**: Usługi kognitywne z dwoma wdrożeniami modeli:
  - **gpt-5.2**: model do uzupełniania czatów (pojemność 20K TPM)
  - **text-embedding-3-small**: model do osadzania dla RAG (pojemność 20K TPM)

### Lokalny rozwój
Wszystkie aplikacje Spring Boot działają lokalnie na twoim komputerze:
- 01-introduction (port 8080)
- 02-prompt-engineering (port 8083)
- 03-rag (port 8081)
- 04-tools (port 8084)

## Utworzone zasoby

| Typ zasobu     | Nazwa zasobu                       | Przeznaczenie             |
|----------------|----------------------------------|---------------------------|
| Grupa zasobów  | `rg-{environmentName}`            | Zawiera wszystkie zasoby  |
| Azure OpenAI   | `aoai-{resourceToken}`             | Hostowanie modelu AI      |

> **Uwaga:** `{resourceToken}` to unikalny ciąg generowany z ID subskrypcji, nazwy środowiska i lokalizacji

## Szybki start

### 1. Wdróż Azure OpenAI

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

Po pojawieniu się monitów:
- Wybierz subskrypcję Azure
- Wybierz lokalizację (zalecane: `eastus2` ze względu na dostępność GPT-5.2)
- Potwierdź nazwę środowiska (domyślna: `langchain4j-dev`)

To spowoduje utworzenie:
- zasobu Azure OpenAI z GPT-5.2 i text-embedding-3-small
- wyświetlenie danych połączeniowych

### 2. Pobierz dane połączeniowe

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Wyświetla:
- `AZURE_OPENAI_ENDPOINT`: URL punktu końcowego Azure OpenAI
- `AZURE_OPENAI_KEY`: klucz API do uwierzytelniania
- `AZURE_OPENAI_DEPLOYMENT`: nazwa modelu czatu (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: nazwa modelu osadzającego

### 3. Uruchom aplikacje lokalnie

Polecenie `azd up` automatycznie tworzy plik `.env` w katalogu głównym z wszystkimi potrzebnymi zmiennymi środowiskowymi.

**Zalecane:** Uruchom wszystkie aplikacje webowe:

**Bash:**
```bash
# Z katalogu głównego
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Z katalogu głównego
cd ../..
.\start-all.ps1
```

Lub uruchom pojedynczy moduł:

**Bash:**
```bash
# Przykład: Uruchom tylko moduł wprowadzenia
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Przykład: Uruchom tylko moduł wprowadzający
cd ../01-introduction
.\start.ps1
```

Oba skrypty automatycznie ładują zmienne środowiskowe z pliku `.env` w katalogu głównym utworzonego przez `azd up`.

## Konfiguracja

### Dostosowywanie wdrożeń modeli

Aby zmienić wdrożenia modeli, edytuj `infra/main.bicep` i modyfikuj parametr `openAiDeployments`:

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

Dostępne modele i wersje: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Zmiana regionów Azure

Aby wdrożyć w innym regionie, edytuj `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Sprawdź dostępność GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Aby zaktualizować infrastrukturę po zmianach w plikach Bicep:

**Bash:**
```bash
# Przebuduj szablon ARM
az bicep build --file infra/main.bicep

# Podgląd zmian
azd provision --preview

# Zastosuj zmiany
azd provision
```

**PowerShell:**
```powershell
# Odbuduj szablon ARM
az bicep build --file infra/main.bicep

# Podgląd zmian
azd provision --preview

# Zastosuj zmiany
azd provision
```

## Porządkowanie

Aby usunąć wszystkie zasoby:

**Bash:**
```bash
# Usuń wszystkie zasoby
azd down

# Usuń wszystko, włącznie ze środowiskiem
azd down --purge
```

**PowerShell:**
```powershell
# Usuń wszystkie zasoby
azd down

# Usuń wszystko, włącznie ze środowiskiem
azd down --purge
```

**Ostrzeżenie**: Spowoduje to trwałe usunięcie wszystkich zasobów Azure.

## Struktura plików

## Optymalizacja kosztów

### Rozwój/testowanie
Dla środowisk dev/test możesz zmniejszyć koszty:
- Używaj standardowego poziomu (S0) dla Azure OpenAI
- Ustaw niższą pojemność (10K TPM zamiast 20K) w `infra/core/ai/cognitiveservices.bicep`
- Usuwaj zasoby, gdy nie są potrzebne: `azd down`

### Produkcja
Dla produkcji:
- Zwiększ pojemność OpenAI w zależności od użycia (50K+ TPM)
- Włącz strefową redundancję dla wyższej dostępności
- Wdrażaj odpowiednie monitorowanie i alerty kosztowe

### Estymacja kosztów
- Azure OpenAI: Płatność za token (wejściowy + wyjściowy)
- GPT-5.2: ok. 3-5 USD za 1 mln tokenów (sprawdź aktualne ceny)
- text-embedding-3-small: ok. 0,02 USD za 1 mln tokenów

Kalkulator cen: https://azure.microsoft.com/pricing/calculator/

## Monitorowanie

### Przegląd metryk Azure OpenAI

Przejdź do Azure Portal → Twój zasób OpenAI → Metryki:
- Wykorzystanie na podstawie tokenów
- Liczba żądań HTTP
- Czas odpowiedzi
- Aktywne tokeny

## Rozwiązywanie problemów

### Problem: konflikt nazwy subdomeny Azure OpenAI

**Wiadomość o błędzie:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Przyczyna:**
Nazwa subdomeny wygenerowana z twojej subskrypcji/środowiska jest już używana, prawdopodobnie z poprzedniego wdrożenia, które nie zostało w pełni usunięte.

**Rozwiązanie:**
1. **Opcja 1 - Użyj innej nazwy środowiska:**
   
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

2. **Opcja 2 - Ręczne wdrożenie przez Azure Portal:**
   - Przejdź do Azure Portal → Utwórz zasób → Azure OpenAI
   - Wybierz unikalną nazwę zasobu
   - Wdróż następujące modele:
     - **GPT-5.2**
     - **text-embedding-3-small** (dla modułów RAG)
   - **Ważne:** Zanotuj nazwy wdrożeń - muszą pasować do konfiguracji w `.env`
   - Po wdrożeniu pobierz punkt końcowy i klucz API z „Klucze i punkt końcowy”
   - Utwórz plik `.env` w katalogu głównym projektu z:

     **Przykładowy plik `.env`:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Wytyczne dotyczące nazewnictwa wdrożeń modeli:**
- Używaj prostych, spójnych nazw: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Nazwy wdrożeń muszą dokładnie odpowiadać konfiguracji w `.env`
- Częsty błąd: Tworzenie modelu pod jedną nazwą, a odwoływanie się w kodzie pod inną

### Problem: GPT-5.2 niedostępny w wybranym regionie

**Rozwiązanie:**
- Wybierz region z dostępem do GPT-5.2 (np. eastus2)
- Sprawdź dostępność: https://learn.microsoft.com/azure/ai-services/openai/concepts/models



### Problem: Niewystarczający limit dla wdrożenia

**Rozwiązanie:**
1. Złóż prośbę o zwiększenie limitu w Azure Portal
2. Lub użyj niższej pojemności w `main.bicep` (np. capacity: 10)

### Problem: "Nie znaleziono zasobu" podczas uruchamiania lokalnego

**Rozwiązanie:**
1. Sprawdź wdrożenie: `azd env get-values`
2. Zweryfikuj poprawność endpointu i klucza
3. Upewnij się, że grupa zasobów istnieje w Azure Portal

### Problem: Błąd uwierzytelniania

**Rozwiązanie:**
- Sprawdź, czy `AZURE_OPENAI_API_KEY` jest poprawnie ustawiony
- Format klucza powinien być 32-znakowym łańcuchem szesnastkowym
- W razie potrzeby pobierz nowy klucz w Azure Portal

### Nieudane wdrożenie

**Problem**: `azd provision` zwraca błędy limitów lub pojemności

**Rozwiązanie**: 
1. Spróbuj innego regionu – Zobacz sekcję [Zmiana regionów Azure](../../../../01-introduction/infra) jak konfigurować regiony
2. Sprawdź, czy w twojej subskrypcji jest dostępny limit dla Azure OpenAI:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Aplikacja się nie łączy

**Problem**: Aplikacja Java wyświetla błędy połączenia

**Rozwiązanie**:
1. Zweryfikuj zmienne środowiskowe:
   
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

2. Sprawdź, czy format endpointu jest prawidłowy (powinien być `https://xxx.openai.azure.com`)
3. Zweryfikuj, czy klucz API jest kluczem podstawowym lub dodatkowym z Azure Portal

**Problem**: 401 Unauthorized z Azure OpenAI

**Rozwiązanie**:
1. Pobierz nowy klucz API z Azure Portal → Klucze i punkt końcowy
2. Ponownie ustaw zmienną środowiskową `AZURE_OPENAI_API_KEY`
3. Upewnij się, że wdrożenia modeli są kompletne (sprawdź w Azure Portal)

### Problemy z wydajnością

**Problem**: Wolne czasy odpowiedzi

**Rozwiązanie**:
1. Sprawdź zużycie tokenów i ograniczenia w metrykach Azure Portal
2. Zwiększ pojemność TPM, jeśli osiągasz limity
3. Rozważ użycie wyższego poziomu wysiłku rozumowania (niski/średni/wysoki)

## Aktualizacja infrastruktury

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

## Zalecenia dotyczące bezpieczeństwa

1. **Nigdy nie commituj kluczy API** – używaj zmiennych środowiskowych
2. **Używaj plików .env lokalnie** – dodaj `.env` do `.gitignore`
3. **Regularnie rotuj klucze** – generuj nowe w Azure Portal
4. **Ogranicz dostęp** – używaj Azure RBAC do kontroli dostępu do zasobów
5. **Monitoruj wykorzystanie** – ustaw alerty kosztowe w Azure Portal

## Dodatkowe zasoby

- [Dokumentacja usługi Azure OpenAI](https://learn.microsoft.com/azure/ai-services/openai/)
- [Dokumentacja modelu GPT-5.2](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Dokumentacja Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Dokumentacja Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [Oficjalna integracja LangChain4j OpenAI](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Wsparcie

W przypadku problemów:
1. Sprawdź [sekcję rozwiązywania problemów](../../../../01-introduction/infra)
2. Przejrzyj stan usługi Azure OpenAI w Azure Portal
3. Zgłoś problem w repozytorium

## Licencja

Szczegóły w pliku [LICENSE](../../../../LICENSE) w katalogu głównym.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:
Niniejszy dokument został przetłumaczony za pomocą usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Chociaż staramy się zapewnić dokładność, prosimy pamiętać, że tłumaczenia automatyczne mogą zawierać błędy lub nieścisłości. Oryginalny dokument w języku źródłowym powinien być traktowany jako źródło autorytatywne. W przypadku informacji o krytycznym znaczeniu zaleca się skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->