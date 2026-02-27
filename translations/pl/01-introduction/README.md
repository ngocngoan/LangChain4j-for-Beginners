# Moduł 01: Rozpoczęcie pracy z LangChain4j

## Spis treści

- [Przewodnik wideo](../../../01-introduction)
- [Czego się nauczysz](../../../01-introduction)
- [Wymagania wstępne](../../../01-introduction)
- [Zrozumienie głównego problemu](../../../01-introduction)
- [Zrozumienie tokenów](../../../01-introduction)
- [Jak działa pamięć](../../../01-introduction)
- [Jak to wykorzystuje LangChain4j](../../../01-introduction)
- [Wdrożenie infrastruktury Azure OpenAI](../../../01-introduction)
- [Uruchomienie aplikacji lokalnie](../../../01-introduction)
- [Korzystanie z aplikacji](../../../01-introduction)
  - [Czat bezstanowy (lewy panel)](../../../01-introduction)
  - [Czat stanowy (prawy panel)](../../../01-introduction)
- [Dalsze kroki](../../../01-introduction)

## Przewodnik wideo

Obejrzyj tę sesję na żywo, która wyjaśnia, jak zacząć z tym modułem: [Getting Started with LangChain4j - Live Session](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Czego się nauczysz

Jeśli ukończyłeś szybki start, zobaczyłeś, jak wysyłać zapytania i otrzymywać odpowiedzi. To podstawa, ale prawdziwe aplikacje potrzebują więcej. Ten moduł nauczy Cię, jak budować konwersacyjną sztuczną inteligencję, która pamięta kontekst i utrzymuje stan - różnica między jednorazowym demo a aplikacją gotową do produkcji.

Przez cały poradnik będziemy korzystać z GPT-5.2 w Azure OpenAI, ponieważ jego zaawansowane możliwości rozumowania sprawiają, że zachowanie różnych wzorców jest bardziej widoczne. Po dodaniu pamięci wyraźnie zobaczysz różnicę. Ułatwia to zrozumienie, co każdy składnik wnosi do Twojej aplikacji.

Zbudujesz jedną aplikację demonstrującą oba wzorce:

**Czat bezstanowy** - Każde zapytanie jest niezależne. Model nie pamięta poprzednich wiadomości. To wzorzec, który używałeś w szybkim startcie.

**Rozmowa stanowa** - Każde zapytanie zawiera historię rozmowy. Model utrzymuje kontekst przez wiele tur. To jest to, czego wymagają aplikacje produkcyjne.

## Wymagania wstępne

- Subskrypcja Azure z dostępem do Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Uwaga:** Java, Maven, Azure CLI oraz Azure Developer CLI (azd) są wstępnie zainstalowane w dostarczonym devcontainerze.

> **Uwaga:** Ten moduł korzysta z GPT-5.2 w Azure OpenAI. Wdrożenie jest konfigurowane automatycznie przez `azd up` - nie modyfikuj nazwy modelu w kodzie.

## Zrozumienie głównego problemu

Modele językowe są bezstanowe. Każde wywołanie API jest niezależne. Jeśli wyślesz "Nazywam się John", a potem zapytasz "Jak mam na imię?", model nie ma pojęcia, że właśnie się przedstawiłeś. Traktuje każde zapytanie jak pierwszą rozmowę, jaką kiedykolwiek prowadziłeś.

To działa dla prostych pytań i odpowiedzi, ale jest bezużyteczne dla prawdziwych aplikacji. Boty do obsługi klienta muszą pamiętać, co im powiedziałeś. Asystenci osobiste potrzebują kontekstu. Każda rozmowa wieloetapowa wymaga pamięci.

<img src="../../../translated_images/pl/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Różnica między rozmowami bezstanowymi (niezależne wywołania) a stanowymi (świadome kontekstu)*

## Zrozumienie tokenów

Zanim zagłębimy się w rozmowy, ważne jest, aby zrozumieć tokeny - podstawowe jednostki tekstu, które przetwarzają modele językowe:

<img src="../../../translated_images/pl/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Przykład, jak tekst jest dzielony na tokeny - "I love AI!" staje się 4 oddzielnymi jednostkami przetwarzania*

Tokeny to sposób, w jaki modele AI mierzą i przetwarzają tekst. Słowa, znaki interpunkcyjne, a nawet spacje mogą być tokenami. Twój model ma limit, ile tokenów może przetworzyć jednocześnie (400,000 dla GPT-5.2, z maksymalnie 272,000 tokenów wejściowych i 128,000 wyjściowych). Zrozumienie tokenów pomaga zarządzać długością rozmowy i kosztami.

## Jak działa pamięć

Pamięć czatu rozwiązuje problem bezstanowości przez utrzymywanie historii rozmowy. Przed wysłaniem zapytania do modelu, framework dołącza wcześniejsze, istotne wiadomości. Gdy zapytasz "Jak mam na imię?", system rzeczywiście wysyła całą historię rozmowy, pozwalając modelowi zobaczyć, że wcześniej powiedziałeś "Nazywam się John."

LangChain4j oferuje implementacje pamięci, które obsługują to automatycznie. Możesz wybrać, ile wiadomości zachować, a framework zarządza oknem kontekstu.

<img src="../../../translated_images/pl/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory utrzymuje przesuwne okno najnowszych wiadomości, automatycznie usuwając stare*

## Jak to wykorzystuje LangChain4j

Ten moduł rozszerza szybki start przez integrację Spring Boot i dodanie pamięci rozmowy. Oto jak elementy się łączą:

**Zależności** - Dodaj dwie biblioteki LangChain4j:

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

**Model czatu** - Skonfiguruj Azure OpenAI jako bean Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder odczytuje poświadczenia ze zmiennych środowiskowych ustawionych przez `azd up`. Ustawienie `baseUrl` na Twój punkt końcowy Azure sprawia, że klient OpenAI działa z Azure OpenAI.

**Pamięć rozmowy** - Śledź historię czatu z MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Utwórz pamięć z `withMaxMessages(10)`, aby przechowywać ostatnie 10 wiadomości. Dodawaj wiadomości użytkownika i AI przy pomocy typowanych wrapperów: `UserMessage.from(text)` i `AiMessage.from(text)`. Pobierz historię przez `memory.messages()` i wyślij ją do modelu. Serwis przechowuje oddzielne instancje pamięci dla każdego identyfikatora rozmowy, umożliwiając wielu użytkownikom jednoczesną rozmowę.

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) i zapytaj:
> - "Jak MessageWindowChatMemory decyduje, które wiadomości usunąć, gdy okno jest pełne?"
> - "Czy mogę zaimplementować własne przechowywanie pamięci, używając bazy danych zamiast pamięci operacyjnej?"
> - "Jak dodać streszczenie, aby skompresować starą historię rozmowy?"

