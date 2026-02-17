# Moduł 02: Inżynieria Promptów z GPT-5.2

## Spis treści

- [Czego się nauczysz](../../../02-prompt-engineering)
- [Wymagania wstępne](../../../02-prompt-engineering)
- [Zrozumienie inżynierii promptów](../../../02-prompt-engineering)
- [Podstawy inżynierii promptów](../../../02-prompt-engineering)
  - [Zero-shot prompting](../../../02-prompt-engineering)
  - [Few-shot prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Szablony promptów](../../../02-prompt-engineering)
- [Zaawansowane wzorce](../../../02-prompt-engineering)
- [Używanie istniejących zasobów Azure](../../../02-prompt-engineering)
- [Zrzuty ekranu aplikacji](../../../02-prompt-engineering)
- [Eksploracja wzorców](../../../02-prompt-engineering)
  - [Niska vs wysoka chęć](../../../02-prompt-engineering)
  - [Wykonywanie zadań (wprowadzenia do narzędzi)](../../../02-prompt-engineering)
  - [Kod samooceniający się](../../../02-prompt-engineering)
  - [Analiza strukturalna](../../../02-prompt-engineering)
  - [Rozmowa wieloetapowa](../../../02-prompt-engineering)
  - [Rozumowanie krok po kroku](../../../02-prompt-engineering)
  - [Ograniczone dane wyjściowe](../../../02-prompt-engineering)
- [Co naprawdę się uczysz](../../../02-prompt-engineering)
- [Kolejne kroki](../../../02-prompt-engineering)

## Czego się nauczysz

<img src="../../../translated_images/pl/what-youll-learn.c68269ac048503b2.webp" alt="Czego się nauczysz" width="800"/>

W poprzednim module zobaczyłeś, jak pamięć umożliwia konwersacyjną AI i używałeś modeli GitHub do podstawowych interakcji. Teraz skoncentrujemy się na tym, jak zadajesz pytania — czyli na samych promptach — wykorzystując GPT-5.2 z Azure OpenAI. Sposób, w jaki strukturujesz prompt, dramatycznie wpływa na jakość uzyskiwanych odpowiedzi. Zaczniemy od przeglądu podstawowych technik promptowania, a następnie przejdziemy do ośmiu zaawansowanych wzorców, które w pełni wykorzystują możliwości GPT-5.2.

Użyjemy GPT-5.2, ponieważ wprowadza on kontrolę rozumowania — możesz powiedzieć modelowi, ile ma myśleć przed odpowiedzią. To sprawia, że różne strategie promptowania stają się bardziej oczywiste i pomaga zrozumieć, kiedy stosować które podejście. Skorzystamy również z mniejszych ograniczeń limitów dla GPT-5.2 w Azure w porównaniu do modeli GitHub.

## Wymagania wstępne

- Ukończony Moduł 01 (zasoby Azure OpenAI wdrożone)
- Plik `.env` w katalogu głównym z poświadczeniami Azure (utworzony przez `azd up` w Module 01)

> **Uwaga:** Jeśli nie ukończyłeś Modułu 01, najpierw postępuj zgodnie z instrukcjami wdrożenia tam.

## Zrozumienie inżynierii promptów

<img src="../../../translated_images/pl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Czym jest inżynieria promptów?" width="800"/>

Inżynieria promptów polega na projektowaniu tekstu wejściowego, który konsekwentnie dostarcza potrzebne wyniki. To nie tylko zadawanie pytań — chodzi o strukturę zapytań, tak aby model dokładnie rozumiał, czego chcesz i jak to dostarczyć.

Pomyśl o tym jak o udzielaniu instrukcji współpracownikowi. „Napraw błąd” jest niejasne. „Napraw wyjątek null pointer w UserService.java, linia 45, dodając sprawdzenie na null” jest konkretne. Modele językowe działają tak samo — konkretność i struktura mają znaczenie.

<img src="../../../translated_images/pl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Jak pasuje LangChain4j" width="800"/>

LangChain4j zapewnia infrastrukturę — połączenia z modelami, pamięć i typy wiadomości — podczas gdy wzorce promptów to po prostu starannie ustrukturyzowany tekst wysyłany przez tę infrastrukturę. Kluczowymi elementami są `SystemMessage` (ustawia zachowanie i rolę AI) oraz `UserMessage` (przekazuje Twoją rzeczywistą prośbę).

## Podstawy inżynierii promptów

<img src="../../../translated_images/pl/five-patterns-overview.160f35045ffd2a94.webp" alt="Przegląd pięciu wzorców inżynierii promptów" width="800"/>

