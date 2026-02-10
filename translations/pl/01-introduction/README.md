# Moduł 01: Rozpoczynanie pracy z LangChain4j

## Spis treści

- [Czego się nauczysz](../../../01-introduction)
- [Wymagania wstępne](../../../01-introduction)
- [Zrozumienie podstawowego problemu](../../../01-introduction)
- [Zrozumienie tokenów](../../../01-introduction)
- [Jak działa pamięć](../../../01-introduction)
- [Jak to wykorzystuje LangChain4j](../../../01-introduction)
- [Wdrażanie infrastruktury Azure OpenAI](../../../01-introduction)
- [Uruchomienie aplikacji lokalnie](../../../01-introduction)
- [Korzystanie z aplikacji](../../../01-introduction)
  - [Czat bezstanowy (lewy panel)](../../../01-introduction)
  - [Czat stanowy (prawy panel)](../../../01-introduction)
- [Kolejne kroki](../../../01-introduction)

## Czego się nauczysz

Jeśli ukończyłeś szybki start, widziałeś jak wysyłać zapytania i otrzymywać odpowiedzi. To jest podstawa, ale prawdziwe aplikacje potrzebują więcej. W tym module nauczysz się, jak zbudować konwersacyjną sztuczną inteligencję, która zapamiętuje kontekst i utrzymuje stan — różnica między jednorazową demonstracją a gotową do produkcji aplikacją.

W całym przewodniku użyjemy GPT-5.2 Azure OpenAI, ponieważ jego zaawansowane możliwości rozumowania wyraźniej ukazują różnicę między różnymi wzorcami. Gdy dodasz pamięć, jasno zobaczysz różnicę. To ułatwia zrozumienie, co każdy element wnosi do twojej aplikacji.

Zbudujesz jedną aplikację, która demonstruje oba wzorce:

**Czat bezstanowy** – Każde zapytanie jest niezależne. Model nie pamięta poprzednich wiadomości. To jest wzorzec używany w szybkim starcie.

**Konwersacja stanowa** – Każde zapytanie zawiera historię rozmowy. Model utrzymuje kontekst przez wiele tur. To jest to, czego wymagają aplikacje produkcyjne.

## Wymagania wstępne

- Subskrypcja Azure z dostępem do Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Uwaga:** Java, Maven, Azure CLI oraz Azure Developer CLI (azd) są preinstalowane w dostarczonym kontenerze deweloperskim.

> **Uwaga:** Moduł ten korzysta z GPT-5.2 na Azure OpenAI. Wdrażanie jest konfigurowane automatycznie przez `azd up` — nie modyfikuj nazwy modelu w kodzie.

## Zrozumienie podstawowego problemu

Modele językowe są bezstanowe. Każde wywołanie API jest niezależne. Jeśli wyślesz "Nazywam się John", a potem zapytasz "Jak mam na imię?", model nie będzie miał pojęcia, że właśnie się przedstawiłeś. Traktuje każde zapytanie, jakby to była pierwsza rozmowa, jaką kiedykolwiek przeprowadziłeś.

To jest w porządku dla prostych pytań i odpowiedzi, ale bezużyteczne dla prawdziwych aplikacji. Boty obsługi klienta muszą pamiętać, co im powiedziałeś. Asystenci osobowi potrzebują kontekstu. Każda rozmowa wieloetapowa wymaga pamięci.

<img src="../../../translated_images/pl/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Rozmowy bezstanowe kontra stanowe" width="800"/>

*Różnica między rozmowami bezstanowymi (niezależne wywołania) a stanowymi (świadome kontekstu)*

## Zrozumienie tokenów

Zanim zagłębimy się w rozmowy, ważne jest, aby zrozumieć tokeny — podstawowe jednostki tekstu, które przetwarzają modele językowe:

<img src="../../../translated_images/pl/token-explanation.c39760d8ec650181.webp" alt="Wyjaśnienie tokenów" width="800"/>

*Przykład, jak tekst jest dzielony na tokeny – "I love AI!" staje się 4 oddzielnymi jednostkami przetwarzania*

Tokeny to sposób, w jaki modele AI mierzą i przetwarzają tekst. Słowa, interpunkcja, a nawet spacje mogą być tokenami. Twój model ma limit, ile tokenów może przetworzyć naraz (400 000 dla GPT-5.2, z czego do 272 000 tokenów wejściowych i 128 000 wychodzących). Zrozumienie tokenów pomaga zarządzać długością rozmowy i kosztami.

## Jak działa pamięć

