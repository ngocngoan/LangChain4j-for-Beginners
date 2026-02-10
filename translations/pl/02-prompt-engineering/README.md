# Moduł 02: Inżynieria promptów z GPT-5.2

## Spis treści

- [Czego się nauczysz](../../../02-prompt-engineering)
- [Wymagania wstępne](../../../02-prompt-engineering)
- [Zrozumienie inżynierii promptów](../../../02-prompt-engineering)
- [Jak to wykorzystuje LangChain4j](../../../02-prompt-engineering)
- [Podstawowe wzorce](../../../02-prompt-engineering)
- [Korzystanie z istniejących zasobów Azure](../../../02-prompt-engineering)
- [Zrzuty ekranu aplikacji](../../../02-prompt-engineering)
- [Eksploracja wzorców](../../../02-prompt-engineering)
  - [Niskie vs wysokie zaangażowanie](../../../02-prompt-engineering)
  - [Wykonywanie zadań (wprowadzenia do narzędzi)](../../../02-prompt-engineering)
  - [Kod z autorefleksją](../../../02-prompt-engineering)
  - [Analiza strukturalna](../../../02-prompt-engineering)
  - [Czat wieloetapowy](../../../02-prompt-engineering)
  - [Rozumowanie krok po kroku](../../../02-prompt-engineering)
  - [Ograniczona odpowiedź](../../../02-prompt-engineering)
- [Czego tak naprawdę się uczysz](../../../02-prompt-engineering)
- [Następne kroki](../../../02-prompt-engineering)

## Czego się nauczysz

W poprzednim module zobaczyłeś, jak pamięć umożliwia konwersacyjną AI i korzystałeś z modeli GitHub do podstawowych interakcji. Teraz skupimy się na tym, jak zadajesz pytania – na samych promptach – korzystając z GPT-5.2 usługi Azure OpenAI. Sposób, w jaki strukturyzujesz prompt, dramatycznie wpływa na jakość otrzymywanych odpowiedzi.

Użyjemy GPT-5.2, ponieważ wprowadza kontrolę rozumowania – możesz powiedzieć modelowi, ile ma myśleć, zanim odpowie. To sprawia, że różne strategie promptowania stają się bardziej widoczne i pomaga zrozumieć, kiedy stosować które podejście. Skorzystamy również z mniejszej liczby ograniczeń przepustowości dla GPT-5.2 w Azure w porównaniu do modeli GitHub.

## Wymagania wstępne

- Ukończony Moduł 01 (wdrożone zasoby Azure OpenAI)
- Plik `.env` w katalogu głównym z poświadczeniami Azure (utworzony przez `azd up` w Module 01)

> **Uwaga:** Jeśli nie ukończyłeś Modułu 01, najpierw wykonaj instrukcje wdrożenia tam zawarte.

## Zrozumienie inżynierii promptów

Inżynieria promptów polega na projektowaniu tekstu wejściowego, który konsekwentnie daje Ci potrzebne wyniki. To nie tylko zadawanie pytań – chodzi o strukturyzowanie zapytań tak, by model dokładnie rozumiał, czego oczekujesz i jak to dostarczyć.

Pomyśl o tym jak o dawaniu poleceń współpracownikowi. "Napraw błąd" jest niejasne. "Napraw wyjątek null pointer w UserService.java na linii 45 przez dodanie sprawdzenia null" jest precyzyjne. Modele językowe działają podobnie – konkretność i struktura mają znaczenie.

## Jak to wykorzystuje LangChain4j

Ten moduł demonstruje zaawansowane wzorce promptowania oparte na tej samej fundamencie LangChain4j z poprzednich modułów, skupiając się na strukturze promptów i kontroli rozumowania.

<img src="../../../translated_images/pl/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*Jak LangChain4j łączy Twoje prompt z Azure OpenAI GPT-5.2*

**Zależności** – Moduł 02 używa następujących zależności langchain4j zdefiniowanych w `pom.xml`:
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

