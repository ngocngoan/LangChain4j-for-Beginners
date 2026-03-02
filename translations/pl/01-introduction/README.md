# Moduł 01: Rozpoczęcie pracy z LangChain4j

## Spis treści

- [Wideo instruktażowe](../../../01-introduction)
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
  - [Czat ze stanem (prawy panel)](../../../01-introduction)
- [Kolejne kroki](../../../01-introduction)

## Wideo instruktażowe

Obejrzyj tę sesję na żywo, która wyjaśnia, jak rozpocząć pracę z tym modułem:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Rozpoczęcie pracy z LangChain4j - Sesja na żywo" width="800"/></a>

## Czego się nauczysz

Jeśli ukończyłeś szybki start, widziałeś, jak wysyłać zapytania i otrzymywać odpowiedzi. To podstawa, ale prawdziwe aplikacje potrzebują więcej. Ten moduł uczy, jak budować konwersacyjną AI, która zapamiętuje kontekst i utrzymuje stan – różnica między jednorazowym demo a aplikacją gotową do produkcji.

Przez cały poradnik będziemy korzystać z GPT-5.2 Azure OpenAI, ponieważ jego zaawansowane zdolności rozumowania lepiej uwidaczniają zachowanie różnych wzorców. Gdy dodasz pamięć, wyraźnie zobaczysz różnicę. To ułatwia zrozumienie, co każdy komponent wnosi do Twojej aplikacji.

Zbudujesz jedną aplikację, która demonstruje oba wzorce:

**Czat bezstanowy** – każde zapytanie jest niezależne. Model nie pamięta poprzednich wiadomości. To wzorzec użyty w szybkim starcie.

**Czat ze stanem** – każde zapytanie zawiera historię rozmowy. Model utrzymuje kontekst na przestrzeni wielu wymian. To wymóg aplikacji produkcyjnych.

## Wymagania wstępne

- Subskrypcja Azure z dostępem do Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Uwaga:** Java, Maven, Azure CLI i Azure Developer CLI (azd) są preinstalowane w dostarczonym devcontainer.

> **Uwaga:** Ten moduł używa GPT-5.2 w Azure OpenAI. Wdrożenie jest skonfigurowane automatycznie przez `azd up` – nie modyfikuj nazwy modelu w kodzie.

## Zrozumienie podstawowego problemu

Modele językowe są bezstanowe. Każde wywołanie API jest niezależne. Jeśli wyślesz "Mam na imię John", a potem zapytasz "Jak mam na imię?", model nie ma pojęcia, że się właśnie przedstawiłeś. Traktuje każde zapytanie jakby była to pierwsza rozmowa, jaką kiedykolwiek przeprowadziłeś.

To wystarcza dla prostych Q&A, ale jest bezużyteczne dla rzeczywistych aplikacji. Boty obsługi klienta muszą pamiętać, co do nich powiedziałeś. Asystenci osobowi potrzebują kontekstu. Każda rozmowa wieloetapowa wymaga pamięci.

<img src="../../../translated_images/pl/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Rozmowy bezstanowe vs ze stanem" width="800"/>

*Różnica między rozmowami bezstanowymi (niezależne wywołania) a rozmowami ze stanem (świadomość kontekstu)*

## Zrozumienie tokenów

Zanim zanurzysz się w rozmowy, ważne jest, aby zrozumieć tokeny – podstawowe jednostki tekstu, które przetwarzają modele językowe:

<img src="../../../translated_images/pl/token-explanation.c39760d8ec650181.webp" alt="Wyjaśnienie tokenów" width="800"/>

*Przykład jak tekst jest dzielony na tokeny – "I love AI!" staje się 4 osobnymi jednostkami przetwarzania*

Tokeny to sposób, w jaki modele AI mierzą i przetwarzają tekst. Słowa, interpunkcja, a nawet spacje mogą być tokenami. Twój model ma limit, ile tokenów może jednocześnie przetworzyć (400,000 dla GPT-5.2, z maksymalnie 272,000 tokenów wejściowych i 128,000 wyjściowych). Rozumienie tokenów pomaga zarządzać długością rozmowy i kosztami.