Pamięć czatu rozwiązuje problem bezstanowości, utrzymując historię rozmowy. Przed wysłaniem zapytania do modelu, framework dokleja istotne wcześniejsze wiadomości. Gdy zapytasz "Jak mam na imię?", system faktycznie wysyła całą historię rozmowy, pozwalając modelowi zobaczyć, że wcześniej powiedziałeś "Nazywam się John."

LangChain4j dostarcza implementacje pamięci, które obsługują to automatycznie. Wybierasz, ile wiadomości zachować, a framework zarządza oknem kontekstu.

<img src="../../../translated_images/pl/memory-window.bbe67f597eadabb3.webp" alt="Koncepcja okna pamięci" width="800"/>

*MessageWindowChatMemory utrzymuje przesuwne okno ostatnich wiadomości, automatycznie usuwając stare*

## Jak to wykorzystuje LangChain4j

Ten moduł rozszerza szybki start poprzez integrację z Spring Boot i dodanie pamięci rozmowy. Oto jak elementy ze sobą współgrają:

**Zależności** – Dodaj dwie biblioteki LangChain4j:

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

**Model czatu** – Skonfiguruj Azure OpenAI jako bean Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder odczytuje dane uwierzytelniające z zmiennych środowiskowych ustawionych przez `azd up`. Ustawienie `baseUrl` na twój endpoint Azure pozwala klientowi OpenAI działać z Azure OpenAI.

**Pamięć konwersacji** – Śledź historię czatu za pomocą MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Utwórz pamięć z `withMaxMessages(10)`, aby zachować ostatnie 10 wiadomości. Dodawaj wiadomości użytkownika i AI za pomocą opakowań: `UserMessage.from(text)` i `AiMessage.from(text)`. Pobieraj historię z `memory.messages()` i wysyłaj ją do modelu. Serwis przechowuje oddzielne instancje pamięci dla każdego ID konwersacji, umożliwiając wielu użytkownikom prowadzenie rozmów jednocześnie.

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) i zapytaj:
> - "Jak MessageWindowChatMemory decyduje, które wiadomości usunąć, gdy okno jest pełne?"
> - "Czy mogę zaimplementować własne przechowywanie pamięci korzystające z bazy danych zamiast pamięci w RAM?"
> - "Jak dodać podsumowanie do kompresji starszej historii rozmów?"

Punkt końcowy czatu bezstanowego całkowicie pomija pamięć – po prostu `chatModel.chat(prompt)` jak w szybkim starcie. Punkt końcowy stanowy dodaje wiadomości do pamięci, pobiera historię i dołącza ją do każdego zapytania. Ta sama konfiguracja modelu, inne wzorce.

## Wdrażanie infrastruktury Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Wybierz subskrypcję i lokalizację (zalecane eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Wybierz subskrypcję i lokalizację (zalecane eastus2)
```

> **Uwaga:** Jeśli pojawi się błąd timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), po prostu uruchom ponownie `azd up`. Zasoby Azure mogą nadal być w fazie wdrażania w tle, a ponowne uruchomienie pozwala na ukończenie wdrażania, gdy zasoby osiągną stan końcowy.

To spowoduje:
1. Wdrożenie zasobu Azure OpenAI z modelami GPT-5.2 i text-embedding-3-small
2. Automatyczne wygenerowanie pliku `.env` w katalogu głównym projektu z danymi uwierzytelniającymi
3. Skonfigurowanie wszystkich wymaganych zmiennych środowiskowych

**Masz problemy z wdrożeniem?** Zobacz [Infrastructure README](infra/README.md) dla szczegółowego rozwiązywania problemów, w tym konfliktów nazw subdomen, kroków manualnego wdrożenia w Azure Portal i wskazówek dotyczących konfiguracji modeli.

**Sprawdź, czy wdrożenie się powiodło:**

**Bash:**
```bash
cat ../.env  # Powinno wyświetlać AZURE_OPENAI_ENDPOINT, API_KEY itp.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Powinno pokazywać AZURE_OPENAI_ENDPOINT, API_KEY itd.
```

> **Uwaga:** Polecenie `azd up` automatycznie generuje plik `.env`. Jeśli musisz go później zaktualizować, możesz edytować plik `.env` ręcznie lub wygenerować go ponownie, uruchamiając:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## Uruchomienie aplikacji lokalnie

**Sprawdź wdrożenie:**

Upewnij się, że plik `.env` istnieje w katalogu głównym i zawiera dane uwierzytelniające Azure:

**Bash:**
```bash
cat ../.env  # Powinno pokazywać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Powinno pokazywać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Uruchom aplikacje:**