**Konfiguracja OpenAiOfficialChatModel** – [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Model czatu jest ręcznie konfigurowany jako bean Spring za pomocą oficjalnego klienta OpenAI, który obsługuje punkty końcowe Azure OpenAI. Kluczowa różnica względem Modułu 01 to sposób strukturyzacji promptów wysyłanych do `chatModel.chat()`, a nie samo ustawienie modelu.

**Wiadomości systemowe i użytkownika** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j rozdziela typy wiadomości dla jasności. `SystemMessage` ustawia zachowanie i kontekst AI (np. "Jesteś recenzentem kodu"), podczas gdy `UserMessage` zawiera faktyczne zapytanie. Ten podział pozwala utrzymać spójne zachowanie AI w różnych pytaniach użytkowników.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/pl/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage zapewnia trwały kontekst, podczas gdy UserMessage zawiera indywidualne zapytania*

**MessageWindowChatMemory dla rozmów wieloetapowych** – Wzorec rozmów wieloetapowych ponownie wykorzystuje `MessageWindowChatMemory` z Modułu 01. Każda sesja ma swoją własną instancję pamięci przechowywaną w `Map<String, ChatMemory>`, co pozwala na wiele równoległych konwersacji bez mieszania kontekstów.

**Szablony promptów** – Główny nacisk jest na inżynierię promptów, a nie nowe API LangChain4j. Każdy wzorzec (niskie zaangażowanie, wysokie zaangażowanie, wykonanie zadania itd.) korzysta z tej samej metody `chatModel.chat(prompt)` lecz z dokładnie zbudowanymi łańcuchami promptów. Tagowanie XML, instrukcje i formatowanie to część tekstu promptu, nie funkcje LangChain4j.

**Kontrola rozumowania** – Wysiłek rozumowania GPT-5.2 jest kontrolowany przez instrukcje w promptach, takie jak "maksymalnie 2 kroki rozumowania" czy "badaj dogłębnie". To techniki inżynierii promptów, a nie konfiguracje LangChain4j. Biblioteka jedynie dostarcza Twoje prompt do modelu.

Kluczowa nauka: LangChain4j zapewnia infrastrukturę (połączenie z modelem poprzez [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), pamięć, obsługę wiadomości przez [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), podczas gdy ten moduł uczy, jak tworzyć skuteczne prompty w tej infrastrukturze.

## Podstawowe wzorce

Nie wszystkie problemy wymagają tego samego podejścia. Niektóre pytania potrzebują szybkich odpowiedzi, inne głębokiego rozumowania. Niektóre wymagają widocznego rozumowania, inne tylko wyników. Ten moduł omawia osiem wzorców promptowania – każdy zoptymalizowany pod inne scenariusze. Przetestujesz je wszystkie, aby zrozumieć, kiedy które działają najlepiej.

<img src="../../../translated_images/pl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Przegląd ośmiu wzorców inżynierii promptów i ich zastosowań*

<img src="../../../translated_images/pl/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Niskie zaangażowanie (szybkie, bezpośrednie) vs wysokie zaangażowanie (dokładne, eksploracyjne) podejścia do rozumowania*

**Niskie zaangażowanie (szybkie i skupione)** – Dla prostych pytań, gdzie chcesz szybkich, bezpośrednich odpowiedzi. Model wykonuje minimalne rozumowanie – maksymalnie 2 kroki. Użyj tego do obliczeń, wyszukiwań lub prostych pytań.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Eksploruj z GitHub Copilot:** Otwórz [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) i zapytaj:
> - "Jaka jest różnica między wzorcami niskiego a wysokiego zaangażowania w promptowaniu?"
> - "Jak tagi XML w promptach pomagają strukturyzować odpowiedź AI?"
> - "Kiedy powinienem używać wzorców autorefleksji a kiedy bezpośredniej instrukcji?"

**Wysokie zaangażowanie (głębokie i dokładne)** – Dla złożonych problemów, gdzie chcesz kompleksowej analizy. Model bada dokładnie i pokazuje szczegółowe rozumowanie. Użyj tego do projektowania systemów, decyzji architektonicznych lub złożonych badań.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Wykonywanie zadań (postęp krok po kroku)** – Dla przepływów wieloetapowych. Model przedstawia plan na początku, opisuje każdy krok podczas działania, a na końcu podaje podsumowanie. Użyj do migracji, wdrożeń lub dowolnej wieloetapowej procedury.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Promptowanie łańcuchowe (Chain-of-Thought) explicite prosi model o pokazanie procesu rozumowania, co poprawia dokładność w złożonych zadaniach. Podział krok po kroku pomaga zarówno ludziom, jak i AI zrozumieć logikę.

> **🤖 Wypróbuj z czatem [GitHub Copilot](https://github.com/features/copilot):** Zapytaj o ten wzorzec:
> - "Jak dostosować wzorzec wykonania zadań do długotrwałych operacji?"
> - "Jakie są najlepsze praktyki dotyczące strukturyzacji wstępnych wprowadzeń do narzędzi w aplikacjach produkcyjnych?"
> - "Jak uchwycić i wyświetlać pośrednie aktualizacje postępu w interfejsie użytkownika?"

<img src="../../../translated_images/pl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plan → Realizacja → Podsumowanie - przepływ pracy dla zadań wieloetapowych*

**Kod z autorefleksją** – Do generowania kodu produkcyjnej jakości. Model generuje kod, ocenia go pod względem jakości i iteracyjnie ulepsza. Użyj przy tworzeniu nowych funkcji lub usług.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Pętla iteracyjnego ulepszania - generuj, oceniaj, identyfikuj problemy, poprawiaj, powtarzaj*

**Analiza strukturalna** – Do spójnej oceny. Model przegląda kod według ustalonego schematu (poprawność, praktyki, wydajność, bezpieczeństwo). Użyj do przeglądów kodu lub oceny jakości.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Wypróbuj z czatem [GitHub Copilot](https://github.com/features/copilot):** Zapytaj o analizę strukturalną:
> - "Jak dostosować ramy analizy do różnych typów przeglądów kodu?"
> - "Jak najlepiej analizować i wykorzystywać strukturalny output programistycznie?"
> - "Jak zapewnić spójność poziomów ważności w różnych sesjach przeglądu?"

<img src="../../../translated_images/pl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Ramowy model czterech kategorii dla spójnych przeglądów kodu z poziomami ważności*

**Czat wieloetapowy** – Do rozmów wymagających kontekstu. Model pamięta poprzednie wiadomości i buduje na ich podstawie. Użyj do interaktywnych sesji pomocy lub złożonych Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/pl/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Jak kontekst rozmowy kumuluje się przez wiele etapów aż do limitu tokenów*

**Rozumowanie krok po kroku** – Do problemów wymagających widocznej logiki. Model pokazuje wyraźne rozumowanie dla każdego kroku. Użyj do zadań matematycznych, łamigłówek logicznych lub gdy chcesz zrozumieć proces myślowy.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pl/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Rozbijanie problemów na wyraźne, logiczne kroki*

**Ograniczona odpowiedź** – Do odpowiedzi z określonymi wymogami formatowania. Model ściśle przestrzega reguł formatu i długości. Użyj do podsumowań lub gdy potrzebujesz precyzyjnej struktury outputu.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pl/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Wymuszanie określonych wymogów formatowania, długości i struktury*

## Korzystanie z istniejących zasobów Azure

**Sprawdź wdrożenie:**

Upewnij się, że plik `.env` istnieje w katalogu głównym z poświadczeniami Azure (utworzony podczas Modułu 01):

```bash
cat ../.env  # Powinno pokazać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Uruchom aplikację:**

> **Uwaga:** Jeśli już uruchomiłeś wszystkie aplikacje za pomocą `./start-all.sh` z Modułu 01, ten moduł działa już na porcie 8083. Możesz pominąć poniższe polecenia uruchomienia i przejść bezpośrednio do http://localhost:8083.

**Opcja 1: Korzystanie z pulpitu Spring Boot (zalecane dla użytkowników VS Code)**

Kontener deweloperski zawiera rozszerzenie Spring Boot Dashboard, które zapewnia graficzny interfejs do zarządzania wszystkimi aplikacjami Spring Boot. Znajdziesz je na pasku aktywności po lewej stronie VS Code (ikonka Spring Boot).

Z poziomu Spring Boot Dashboard możesz:
- Zobaczyć wszystkie dostępne aplikacje Spring Boot w przestrzeni roboczej
- Uruchamiać/zatrzymywać aplikacje jednym kliknięciem
- Podglądać logi aplikacji na żywo
- Monitorować status aplikacji

Wystarczy kliknąć przycisk play obok "prompt-engineering", aby uruchomić ten moduł lub uruchomić wszystkie moduły jednocześnie.

<img src="../../../translated_images/pl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opcja 2: Korzystanie ze skryptów powłoki**

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
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

Oba skrypty automatycznie ładują zmienne środowiskowe z pliku `.env` w katalogu głównym i zbudują pliki JAR, jeśli nie istnieją.

> **Uwaga:** Jeśli wolisz zbudować moduły ręcznie przed uruchomieniem:
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

Otwórz http://localhost:8083 w przeglądarce.

**Aby zatrzymać:**

**Bash:**
```bash
./stop.sh  # Ten moduł tylko
# Lub
cd .. && ./stop-all.sh  # Wszystkie moduły
```

**PowerShell:**
```powershell
.\stop.ps1  # Tylko ten moduł
# Lub
cd ..; .\stop-all.ps1  # Wszystkie moduły
```

## Zrzuty ekranu aplikacji

<img src="../../../translated_images/pl/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Główny pulpit pokazujący wszystkie 8 wzorców inżynierii promptów wraz z ich cechami i zastosowaniami*

## Eksploracja wzorców

Interfejs webowy pozwala eksperymentować z różnymi strategiami promptowania. Każdy wzorzec rozwiązuje inne problemy – wypróbuj je, aby zobaczyć, kiedy które podejście działa najlepiej.

### Niskie vs wysokie zaangażowanie

Zadaj proste pytanie jak "Ile to jest 15% z 200?" używając niskiego zaangażowania. Otrzymasz natychmiastową, bezpośrednią odpowiedź. Teraz zapytaj coś złożonego, np. "Zaprojektuj strategię buforowania dla API o dużym ruchu" przy użyciu wysokiego zaangażowania. Obserwuj, jak model zwalnia i podaje szczegółowe rozumowanie. Ten sam model, ta sama struktura pytania – ale prompt mówi mu, ile ma myśleć.
<img src="../../../translated_images/pl/low-eagerness-demo.898894591fb23aa0.webp" alt="Demo niskiej ochoty" width="800"/>

*Szybkie obliczenie z minimalnym rozumowaniem*

<img src="../../../translated_images/pl/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Demo wysokiej ochoty" width="800"/>

*Kompleksowa strategia buforowania (2.8MB)*

### Wykonywanie zadań (Wprowadzenia narzędziowe)

Wieloetapowe przepływy pracy korzystają na wcześniejszym planowaniu i narracji postępu. Model opisuje, co zrobi, relacjonuje każdy krok, a następnie podsumowuje wyniki.

<img src="../../../translated_images/pl/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Demo wykonywania zadań" width="800"/>

*Tworzenie punktu końcowego REST z narracją krok po kroku (3.9MB)*

### Samorefleksyjny kod

Spróbuj "Utwórz usługę walidacji e-mail". Zamiast po prostu wygenerować kod i się zatrzymać, model generuje, ocenia względem kryteriów jakości, identyfikuje słabości i wprowadza poprawki. Zobaczysz, jak iteruje, aż kod spełni standardy produkcyjne.

<img src="../../../translated_images/pl/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Demo samorefleksyjnego kodu" width="800"/>

*Kompletna usługa walidacji e-mail (5.2MB)*

### Strukturalna analiza

Przeglądy kodu wymagają spójnych ram oceny. Model analizuje kod, używając stałych kategorii (poprawność, praktyki, wydajność, bezpieczeństwo) z poziomami powagi.

<img src="../../../translated_images/pl/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Demo strukturalnej analizy" width="800"/>

*Przegląd kodu oparty na ramach*

### Rozmowa wieloetapowa

Zapytaj "Co to jest Spring Boot?", a następnie od razu dodaj "Pokaż mi przykład". Model pamięta Twoje pierwsze pytanie i podaje przykład Spring Boota specjalnie dla Ciebie. Bez pamięci drugie pytanie byłoby zbyt ogólne.

<img src="../../../translated_images/pl/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Demo rozmowy wieloetapowej" width="800"/>

*Zachowanie kontekstu między pytaniami*

### Rozumowanie krok po kroku

Wybierz zadanie matematyczne i spróbuj z Rozumowaniem krok po kroku oraz niską ochotą. Niska ochota daje tylko odpowiedź - szybko, ale niejasno. Krok po kroku pokazuje każde obliczenie i decyzję.

<img src="../../../translated_images/pl/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Demo rozumowania krok po kroku" width="800"/>

*Zadanie matematyczne z wyraźnymi krokami*

### Ograniczona odpowiedź

Gdy potrzebujesz konkretnych formatów lub liczby słów, ten wzór wymusza ścisłe przestrzeganie. Spróbuj wygenerować podsumowanie zawierające dokładnie 100 słów w formie punktów.

<img src="../../../translated_images/pl/constrained-output-demo.567cc45b75da1633.webp" alt="Demo ograniczonej odpowiedzi" width="800"/>

*Podsumowanie uczenia maszynowego z kontrolą formatu*

## Czego naprawdę się uczysz

**Wysiłek rozumowania zmienia wszystko**

GPT-5.2 pozwala kontrolować nakład obliczeniowy przez wskazówki. Niski wysiłek oznacza szybkie odpowiedzi z minimalnym badaniem. Wysoki wysiłek oznacza, że model poświęca czas na głębokie myślenie. Uczysz się dopasowywać wysiłek do złożoności zadania - nie trać czasu na proste pytania, ale też nie śpiesz się z trudnymi decyzjami.

**Struktura kieruje zachowaniem**

Zwróć uwagę na znaczniki XML w wskazówkach? Nie są ozdobne. Modele bardziej niezawodnie wykonują uporządkowane instrukcje niż swobodny tekst. Gdy potrzebujesz procesów wieloetapowych lub złożonej logiki, struktura pomaga modelowi śledzić, gdzie jest i co dalej.

<img src="../../../translated_images/pl/prompt-structure.a77763d63f4e2f89.webp" alt="Struktura promptu" width="800"/>

*Anatomia dobrze zorganizowanego promptu z jasnymi sekcjami i organizacją w stylu XML*

**Jakość przez samoocenę**

Wzory samorefleksji działają przez wyraźne określenie kryteriów jakości. Zamiast mieć nadzieję, że model "zrobi to dobrze", mówisz mu dokładnie, co znaczy "dobrze": poprawna logika, obsługa błędów, wydajność, bezpieczeństwo. Model może ocenić własne wyniki i się poprawić. To zmienia generowanie kodu z loterii w proces.

**Kontekst jest ograniczony**

Rozmowy wieloetapowe działają przez dołączanie historii wiadomości do każdego zapytania. Ale jest limit - każdy model ma maksymalną liczbę tokenów. W miarę wzrostu rozmów potrzebujesz strategii, by utrzymać istotny kontekst bez przekraczania limitu. Ten moduł pokazuje, jak działa pamięć; później nauczysz się, kiedy streszczać, kiedy zapominać, a kiedy pobierać.

## Kolejne kroki

**Następny moduł:** [03-rag - RAG (Generowanie wspomagane wyszukiwaniem)](../03-rag/README.md)

---

**Nawigacja:** [← Poprzedni: Moduł 01 - Wprowadzenie](../01-introduction/README.md) | [Powrót do głównego](../README.md) | [Następny: Moduł 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:  
Niniejszy dokument został przetłumaczony za pomocą usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Chociaż dokładamy starań, aby tłumaczenie było jak najdokładniejsze, prosimy mieć na uwadze, że automatyczne tłumaczenia mogą zawierać błędy lub nieścisłości. Oryginalny dokument w języku źródłowym powinien być uznawany za dokument autorytatywny. W przypadku informacji krytycznych zalecamy skorzystanie z profesjonalnego tłumaczenia wykonywanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->