Punkt końcowy czatu bezstanowego pomija pamięć całkowicie - to po prostu `chatModel.chat(prompt)` jak w szybkim starcie. Punkt końcowy stanowy dodaje wiadomości do pamięci, pobiera historię i dołącza ten kontekst do każdego zapytania. Ta sama konfiguracja modelu, różne wzorce.

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

> **Uwaga:** Jeśli napotkasz błąd przekroczenia limitu czasu (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), po prostu uruchom ponownie `azd up`. Zasoby Azure mogą nadal się wdrażać w tle, a powtórzenie pozwoli na zakończenie wdrożenia, gdy zasoby osiągną stan końcowy.

To spowoduje:
1. Wdrożenie zasobu Azure OpenAI z modelami GPT-5.2 oraz text-embedding-3-small
2. Automatyczne wygenerowanie pliku `.env` w katalogu głównym projektu z poświadczeniami
3. Ustawienie wszystkich wymaganych zmiennych środowiskowych

**Masz problemy z wdrożeniem?** Zobacz [README infrastruktury](infra/README.md) dla szczegółowego rozwiązywania problemów, w tym konfliktów nazw subdomen, ręcznych kroków wdrożeniowych w Azure Portal oraz wskazówek dotyczących konfiguracji modelu.

**Sprawdź, czy wdrożenie się powiodło:**

**Bash:**
```bash
cat ../.env  # Powinno pokazywać AZURE_OPENAI_ENDPOINT, API_KEY itp.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Powinno pokazywać AZURE_OPENAI_ENDPOINT, API_KEY itd.
```