**Opcja 1: Korzystanie z Spring Boot Dashboard (zalecane dla użytkowników VS Code)**

Kontener deweloperski zawiera rozszerzenie Spring Boot Dashboard, które oferuje wizualny interfejs do zarządzania wszystkimi aplikacjami Spring Boot. Znajdziesz je na pasku aktywności po lewej stronie VS Code (szukaj ikony Spring Boot).

Z poziomu Spring Boot Dashboard możesz:
- Zobaczyć wszystkie dostępne aplikacje Spring Boot w przestrzeni roboczej
- Uruchamiać/zatrzymywać aplikacje jednym kliknięciem
- Podglądać logi aplikacji w czasie rzeczywistym
- Monitorować status aplikacji

Po prostu kliknij przycisk odtwarzania obok "introduction", aby uruchomić ten moduł, lub uruchom wszystkie moduły naraz.

<img src="../../../translated_images/pl/dashboard.69c7479aef09ff6b.webp" alt="Panel Spring Boot Dashboard" width="400"/>

**Opcja 2: Użycie skryptów powłoki**

Uruchom wszystkie aplikacje webowe (moduły 01-04):

**Bash:**
```bash
cd ..  # Z katalogu głównego
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Z katalogu głównego
.\start-all.ps1
```

Lub uruchom tylko ten moduł:

**Bash:**
```bash
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Oba skrypty automatycznie ładują zmienne środowiskowe z pliku `.env` w katalogu głównym i zbudują pliki JAR, jeśli nie istnieją.

> **Uwaga:** Jeśli wolisz zbudować wszystkie moduły ręcznie przed uruchomieniem:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Otwórz w przeglądarce adres http://localhost:8080.

**Aby zatrzymać:**

**Bash:**
```bash
./stop.sh  # Tylko ten moduł
# Lub
cd .. && ./stop-all.sh  # Wszystkie moduły
```

**PowerShell:**
```powershell
.\stop.ps1  # Tylko ten moduł
# Lub
cd ..; .\stop-all.ps1  # Wszystkie moduły
```

## Korzystanie z aplikacji

Aplikacja udostępnia interfejs webowy z dwoma implementacjami czatu obok siebie.

<img src="../../../translated_images/pl/home-screen.121a03206ab910c0.webp" alt="Ekran główny aplikacji" width="800"/>

*Panel kontrolny pokazujący opcje Simple Chat (bezstanowy) oraz Conversational Chat (stanowy)*

### Czat bezstanowy (lewy panel)

Wypróbuj ten najpierw. Zapytaj "Nazywam się John", a następnie zaraz zapytaj "Jak mam na imię?" Model tego nie zapamięta, ponieważ każda wiadomość jest niezależna. To demonstruje podstawowy problem integracji modelu językowego – brak kontekstu rozmowy.

<img src="../../../translated_images/pl/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo czatu bezstanowego" width="800"/>

*AI nie pamięta twojego imienia z poprzedniej wiadomości*

### Czat stanowy (prawy panel)

Teraz spróbuj tej samej sekwencji tutaj. Zapytaj "Nazywam się John", a potem "Jak mam na imię?" Tym razem pamięta. Różnicą jest MessageWindowChatMemory – utrzymuje historię rozmowy i dołącza ją do każdego zapytania. Tak działa produkcyjna konwersacyjna AI.

<img src="../../../translated_images/pl/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo czatu stanowego" width="800"/>

*AI pamięta twoje imię z wcześniejszej rozmowy*

Oba panele używają tego samego modelu GPT-5.2. Jedyną różnicą jest pamięć. To wyraźnie pokazuje, co pamięć wnosi do aplikacji i dlaczego jest niezbędna do rzeczywistych zastosowań.

## Kolejne kroki

**Następny moduł:** [02-prompt-engineering - Inżynieria promptów z GPT-5.2](../02-prompt-engineering/README.md)

---

**Nawigacja:** [← Poprzedni: Moduł 00 - Szybki start](../00-quick-start/README.md) | [Powrót do głównej](../README.md) | [Następny: Moduł 02 - Inżynieria promptów →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zrzeczenie się odpowiedzialności**:  
Niniejszy dokument został przetłumaczony za pomocą automatycznej usługi tłumaczeniowej AI [Co-op Translator](https://github.com/Azure/co-op-translator). Chociaż dokładamy wszelkich starań, aby zapewnić poprawność, prosimy mieć na uwadze, że tłumaczenia automatyczne mogą zawierać błędy lub niedokładności. Oryginalny dokument w języku źródłowym powinien być uważany za autorytatywne źródło. W przypadku informacji o krytycznym znaczeniu zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->