## Jak działa pamięć

Pamięć czatu rozwiązuje problem braku stanu przez utrzymywanie historii rozmowy. Przed wysłaniem zapytania do modelu, framework dołącza odpowiednie wcześniejsze wiadomości. Kiedy zapytasz "Jak mam na imię?", system faktycznie wysyła całą historię rozmowy, pozwalając modelowi zobaczyć, że wcześniej powiedziałeś "Mam na imię John".

LangChain4j dostarcza implementacje pamięci, które radzą sobie z tym automatycznie. Wybierasz, ile wiadomości chcesz zachować, a framework zarządza oknem kontekstowym.

<img src="../../../translated_images/pl/memory-window.bbe67f597eadabb3.webp" alt="Koncepcja okna pamięci" width="800"/>

*MessageWindowChatMemory utrzymuje przesuwne okno z ostatnimi wiadomościami, automatycznie usuwając starsze*

## Jak to wykorzystuje LangChain4j

Ten moduł rozwija szybki start integrując Spring Boot i dodając pamięć rozmowy. Oto jak elementy się łączą:

**Zależności** – dodaj dwie biblioteki LangChain4j:

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

**Model czatu** – skonfiguruj Azure OpenAI jako bean Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder odczytuje dane uwierzytelniające ze zmiennych środowiskowych ustawionych przez `azd up`. Ustawienie `baseUrl` na Twój punkt końcowy Azure sprawia, że klient OpenAI działa z Azure OpenAI.

**Pamięć rozmowy** – śledź historię czatu za pomocą MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Utwórz pamięć z `withMaxMessages(10)` aby zachować ostatnie 10 wiadomości. Dodawaj wiadomości użytkownika i AI za pomocą opakowań typu: `UserMessage.from(text)` i `AiMessage.from(text)`. Pobierz historię przez `memory.messages()` i wyślij ją do modelu. Serwis przechowuje osobne instancje pamięci dla każdego ID rozmowy, co umożliwia wielu użytkownikom jednoczesne czatowanie.

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) i zapytaj:
> - "Jak MessageWindowChatMemory decyduje, które wiadomości usunąć, gdy okno jest pełne?"
> - "Czy mogę zaimplementować własne przechowywanie pamięci, korzystając z bazy danych zamiast pamięci operacyjnej?"
> - "Jak dodać podsumowanie do kompresji starej historii rozmowy?"

Endpoint czatu bezstanowego całkowicie pomija pamięć – po prostu `chatModel.chat(prompt)` tak jak w szybkim starcie. Endpoint ze stanem dodaje wiadomości do pamięci, pobiera historię i dołącza ten kontekst do każdego zapytania. Ta sama konfiguracja modelu, różne wzorce.

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

> **Uwaga:** Jeśli napotkasz błąd timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), po prostu uruchom ponownie `azd up`. Zasoby Azure mogą nadal się provisioningować w tle, a ponowne próby pozwalają wdrożeniu zakończyć się, gdy zasoby osiągną stan końcowy.

To spowoduje:
1. Wdrożenie zasobu Azure OpenAI z modelami GPT-5.2 i text-embedding-3-small
2. Automatyczne wygenerowanie pliku `.env` w katalogu głównym projektu z danymi uwierzytelniającymi
3. Ustawienie wszystkich wymaganych zmiennych środowiskowych

**Masz problemy z wdrożeniem?** Zajrzyj do [README infrastruktury](infra/README.md) po szczegółowe wskazówki, m.in. dotyczące konfliktów nazw subdomen, ręcznego wdrażania przez Azure Portal oraz konfiguracji modeli.

**Sprawdź, czy wdrożenie powiodło się:**

**Bash:**
```bash
cat ../.env  # Powinien pokazywać AZURE_OPENAI_ENDPOINT, API_KEY itp.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Powinno wyświetlać AZURE_OPENAI_ENDPOINT, API_KEY itp.
```

> **Uwaga:** Polecenie `azd up` generuje plik `.env` automatycznie. Jeśli chcesz go później zaktualizować, możesz edytować plik `.env` ręcznie lub wygenerować ponownie, uruchamiając:
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

