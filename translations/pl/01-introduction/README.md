# Moduł 01: Pierwsze kroki z LangChain4j

## Spis treści

- [Przewodnik wideo](../../../01-introduction)
- [Czego się nauczysz](../../../01-introduction)
- [Wymagania wstępne](../../../01-introduction)
- [Zrozumienie podstawowego problemu](../../../01-introduction)
- [Zrozumienie tokenów](../../../01-introduction)
- [Jak działa pamięć](../../../01-introduction)
- [Jak to wykorzystuje LangChain4j](../../../01-introduction)
- [Wdrożenie infrastruktury Azure OpenAI](../../../01-introduction)
- [Uruchomienie aplikacji lokalnie](../../../01-introduction)
- [Korzystanie z aplikacji](../../../01-introduction)
  - [Czat bezstanowy (panel lewy)](../../../01-introduction)
  - [Czat stanowy (panel prawy)](../../../01-introduction)
- [Kolejne kroki](../../../01-introduction)

## Przewodnik wideo

Obejrzyj tę sesję na żywo, która wyjaśnia, jak zacząć z tym modułem:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Pierwsze kroki z LangChain4j - sesja na żywo" width="800"/></a>

## Czego się nauczysz

W szybkim starcie korzystałeś z modeli GitHub, aby wysyłać prompt, wywoływać narzędzia, budować pipeline RAG oraz testować zabezpieczenia. Te dema pokazały, co jest możliwe — teraz przechodzimy do Azure OpenAI i GPT-5.2 i zaczynamy budować aplikacje produkcyjne. Ten moduł skupia się na konwersacyjnym AI, które pamięta kontekst i utrzymuje stan — to pojęcia, które były używane w demonstracjach szybkiego startu, ale nie zostały wyjaśnione.

W całym przewodniku użyjemy GPT-5.2 od Azure OpenAI, ponieważ jego zaawansowane możliwości rozumowania sprawiają, że zachowanie różnych wzorców jest bardziej widoczne. Kiedy dodasz pamięć, wyraźnie zobaczysz różnicę. To ułatwia zrozumienie, co każdy element wnosi do twojej aplikacji.

Zbudujesz jedną aplikację, która demonstruje oba wzorce:

**Czat bezstanowy** – Każde zapytanie jest niezależne. Model nie pamięta poprzednich wiadomości. To wzorzec używany w szybkim starcie.

**Konwersacja stanowa** – Każde zapytanie zawiera historię rozmowy. Model utrzymuje kontekst przez wiele tur. To jest wymagane w aplikacjach produkcyjnych.

## Wymagania wstępne

- Subskrypcja Azure z dostępem do Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Uwaga:** Java, Maven, Azure CLI i Azure Developer CLI (azd) są preinstalowane w dostarczonym devcontainerze.

> **Uwaga:** Ten moduł korzysta z GPT-5.2 na Azure OpenAI. Wdrożenie jest konfigurowane automatycznie przez `azd up` – nie modyfikuj nazwy modelu w kodzie.

## Zrozumienie podstawowego problemu

Modele językowe są bezstanowe. Każde wywołanie API jest niezależne. Jeśli wyślesz „Nazywam się John”, a potem zapytasz „Jak mam na imię?”, model nie ma pojęcia, że właśnie się przedstawiłeś. Traktuje każde zapytanie tak, jakby to była twoja pierwsza rozmowa.

To wystarcza do prostych pytań i odpowiedzi, ale jest bezużyteczne dla prawdziwych aplikacji. Boty obsługi klienta muszą pamiętać, co im powiedziałeś. Asystenci osobisci potrzebują kontekstu. Każda rozmowa wieloturnowa wymaga pamięci.

Poniższy diagram kontrastuje oba podejścia – po lewej wywołanie bezstanowe, które zapomina twoje imię; po prawej wywołanie stanowe wspierane przez ChatMemory, które je pamięta.

<img src="../../../translated_images/pl/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Rozmowy bezstanowe vs stanowe" width="800"/>

*Różnica między rozmowami bezstanowymi (niezależnymi wywołaniami) a stanowymi (świadomymi kontekstu)*

## Zrozumienie tokenów

Zanim zanurkujesz w rozmowy, ważne jest, aby zrozumieć tokeny – podstawowe jednostki tekstu, które przetwarzają modele językowe:

<img src="../../../translated_images/pl/token-explanation.c39760d8ec650181.webp" alt="Wyjaśnienie tokena" width="800"/>