Zanim przejdziemy do zaawansowanych wzorców w tym module, przejrzyjmy pięć podstawowych technik promptowania. To podstawowe elementy konstrukcyjne, które każdy inżynier promptów powinien znać. Jeśli pracowałeś już z [modułem szybkiego startu](../00-quick-start/README.md#2-prompt-patterns), widziałeś je w akcji — oto koncepcyjne ramy ich działania.

### Zero-shot prompting

Najprostsze podejście: daj modelowi bezpośrednią instrukcję bez przykładów. Model polega całkowicie na swoim treningu, aby zrozumieć i wykonać zadanie. Działa to dobrze dla prostych zapytań, gdzie oczekiwane zachowanie jest oczywiste.

<img src="../../../translated_images/pl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-shot prompting" width="800"/>

*Bezpośrednia instrukcja bez przykładów — model wywnioskowuje zadanie tylko z instrukcji*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odpowiedź: "Pozytywny"
```

**Kiedy używać:** Proste klasyfikacje, bezpośrednie pytania, tłumaczenia lub każde zadanie, które model może wykonać bez dodatkowych wskazówek.

### Few-shot prompting

Podajesz przykłady, które pokazują wzór, jakiego model ma się trzymać. Model uczy się spodziewanego formatu wejścia i wyjścia z Twoich przykładów i stosuje to do nowych danych. To znacznie poprawia spójność tam, gdzie pożądany format lub zachowanie nie jest oczywiste.

<img src="../../../translated_images/pl/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-shot prompting" width="800"/>

*Uczenie się na przykładach — model identyfikuje wzór i stosuje go do nowych danych*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**Kiedy używać:** Niestandardowe klasyfikacje, spójne formatowanie, zadania specyficzne dla domeny lub gdy wyniki zero-shot są niespójne.

### Chain of Thought

Proś model o pokazanie swojego rozumowania krok po kroku. Zamiast od razu podawać odpowiedź, model rozbija problem i explicite przechodzi przez każdy etap. To poprawia dokładność w zadaniach z matematyki, logiki i wieloetapowego rozumowania.

<img src="../../../translated_images/pl/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Rozumowanie krok po kroku — rozbijanie złożonych problemów na jawne logiczne kroki*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model pokazuje: 15 - 8 = 7, następnie 7 + 12 = 19 jabłek
```

**Kiedy używać:** Zadania matematyczne, zagadki logiczne, debugowanie lub każde zadanie, gdzie pokazanie procesu rozumowania poprawia dokładność i zaufanie.

### Role-Based Prompting

Ustaw personę lub rolę dla AI przed zadaniem pytania. To dostarcza kontekstu, który kształtuje ton, głębokość i fokus odpowiedzi. „Architekt oprogramowania” daje inne rady niż „młodszy programista” czy „audytor bezpieczeństwa”.

<img src="../../../translated_images/pl/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Ustawianie kontekstu i persony — to samo pytanie dostaje inną odpowiedź zależnie od przypisanej roli*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**Kiedy używać:** Przeglądy kodu, korepetycje, analiza specyficzna dla domeny lub gdy potrzebujesz odpowiedzi dopasowanych do poziomu wiedzy lub perspektywy.

### Szablony promptów

Twórz wielokrotnego użytku promptów z zmiennymi zastępczymi. Zamiast pisać nowy prompt za każdym razem, zdefiniuj szablon i wypełniaj różne wartości. Klasa `PromptTemplate` LangChain4j ułatwia to ze składnią `{{variable}}`.

<img src="../../../translated_images/pl/prompt-templates.14bfc37d45f1a933.webp" alt="Szablony promptów" width="800"/>

*Wielokrotnego użytku prompt z miejscami na zmienne — jeden szablon, wiele zastosowań*

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

**Kiedy używać:** Powtarzane zapytania z różnymi danymi, przetwarzanie wsadowe, budowanie wielokrotnego użytku przepływów AI lub każdy scenariusz, gdzie struktura promptu pozostaje taka sama, ale zmieniają się dane.

---

Te pięć podstaw zapewnia solidne narzędzia dla większości zadań promptowania. Reszta modułu opiera się na nich i prezentuje **osiem zaawansowanych wzorców**, które wykorzystują kontrolę rozumowania, samoocenę i możliwości ustrukturyzowanego wyjścia GPT-5.2.

## Zaawansowane wzorce

Po omówieniu podstaw, przejdźmy do ośmiu zaawansowanych wzorców, które czynią ten moduł wyjątkowym. Nie wszystkie problemy wymagają tego samego podejścia. Niektóre pytania potrzebują szybkich odpowiedzi, inne głębokiego myślenia. Niektóre potrzebują widocznego rozumowania, inne tylko wyników. Każdy wzorzec poniżej jest zoptymalizowany pod inny scenariusz — a kontrola rozumowania GPT-5.2 uwydatnia te różnice.

<img src="../../../translated_images/pl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osiem wzorców promptowania" width="800"/>

*Przegląd ośmiu wzorców inżynierii promptów i ich zastosowań*

<img src="../../../translated_images/pl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrola rozumowania z GPT-5.2" width="800"/>

*Kontrola rozumowania GPT-5.2 pozwala określić, ile myślenia model ma wykonać — od szybkich, bezpośrednich odpowiedzi po dogłębną eksplorację*

<img src="../../../translated_images/pl/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Porównanie wysiłku rozumowania" width="800"/>

*Niska chęć (szybka, bezpośrednia) vs wysoka chęć (dokonująca pełnej, eksploracyjnej analizy)*

**Niska chęć (Szybka i skoncentrowana)** – Do prostych pytań, gdy chcesz szybkich, bezpośrednich odpowiedzi. Model wykonuje minimalne rozumowanie - maksymalnie 2 kroki. Używaj tego do obliczeń, wyszukiwań lub prostych pytań.

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Eksploruj z GitHub Copilot:** Otwórz [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) i zapytaj:
> - "Jaka jest różnica między wzorcami promptowania niskiej a wysokiej chęci?"
> - "Jak znaczniki XML w promptach pomagają ustrukturyzować odpowiedź AI?"
> - "Kiedy powinienem używać wzorców samo-refleksji, a kiedy bezpośrednich instrukcji?"

**Wysoka chęć (Głębokie i dokładne)** – Do złożonych problemów, gdzie chcesz kompleksowej analizy. Model eksploruje gruntownie i pokazuje szczegółowe rozumowanie. Używaj tego do projektowania systemów, decyzji architektonicznych lub złożonych badań.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Wykonywanie zadań (postęp krok po kroku)** – Do wieloetapowych przepływów pracy. Model daje uprzedni plan, opowiada o każdym kroku podczas pracy, a następnie podsumowuje. Używaj tego do migracji, implementacji lub każdego procesu wieloetapowego.

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought prompting explicite prosi model o pokazanie procesu myślenia, co poprawia dokładność dla złożonych zadań. Rozbijanie krok po kroku pomaga zarówno ludziom, jak i AI zrozumieć logikę.

> **🤖 Spróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Zapytaj o ten wzorzec:
> - "Jak dostosować wzorzec wykonywania zadań do długotrwałych operacji?"
> - "Jakie są najlepsze praktyki tworzenia wprowadzeń do narzędzi w aplikacjach produkcyjnych?"
> - "Jak mogę przechwytywać i wyświetlać pośrednie raporty postępu w interfejsie użytkownika?"

<img src="../../../translated_images/pl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Wzorzec wykonywania zadań" width="800"/>

*Planowanie → Wykonanie → Podsumowanie przepływu dla zadań wieloetapowych*

**Kod samooceniający się** – Do generowania kodu jakości produkcyjnej. Model generuje kod zgodny ze standardami produkcji z prawidłową obsługą błędów. Używaj go do budowy nowych funkcji lub usług.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cykl samorefleksji" width="800"/>

*Iteracyjna pętla poprawy – generuj, oceniaj, identyfikuj błędy, poprawiaj, powtarzaj*

**Analiza strukturalna** – Do spójnej oceny. Model przegląda kod używając ustalonego frameworku (poprawność, praktyki, wydajność, bezpieczeństwo, utrzymywalność). Używaj tego do przeglądów kodu lub oceny jakości.

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Spróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Zapytaj o analizę strukturalną:
> - "Jak mogę dostosować ramy analizy do różnych typów przeglądów kodu?"
> - "Jaki jest najlepszy sposób na programowe parsowanie i wykorzystanie ustrukturyzowanego wyjścia?"
> - "Jak zapewnić spójne poziomy ważności podczas różnych sesji przeglądów?"

<img src="../../../translated_images/pl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Wzorzec analizy strukturalnej" width="800"/>

*Ramka dla spójnych przeglądów kodu z poziomami ważności*

**Rozmowa wieloetapowa** – Do konwersacji wymagających kontekstu. Model pamięta poprzednie wiadomości i buduje na nich. Używaj tego do interaktywnych sesji pomocy lub złożonego Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/pl/context-memory.dff30ad9fa78832a.webp" alt="Pamięć kontekstu" width="800"/>

*Jak kontekst rozmowy kumuluje się przez wiele tur aż do limitu tokenów*

**Rozumowanie krok po kroku** – Do problemów wymagających widocznej logiki. Model pokazuje explicite rozumowanie każdego kroku. Używaj tego do zadań matematycznych, łamigłówek logicznych lub gdy chcesz zrozumieć proces myślenia.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pl/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Wzorzec krok po kroku" width="800"/>

*Rozbijanie problemów na explicite logiczne kroki*

**Ograniczone dane wyjściowe** – Do odpowiedzi o ściśle określonym formacie. Model ściśle przestrzega reguł formatu i długości. Używaj tego do podsumowań lub gdy potrzebujesz precyzyjnej struktury wyjścia.

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

<img src="../../../translated_images/pl/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Wzorzec ograniczonych wyjść" width="800"/>

*Egzekwowanie konkretnych wymagań dotyczących formatu, długości i struktury*

## Używanie istniejących zasobów Azure

**Sprawdź wdrożenie:**

Upewnij się, że plik `.env` istnieje w katalogu głównym z poświadczeniami Azure (utworzony podczas Modułu 01):
```bash
cat ../.env  # Powinno pokazać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Uruchom aplikację:**

> **Uwaga:** Jeśli już uruchomiłeś wszystkie aplikacje za pomocą `./start-all.sh` z Modułu 01, ten moduł już działa na porcie 8083. Możesz pominąć poniższe polecenia startowe i przejść bezpośrednio do http://localhost:8083.

**Opcja 1: Użycie Spring Boot Dashboard (zalecane dla użytkowników VS Code)**

Kontener deweloperski zawiera rozszerzenie Spring Boot Dashboard, które zapewnia wizualny interfejs do zarządzania wszystkimi aplikacjami Spring Boot. Znajdziesz je na pasku aktywności po lewej stronie VS Code (poszukaj ikony Spring Boot).
Z poziomu Spring Boot Dashboard możesz:
- Zobaczyć wszystkie dostępne aplikacje Spring Boot w obszarze roboczym
- Uruchamiać/zatrzymywać aplikacje jednym kliknięciem
- Oglądać logi aplikacji w czasie rzeczywistym
- Monitorować status aplikacji

Wystarczy kliknąć przycisk odtwarzania obok "prompt-engineering", aby uruchomić ten moduł, lub uruchomić wszystkie moduły naraz.

<img src="../../../translated_images/pl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opcja 2: Użycie skryptów shell**

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

> **Uwaga:** Jeśli wolisz samodzielnie zbudować wszystkie moduły przed uruchomieniem:
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

Otwórz w przeglądarce http://localhost:8083.

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

## Zrzuty ekranu aplikacji

<img src="../../../translated_images/pl/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Główny dashboard pokazujący wszystkie 8 wzorców inżynierii promptów wraz z ich cechami i przypadkami użycia*

## Eksploracja Wzorców

Interfejs webowy pozwala eksperymentować z różnymi strategiami tworzenia promptów. Każdy wzorzec rozwiązuje inne problemy – wypróbuj je, aby zobaczyć, kiedy każda metoda działa najlepiej.

### Niska vs Wysoka Gotowość (Eagerness)

Zadaj proste pytanie, np. „Jaka jest 15% z 200?” używając Niskiej Gotowości. Otrzymasz natychmiastową, bezpośrednią odpowiedź. Teraz zapytaj coś skomplikowanego, np. „Zaprojektuj strategię cache’owania dla API o wysokim natężeniu ruchu” używając Wysokiej Gotowości. Zobacz, jak model zwalnia i udziela szczegółowych rozważań. Ten sam model, ta sama struktura pytania – ale prompt mówi mu, ile myślenia ma wykonać.

<img src="../../../translated_images/pl/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Szybkie obliczenie z minimalnymi rozważaniami*

<img src="../../../translated_images/pl/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Kompleksowa strategia cache’owania (2,8 MB)*

### Wykonywanie zadań (Wprowadzenia narzędziowe)

Wielostopniowe scenariusze korzystają na planowaniu i narracji postępów. Model opisuje, co zamierza zrobić, relacjonuje każdy krok, a potem podsumowuje wyniki.

<img src="../../../translated_images/pl/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Tworzenie endpointu REST z narracją krok po kroku (3,9 MB)*

### Samorefleksyjny kod

Wypróbuj „Utwórz serwis walidacji e-maili”. Zamiast tylko generować kod i kończyć, model generuje, ocenia pod kątem kryteriów jakości, identyfikuje słabe punkty i poprawia. Zobaczysz, jak iteruje, aż kod spełni standardy produkcyjne.

<img src="../../../translated_images/pl/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Kompletny serwis walidacji e-maili (5,2 MB)*

### Analiza Strukturalna

Przeglądy kodu wymagają spójnych ram ewaluacji. Model analizuje kod z użyciem stałych kategorii (poprawność, praktyki, wydajność, bezpieczeństwo) oraz poziomów ważności.

<img src="../../../translated_images/pl/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Przegląd kodu na bazie utworzonego frameworku*

### Wieloetapowy Chat

Zapytaj „Co to jest Spring Boot?”, a następnie natychmiast „Pokaż przykład”. Model pamięta pierwsze pytanie i odpowiada przykładem Spring Boot. Bez pamięci drugie pytanie byłoby zbyt niejasne.

<img src="../../../translated_images/pl/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Zachowanie kontekstu pomiędzy pytaniami*

### Rozumowanie krok po kroku

Wybierz zadanie matematyczne i spróbuj je rozwiązać zarówno z Rozumowaniem krok po kroku, jak i Niską Gotowością. Niska gotowość daje odpowiedź szybko – ale nieprzejrzystą. Rozumowanie krok po kroku pokazuje wszystkie obliczenia i decyzje.

<img src="../../../translated_images/pl/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Zadanie matematyczne z wyraźnymi krokami*

### Ograniczona odpowiedź

Gdy potrzebujesz określonych formatów lub liczby słów, ten wzorzec wymusza ścisłe przestrzeganie wymagań. Spróbuj wygenerować streszczenie dokładnie w 100 słowach w formacie punktów.

<img src="../../../translated_images/pl/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Streszczenie uczenia maszynowego z kontrolą formatu*

## Czego naprawdę się uczysz

**Wysiłek myślenia zmienia wszystko**

GPT-5.2 pozwala kontrolować nakład obliczeniowy poprzez twoje prompt'y. Niski wysiłek oznacza szybkie odpowiedzi z minimalnym badaniem. Wysoki wysiłek oznacza, że model poświęca czas na głębokie rozważania. Uczysz się dopasowywać wysiłek do złożoności zadania – nie marnuj czasu na proste pytania, ale też nie śpiesz się z decyzjami złożonymi.

**Struktura kieruje zachowaniem**

Zauważyłeś tagi XML w promptach? Nie są ozdobą. Modele lepiej wykonują instrukcje, gdy są one strukturalne niż kiedy są to luźne teksty. Gdy potrzebujesz wieloetapowych procesów lub złożonej logiki, struktura pomaga modelowi śledzić, gdzie jest i co ma zrobić dalej.

<img src="../../../translated_images/pl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomia dobrze ustrukturyzowanego promptu z wyraźnymi sekcjami i organizacją w stylu XML*

**Jakość przez samoewaluację**

Wzorce samorefleksyjne działają dzięki wyraźnemu określeniu kryteriów jakości. Zamiast mieć nadzieję, że model „zrobi to dobrze”, mówisz mu dokładnie, co oznacza „dobrze”: poprawna logika, obsługa błędów, wydajność, bezpieczeństwo. Model może potem ocenić własny wynik i poprawić go. To zmienia generowanie kodu z loterii w proces.

**Kontekst jest ograniczony**

Wielokrotny chat działa przez dołączanie historii wiadomości do każdego zapytania. Ale jest limit – każdy model ma maksymalną liczbę tokenów. W miarę rozwoju rozmów będziesz potrzebować strategii, by zachować istotny kontekst bez przekraczania limitu. Ten moduł pokazuje, jak działa pamięć; później nauczysz się, kiedy podsumowywać, kiedy zapominać, a kiedy pobierać dane.

## Kolejne kroki

**Następny moduł:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Nawigacja:** [← Poprzedni: Moduł 01 - Wprowadzenie](../01-introduction/README.md) | [Powrót do głównego](../README.md) | [Następny: Moduł 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:  
Niniejszy dokument został przetłumaczony za pomocą automatycznej usługi tłumaczeniowej AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mimo że dążymy do jak największej dokładności, prosimy mieć na uwadze, że tłumaczenia automatyczne mogą zawierać błędy lub nieścisłości. Oryginalny dokument w języku źródłowym powinien być uważany za autorytatywne źródło. W przypadku informacji istotnych zalecamy skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za wszelkie nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->