> **Uwaga:** Komenda `azd up` automatycznie generuje plik `.env`. Jeśli musisz go zaktualizować później, możesz edytować plik `.env` ręcznie lub wygenerować go ponownie za pomocą:
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

Upewnij się, że plik `.env` znajduje się w katalogu głównym i zawiera poświadczenia Azure:

**Bash:**
```bash
cat ../.env  # Powinno pokazywać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Powinien pokazywać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Uruchom aplikacje:**

**Opcja 1: Korzystanie z Spring Boot Dashboard (zalecane dla użytkowników VS Code)**

Dev container zawiera rozszerzenie Spring Boot Dashboard, które oferuje wizualny interfejs do zarządzania wszystkimi aplikacjami Spring Boot. Znajdziesz je na pasku aktywności po lewej stronie VS Code (ikona Spring Boot).

Z poziomu Spring Boot Dashboard możesz:
- Zobaczyć wszystkie dostępne aplikacje Spring Boot w obszarze roboczym
- Uruchamiać/zatrzymywać aplikacje jednym kliknięciem
- Oglądać logi aplikacji w czasie rzeczywistym
- Monitorować status aplikacji

Po prostu kliknij przycisk odtwarzania obok "introduction", aby uruchomić ten moduł lub uruchom wszystkie moduły naraz.

<img src="../../../translated_images/pl/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

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

Oba skrypty automatycznie ładują zmienne środowiskowe z pliku `.env` w katalogu głównym i zbudują pliki JAR, jeśli nie istnieją.

> **Uwaga:** Jeśli wolisz ręcznie budować wszystkie moduły przed uruchomieniem:
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

Otwórz http://localhost:8080 w przeglądarce.

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

<img src="../../../translated_images/pl/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Panel sterowania pokazujący opcje zarówno Prosty Czat (bezstanowy), jak i Rozmowę Czatową (stanową)*

### Czat bezstanowy (lewy panel)

Wypróbuj najpierw tę opcję. Zapytaj "Nazywam się John", a potem od razu "Jak mam na imię?" Model nie zapamięta, ponieważ każda wiadomość jest niezależna. Demonstruje to podstawowy problem integracji z modelem językowym - brak kontekstu rozmowy.

<img src="../../../translated_images/pl/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI nie pamięta Twojego imienia z poprzedniej wiadomości*

### Czat stanowy (prawy panel)

Teraz spróbuj tej samej sekwencji tutaj. Zapytaj "Nazywam się John", a potem "Jak mam na imię?" Tym razem pamięta. Różnicą jest MessageWindowChatMemory - utrzymuje historię rozmowy i dołącza ją do każdego zapytania. Tak działają produkcyjne systemy konwersacyjne AI.

<img src="../../../translated_images/pl/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI pamięta Twoje imię z wcześniejszej części rozmowy*

Oba panele używają tego samego modelu GPT-5.2. Jedyne, co się różni, to pamięć. Dzięki temu jasno widać, co pamięć wnosi do Twojej aplikacji i dlaczego jest niezbędna dla prawdziwych zastosowań.

## Dalsze kroki

**Następny moduł:** [02-prompt-engineering - Inżynieria promptów z GPT-5.2](../02-prompt-engineering/README.md)

---

**Nawigacja:** [← Poprzedni: Moduł 00 - Szybki start](../00-quick-start/README.md) | [Powrót do głównej](../README.md) | [Następny: Moduł 02 - Inżynieria promptów →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:  
Niniejszy dokument został przetłumaczony przy użyciu automatycznego serwisu tłumaczeniowego AI [Co-op Translator](https://github.com/Azure/co-op-translator). Dokładamy starań, aby tłumaczenie było jak najbardziej precyzyjne, jednak prosimy pamiętać, że tłumaczenia automatyczne mogą zawierać błędy lub nieścisłości. Oryginalny dokument w języku źródłowym należy traktować jako ostateczne źródło informacji. W przypadku istotnych informacji zalecane jest skorzystanie z profesjonalnego, ludzkiego tłumaczenia. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->