*Przykład rozbicia tekstu na tokeny – "I love AI!" staje się 4 oddzielnymi jednostkami do przetwarzania*

Tokeny to sposób, w jaki modele AI mierzą i przetwarzają tekst. Słowa, znaki interpunkcyjne, a nawet spacje mogą być tokenami. Twój model ma limit, ile tokenów może przetworzyć naraz (400 000 dla GPT-5.2, z maksymalnie 272 000 tokenów wejściowych i 128 000 wyjściowych). Zrozumienie tokenów pomaga zarządzać długością rozmowy i kosztami.

## Jak działa pamięć

Pamięć czatu rozwiązuje problem bezstanowości przez utrzymywanie historii rozmowy. Przed wysłaniem twojego zapytania do modelu, framework dodaje na początek odpowiednie poprzednie wiadomości. Gdy zapytasz „Jak mam na imię?”, system rzeczywiście wysyła całą historię rozmowy, pozwalając modelowi zobaczyć, że wcześniej powiedziałeś „Nazywam się John”.

LangChain4j dostarcza implementacje pamięci, które obsługują to automatycznie. Wybierasz, ile wiadomości chcesz przechowywać, a framework zarządza oknem kontekstowym. Poniższy diagram pokazuje, jak MessageWindowChatMemory utrzymuje przesuwne okno ostatnich wiadomości.

<img src="../../../translated_images/pl/memory-window.bbe67f597eadabb3.webp" alt="Koncepcja okna pamięci" width="800"/>

*MessageWindowChatMemory utrzymuje przesuwne okno ostatnich wiadomości, automatycznie usuwając stare*

## Jak to wykorzystuje LangChain4j

Ten moduł rozszerza szybki start przez integrację Spring Boot i dodanie pamięci konwersacji. Oto jak elementy współpracują:

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

Builder odczytuje poświadczenia z zmiennych środowiskowych ustawionych przez `azd up`. Ustawienie `baseUrl` na twój punkt końcowy Azure zapewnia działanie klienta OpenAI z Azure OpenAI.

**Pamięć konwersacji** – Śledź historię czatu z MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Tworzy pamięć z `withMaxMessages(10)`, aby zachować ostatnie 10 wiadomości. Dodaje wiadomości użytkownika i AI za pomocą typowanych wrapperów: `UserMessage.from(text)` i `AiMessage.from(text)`. Pobiera historię z `memory.messages()` i wysyła ją do modelu. Serwis przechowuje oddzielne instancje pamięci dla każdego ID rozmowy, co pozwala na jednoczesne rozmowy wielu użytkowników.

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) i zapytaj:
> - "Jak MessageWindowChatMemory decyduje, które wiadomości usunąć, gdy okno jest pełne?"
> - "Czy mogę zaimplementować niestandardowe przechowywanie pamięci używając bazy danych zamiast pamięci operacyjnej?"
> - "Jak dodać podsumowanie do kompresji starej historii rozmowy?"

Endpoint czatu bezstanowego pomija całkowicie pamięć – po prostu `chatModel.chat(prompt)`, jak w szybkim starcie. Endpoint stanowy dodaje wiadomości do pamięci, pobiera historię i dołącza ją do każdego zapytania. Ta sama konfiguracja modelu, różne wzorce.

## Wdrożenie infrastruktury Azure OpenAI

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

> **Uwaga:** Jeśli wystąpi błąd timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), po prostu uruchom ponownie `azd up`. Zasoby Azure mogą być nadal wdrażane w tle, a ponowna próba pozwoli zakończyć wdrożenie, gdy zasoby osiągną stan terminalny.

To wykona:
1. Wdrożenie zasobu Azure OpenAI z modelami GPT-5.2 i text-embedding-3-small
2. Automatyczne wygenerowanie pliku `.env` w katalogu głównym projektu z poświadczeniami
3. Ustawienie wszystkich wymaganych zmiennych środowiskowych

**Masz problemy z wdrożeniem?** Zobacz [README infrastruktury](infra/README.md) dla szczegółowego rozwiązywania problemów, w tym konfliktów nazw subdomen, ręcznych kroków wdrażania w Azure Portal oraz wskazówek dotyczących konfiguracji modelu.

**Sprawdź, czy wdrożenie się powiodło:**

**Bash:**
```bash
cat ../.env  # Powinno pokazywać AZURE_OPENAI_ENDPOINT, API_KEY itd.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Powinno pokazywać AZURE_OPENAI_ENDPOINT, API_KEY, itd.
```