Upewnij się, że plik `.env` istnieje w katalogu głównym i zawiera dane Azure:

**Bash:**
```bash
cat ../.env  # Powinno pokazywać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Powinno pokazywać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Uruchom aplikacje:**

**Opcja 1: Korzystając z Spring Boot Dashboard (zalecane dla użytkowników VS Code)**

Dev container zawiera rozszerzenie Spring Boot Dashboard, które zapewnia wizualny interfejs do zarządzania wszystkimi aplikacjami Spring Boot. Znajdziesz je na pasku aktywności po lewej stronie VS Code (ikona Spring Boot).

Z poziomu Spring Boot Dashboard możesz:
- Zobaczyć wszystkie dostępne aplikacje Spring Boot w workspace
- Uruchamiać/zatrzymywać aplikacje jednym kliknięciem
- Podglądać logi aplikacji w czasie rzeczywistym
- Monitorować status aplikacji

Kliknij przycisk play obok "introduction", aby uruchomić ten moduł, lub uruchom wszystkie moduły jednocześnie.

<img src="../../../translated_images/pl/dashboard.69c7479aef09ff6b.webp" alt="Panel sterowania Spring Boot" width="400"/>

**Opcja 2: Korzystając ze skryptów shell**

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

Oba skrypty automatycznie ładują zmienne środowiskowe z pliku `.env` w katalogu głównym i zbudują pliki JAR, jeśli jeszcze ich nie ma.

> **Uwaga:** Jeśli wolisz ręcznie zbudować wszystkie moduły przed uruchomieniem:
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

Aplikacja udostępnia interfejs webowy z dwoma implementacjami czatu obok siebie.

<img src="../../../translated_images/pl/home-screen.121a03206ab910c0.webp" alt="Ekran główny aplikacji" width="800"/>

*Panel pokazujący opcje prostego czatu (bezstanowy) i czatu konwersacyjnego (ze stanem)*

### Czat bezstanowy (lewy panel)

Wypróbuj to najpierw. Powiedz "Mam na imię John", a potem natychmiast zapytaj "Jak mam na imię?" Model nie zapamięta, ponieważ każda wiadomość jest niezależna. To pokazuje podstawowy problem integracji z modelem językowym – brak kontekstu rozmowy.

<img src="../../../translated_images/pl/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo czatu bezstanowego" width="800"/>

*AI nie pamięta Twojego imienia z poprzedniej wiadomości*

### Czat ze stanem (prawy panel)

Teraz spróbuj tego samego tutaj. Powiedz "Mam na imię John", a potem "Jak mam na imię?" Tym razem AI zapamiętuje. Różnicą jest MessageWindowChatMemory – utrzymuje historię rozmowy i dołącza ją do każdego zapytania. Tak działa produkcyjna AI rozmów.

<img src="../../../translated_images/pl/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo czatu ze stanem" width="800"/>

*AI pamięta Twoje imię z wcześniejszej części rozmowy*

Oba panele korzystają z tego samego modelu GPT-5.2. Jedyną różnicą jest pamięć. To jasno pokazuje, co pamięć wnosi do Twojej aplikacji i dlaczego jest niezbędna do prawdziwych zastosowań.

## Kolejne kroki

**Następny moduł:** [02-prompt-engineering - Tworzenie promptów z GPT-5.2](../02-prompt-engineering/README.md)

---

**Nawigacja:** [← Poprzedni: Moduł 00 - Szybki start](../00-quick-start/README.md) | [Powrót do głównego](../README.md) | [Następny: Moduł 02 - Tworzenie promptów →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:  
Niniejszy dokument został przetłumaczony za pomocą usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Chociaż dokładamy starań, aby tłumaczenie było precyzyjne, prosimy pamiętać, że automatyczne tłumaczenia mogą zawierać błędy lub nieścisłości. Oryginalny dokument w języku źródłowym należy traktować jako źródło wiążące. W przypadku informacji krytycznych zaleca się skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za wszelkie nieporozumienia lub błędne interpretacje wynikające z użytkowania tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->