> **Uwaga:** Komenda `azd up` automatycznie generuje plik `.env`. Jeśli potrzebujesz go później zaktualizować, możesz albo edytować `.env` ręcznie albo wygenerować go ponownie uruchamiając:
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

**Zweryfikuj wdrożenie:**

Upewnij się, że plik `.env` znajduje się w katalogu głównym z poświadczeniami Azure. Uruchom to z katalogu modułu (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Powinno wyświetlać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Powinno wyświetlać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Uruchom aplikacje:**

**Opcja 1: Korzystanie z Spring Boot Dashboard (zalecane dla użytkowników VS Code)**

Devcontainer zawiera rozszerzenie Spring Boot Dashboard, które zapewnia wizualny interfejs do zarządzania wszystkimi aplikacjami Spring Boot. Znajdziesz go na pasku aktywności po lewej stronie VS Code (ikona Spring Boot).

W Spring Boot Dashboard możesz:
- Zobaczyć wszystkie dostępne aplikacje Spring Boot w workspace
- Uruchomić/zatrzymać aplikacje jednym kliknięciem
- Przeglądać logi aplikacji w czasie rzeczywistym
- Monitorować status aplikacji

Kliknij przycisk odtwarzania obok "introduction", aby uruchomić ten moduł, lub uruchom wszystkie moduły naraz.

<img src="../../../translated_images/pl/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard w VS Code — uruchamiaj, zatrzymuj i monitoruj wszystkie moduły z jednego miejsca*

**Opcja 2: Korzystanie ze skryptów shell**

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

Oba skrypty automatycznie ładują zmienne środowiskowe z pliku `.env` w katalogu głównym i zbudują pliki JAR, jeśli jeszcze nie istnieją.

> **Uwaga:** Jeśli wolisz samodzielnie budować wszystkie moduły przed uruchomieniem:
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

Otwórz w przeglądarce http://localhost:8080.

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

Aplikacja zapewnia interfejs webowy z dwoma implementacjami czatu obok siebie.

<img src="../../../translated_images/pl/home-screen.121a03206ab910c0.webp" alt="Ekran główny aplikacji" width="800"/>

*Panel pokazujący oba czaty — prosty czat (bezstanowy) i czat konwersacyjny (stanowy)*

### Czat bezstanowy (panel lewy)

Wypróbuj najpierw to. Powiedz „Nazywam się John”, a potem natychmiast zapytaj „Jak mam na imię?”. Model tego nie zapamięta, ponieważ każda wiadomość jest niezależna. To demonstruje podstawowy problem integracji modeli językowych – brak kontekstu rozmowy.

<img src="../../../translated_images/pl/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo czatu bezstanowego" width="800"/>

*AI nie pamięta twojego imienia z poprzedniej wiadomości*

### Czat stanowy (panel prawy)

Teraz spróbuj tę samą sekwencję tutaj. Powiedz „Nazywam się John”, a potem „Jak mam na imię?”. Tym razem pamięta. Różnica to MessageWindowChatMemory – utrzymuje historię rozmowy i dołącza ją do każdego zapytania. Tak działa konwersacyjne AI produkcyjne.

<img src="../../../translated_images/pl/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo czatu stanowego" width="800"/>

*AI pamięta twoje imię z wcześniejszej rozmowy*

Oba panele korzystają z tego samego modelu GPT-5.2. Jedyną różnicą jest pamięć. To jasno pokazuje, co pamięć wnosi do twojej aplikacji i dlaczego jest niezbędna w realnych przypadkach użycia.

## Kolejne kroki

**Następny moduł:** [02-prompt-engineering - Inżynieria promptów z GPT-5.2](../02-prompt-engineering/README.md)

---

**Nawigacja:** [← Poprzedni: Moduł 00 - szybki start](../00-quick-start/README.md) | [Powrót do głównego](../README.md) | [Następny: Moduł 02 - Inżynieria promptów →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zrzeczenie się odpowiedzialności**:  
Dokument ten został przetłumaczony za pomocą usługi tłumaczeń AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mimo że dążymy do dokładności, prosimy mieć na uwadze, że automatyczne tłumaczenia mogą zawierać błędy lub nieścisłości. Oryginalny dokument w języku źródłowym powinien być uznawany za autorytatywne źródło. W przypadku informacji krytycznych zaleca się skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->