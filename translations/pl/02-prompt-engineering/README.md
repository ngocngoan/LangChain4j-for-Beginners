# Moduł 02: Inżynieria Podpowiedzi z GPT-5.2

## Spis Treści

- [Czego się Nauczysz](../../../02-prompt-engineering)
- [Wymagania Wstępne](../../../02-prompt-engineering)
- [Zrozumienie Inżynierii Podpowiedzi](../../../02-prompt-engineering)
- [Podstawy Inżynierii Podpowiedzi](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Szablony Podpowiedzi](../../../02-prompt-engineering)
- [Zaawansowane Wzorce](../../../02-prompt-engineering)
- [Korzystanie z Istniejących Zasobów Azure](../../../02-prompt-engineering)
- [Zrzuty Ekranu Aplikacji](../../../02-prompt-engineering)
- [Eksploracja Wzorców](../../../02-prompt-engineering)
  - [Niskie vs Wysokie Zapały](../../../02-prompt-engineering)
  - [Wykonywanie Zadań (Wprowadzenia do Narzędzi)](../../../02-prompt-engineering)
  - [Kod z Auto-Refleksją](../../../02-prompt-engineering)
  - [Analiza Strukturalna](../../../02-prompt-engineering)
  - [Wieloturowy Chat](../../../02-prompt-engineering)
  - [Rozumowanie Krok po Kroku](../../../02-prompt-engineering)
  - [Ograniczona Odpowiedź](../../../02-prompt-engineering)
- [Czego Naprawdę się Uczysz](../../../02-prompt-engineering)
- [Kolejne Kroki](../../../02-prompt-engineering)

## Czego się Nauczysz

<img src="../../../translated_images/pl/what-youll-learn.c68269ac048503b2.webp" alt="Czego się Nauczysz" width="800"/>

W poprzednim module dowiedziałeś się, jak pamięć umożliwia konwersacyjną AI i używałeś modeli GitHub do podstawowych interakcji. Teraz skupimy się na tym, jak zadajesz pytania — same podpowiedzi — wykorzystując Azure OpenAI GPT-5.2. Sposób, w jaki formułujesz podpowiedzi, znacząco wpływa na jakość otrzymywanych odpowiedzi. Zaczniemy od przeglądu podstawowych technik promptowania, a następnie przejdziemy do ośmiu zaawansowanych wzorców, które w pełni wykorzystują możliwości GPT-5.2.

Używamy GPT-5.2, ponieważ wprowadza kontrolę nad procesem rozumowania — możesz określić, ile model ma myśleć przed odpowiedzią. To wyróżnia różne strategie promptowania i pomaga zrozumieć, kiedy stosować które podejście. Skorzystamy również z mniejszych ograniczeń szybkości w Azure dla GPT-5.2 w porównaniu do modeli GitHub.

## Wymagania Wstępne

- Ukończony Moduł 01 (wdrożone zasoby Azure OpenAI)
- Plik `.env` w katalogu głównym z poświadczeniami Azure (utworzony przez `azd up` w Module 01)

> **Uwaga:** Jeśli nie ukończyłeś Modułu 01, najpierw postępuj zgodnie z instrukcjami wdrożenia tam zawartymi.

## Zrozumienie Inżynierii Podpowiedzi

<img src="../../../translated_images/pl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Czym jest Inżynieria Podpowiedzi?" width="800"/>

Inżynieria podpowiedzi polega na projektowaniu tekstów wejściowych, które konsekwentnie dają wyniki, których potrzebujesz. To nie tylko zadawanie pytań — to strukturyzowanie próśb tak, by model dokładnie rozumiał, czego chcesz i jak to dostarczyć.

Pomyśl o tym jak o dawaniu instrukcji współpracownikowi. „Napraw błąd” jest nieprecyzyjne. „Napraw wyjątek null pointer w UserService.java w linii 45, dodając sprawdzenie na null” jest konkretne. Modele językowe działają tak samo — precyzja i struktura mają znaczenie.

<img src="../../../translated_images/pl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Jak działa LangChain4j" width="800"/>

LangChain4j zapewnia infrastrukturę — połączenia z modelami, pamięć i typy wiadomości — podczas gdy wzorce promptów to po prostu starannie zorganizowany tekst wysyłany przez tę infrastrukturę. Kluczowe elementy to `SystemMessage` (który ustawia zachowanie i rolę AI) oraz `UserMessage` (który niesie twoją rzeczywistą prośbę).

## Podstawy Inżynierii Podpowiedzi

<img src="../../../translated_images/pl/five-patterns-overview.160f35045ffd2a94.webp" alt="Przegląd pięciu wzorców inżynierii podpowiedzi" width="800"/>

Zanim zagłębimy się w zaawansowane wzorce tego modułu, przejrzyjmy pięć podstawowych technik promptowania. To fundamenty, które każdy inżynier podpowiedzi powinien znać. Jeśli ukończyłeś już [moduł szybkiego startu](../00-quick-start/README.md#2-prompt-patterns), widziałeś je w praktyce — oto koncepcyjna podstawa za nimi.

### Zero-Shot Prompting

Najprostsze podejście: podaj modelowi bezpośrednią instrukcję bez przykładów. Model polega całkowicie na swoim treningu, by zrozumieć i wykonać zadanie. Działa dobrze w przypadku prostych próśb, gdzie oczekiwane zachowanie jest oczywiste.

<img src="../../../translated_images/pl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Bezpośrednia instrukcja bez przykładów — model wywnioskowuje zadanie tylko z instrukcji*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odpowiedź: "Pozytywna"
```

**Kiedy używać:** Proste klasyfikacje, pytania bezpośrednie, tłumaczenia lub każde zadanie, które model potrafi wykonać bez dodatkowych wskazówek.

### Few-Shot Prompting

Podaj przykłady pokazujące wzorzec, którego chcesz, aby model się nauczył. Model uczy się oczekiwanego formatu wejścia-wyjścia z twoich przykładów i stosuje go do nowych danych. To znacznie poprawia spójność w zadaniach, gdzie oczekiwany format lub zachowanie nie są oczywiste.

<img src="../../../translated_images/pl/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Uczenie się na przykładach — model rozpoznaje wzorzec i stosuje go do nowych danych*

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

**Kiedy używać:** Niestandardowe klasyfikacje, spójne formatowanie, zadania specyficzne dla dziedziny lub gdy wyniki zero-shot są niespójne.

### Chain of Thought

Poproś model, aby pokazał swoje rozumowanie krok po kroku. Zamiast przechodzić od razu do odpowiedzi, model rozbija problem i przepracowuje go etapami. Poprawia to dokładność w zadaniach matematycznych, logicznych i wieloetapowym rozumowaniu.

<img src="../../../translated_images/pl/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Rozumowanie krok po kroku — dzielenie złożonych problemów na jawne logiczne kroki*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model pokazuje: 15 - 8 = 7, następnie 7 + 12 = 19 jabłek
```

**Kiedy używać:** Problemy matematyczne, łamigłówki logiczne, debugowanie lub każde zadanie, dla którego pokazywanie procesu rozumowania zwiększa dokładność i zaufanie.

### Role-Based Prompting

Ustaw personę lub rolę dla AI przed zadaniem pytania. To dostarcza kontekst, który wpływa na ton, głębokość i ukierunkowanie odpowiedzi. „Architekt oprogramowania” da inne rady niż „młodszy programista” czy „audytor bezpieczeństwa”.

<img src="../../../translated_images/pl/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Ustawianie kontekstu i persony — to samo pytanie otrzymuje różne odpowiedzi w zależności od przypisanej roli*

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

**Kiedy używać:** Przeglądy kodu, tutoring, analizy specyficzne dla domeny lub gdy potrzebujesz odpowiedzi dopasowanych do poziomu ekspertyzy lub perspektywy.

### Szablony Podpowiedzi

Twórz wielokrotnego użytku podpowiedzi z miejscami na zmienne. Zamiast pisać nową podpowiedź za każdym razem, zdefiniuj szablon raz i uzupełniaj różne wartości. Klasa `PromptTemplate` LangChain4j ułatwia to składnią `{{zmienna}}`.

<img src="../../../translated_images/pl/prompt-templates.14bfc37d45f1a933.webp" alt="Szablony podpowiedzi" width="800"/>

*Wielokrotnego użytku podpowiedzi z miejscami na zmienne — jeden szablon, wiele zastosowań*

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

**Kiedy używać:** Powtarzające się zapytania z różnymi danymi, przetwarzanie wsadowe, budowanie wielokrotnego użytku przepływów AI lub każdy scenariusz, gdzie struktura podpowiedzi pozostaje taka sama, a zmieniają się dane.

---

Te pięć fundamentów daje solidne narzędzia do większości zadań promptowania. Reszta modułu opiera się na nich wprowadzając **osiem zaawansowanych wzorców**, korzystających z kontroli rozumowania, samooceny i strukturalnego outputu GPT-5.2.

## Zaawansowane Wzorce

Po zapoznaniu się z fundamentami przejdźmy do ośmiu zaawansowanych wzorców, które czynią ten moduł wyjątkowym. Nie wszystkie problemy wymagają takiego samego podejścia. Niektóre pytania potrzebują szybkich odpowiedzi, inne głębokiego rozpatrzenia. Niektóre wymagają widocznego rozumowania, inne tylko wyników. Każdy z poniższych wzorców jest zoptymalizowany pod inny scenariusz — a kontrola rozumowania GPT-5.2 podkreśla te różnice jeszcze mocniej.

<img src="../../../translated_images/pl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osiem wzorców promptowania" width="800"/>

*Przegląd ośmiu wzorców inżynierii podpowiedzi i ich zastosowań*

<img src="../../../translated_images/pl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrola rozumowania w GPT-5.2" width="800"/>

*Kontrola rozumowania GPT-5.2 pozwala określić, ile myślenia model ma wykonać — od szybkich, bezpośrednich odpowiedzi po głęboką eksplorację*

**Niski Zapał (Szybkie i Skoncentrowane)** - Dla prostych pytań, gdzie chcesz szybkich, bezpośrednich odpowiedzi. Model wykonuje minimalne rozumowanie - maksymalnie 2 kroki. Używaj tego do obliczeń, wyszukiwań lub prostych pytań.

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

> 💡 **Eksperymentuj z GitHub Copilot:** Otwórz [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) i zapytaj:
> - „Jaka jest różnica między niskim a wysokim zapałem w wzorcach promptowania?”
> - „Jak znaczniki XML w promptach pomagają uporządkować odpowiedź AI?”
> - „Kiedy powinienem używać wzorców z auto-refleksją, a kiedy bezpośrednich instrukcji?”

**Wysoki Zapał (Głębokie i Szczegółowe)** - Do złożonych problemów, gdzie chcesz wyczerpującej analizy. Model eksploruje dogłębnie i pokazuje szczegółowe rozumowanie. Używaj tego do projektowania systemów, decyzji architektonicznych lub skomplikowanych badań.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Wykonywanie Zadań (Postęp Krok po Kroku)** - Dla wieloetapowych procesów. Model przedstawia plan, komentuje każdy krok podczas pracy, a na końcu podsumowuje. Używaj tego do migracji, implementacji lub dowolnego procesu wieloetapowego.

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

Chain-of-Thought explicite prosi model o pokazanie procesu rozumowania, co poprawia dokładność przy złożonych zadaniach. Rozbicie na kroki ułatwia zrozumienie logiki ludziom i AI.

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Zapytaj o ten wzorzec:
> - „Jak dostosować wzorzec wykonywania zadań do operacji długotrwałych?”
> - „Jakie są najlepsze praktyki do strukturyzowania wprowadzeń do narzędzi w aplikacjach produkcyjnych?”
> - „Jak przechwycić i wyświetlać pośrednie aktualizacje postępu w UI?”

<img src="../../../translated_images/pl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Wzorzec wykonywania zadań" width="800"/>

*Plan → Wykonanie → Podsumowanie dla zadań wieloetapowych*

**Kod z Auto-Refleksją** - Do generowania kodu produkcyjnej jakości. Model tworzy kod zgodny ze standardami produkcyjnymi z odpowiednią obsługą błędów. Używaj tego przy budowie nowych funkcji lub usług.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cykl auto-refleksji" width="800"/>

*Iteracyjna pętla ulepszania - generuj, oceniaj, identyfikuj problemy, poprawiaj, powtarzaj*

**Analiza Strukturalna** - Do spójnej oceny. Model przegląda kod używając stałego schematu (poprawność, praktyki, wydajność, bezpieczeństwo, utrzymywalność). Używaj tego do przeglądów kodu lub ocen jakości.

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

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Zapytaj o analizę strukturalną:
> - „Jak dostosować ramy analizy do różnych rodzajów przeglądów kodu?”
> - „Jaki jest najlepszy sposób na programowe parsowanie i reagowanie na strukturalne wyniki?”
> - „Jak zapewnić spójność poziomów ważności pomiędzy sesjami przeglądów?”

<img src="../../../translated_images/pl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Wzorzec analizy strukturalnej" width="800"/>

*Schemat do spójnych przeglądów kodu z poziomami ważności*

**Wieloturowy Chat** - Do rozmów, które potrzebują kontekstu. Model pamięta poprzednie wiadomości i buduje na nich. Używaj tego do interaktywnych sesji pomocy lub skomplikowanych pytań i odpowiedzi.

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

*Jak kontekst rozmowy akumuluje się przez wiele tur aż do limitu tokenów*

**Rozumowanie Krok po Kroku** - Do problemów wymagających widocznej logiki. Model pokazuje jawne rozumowanie dla każdego kroku. Używaj tego do problemów matematycznych, łamigłówek logicznych lub gdy chcesz zrozumieć proces myślenia.

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

*Dzielenie problemów na jawne logiczne kroki*

**Ograniczona Odpowiedź** - Do odpowiedzi z konkretnymi wymogami formatowania. Model ściśle przestrzega reguł formatu i długości. Używaj tego do podsumowań lub gdy potrzebujesz precyzyjnej struktury outputu.

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

<img src="../../../translated_images/pl/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Wzorzec ograniczonego outputu" width="800"/>

*Egzekwowanie określonego formatu, długości i wymagań strukturalnych*

## Korzystanie z Istniejących Zasobów Azure

**Weryfikacja wdrożenia:**

Upewnij się, że plik `.env` istnieje w katalogu głównym z poświadczeniami Azure (utworzony podczas Modułu 01):
```bash
cat ../.env  # Powinno wyświetlać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Uruchom aplikację:**

> **Uwaga:** Jeśli już uruchomiłeś wszystkie aplikacje za pomocą `./start-all.sh` z Modułu 01, ten moduł jest już uruchomiony na porcie 8083. Możesz pominąć poniższe polecenia startowe i przejść bezpośrednio pod adres http://localhost:8083.

**Opcja 1: Korzystanie z Spring Boot Dashboard (zalecane dla użytkowników VS Code)**

Kontener deweloperski zawiera rozszerzenie Spring Boot Dashboard, które zapewnia wizualny interfejs do zarządzania wszystkimi aplikacjami Spring Boot. Znajdziesz go na pasku aktywności po lewej stronie VS Code (szukaj ikony Spring Boot).

Z poziomu Spring Boot Dashboard możesz:
- Zobaczyć wszystkie dostępne aplikacje Spring Boot w obszarze roboczym
- Uruchamiać/zatrzymywać aplikacje jednym kliknięciem
- Oglądać logi aplikacji w czasie rzeczywistym
- Monitorować status aplikacji
Po prostu kliknij przycisk odtwarzania obok "prompt-engineering", aby rozpocząć ten moduł lub uruchom od razu wszystkie moduły.

<img src="../../../translated_images/pl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

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
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
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

*Główny panel pokazujący wszystkie 8 wzorców inżynierii podpowiedzi wraz z ich cechami i zastosowaniami*

## Odkrywanie wzorców

Interfejs webowy pozwala eksperymentować z różnymi strategiami podpowiedzi. Każdy wzorzec rozwiązuje inne problemy – wypróbuj je, by zobaczyć, kiedy sprawdza się każdy z nich.

> **Uwaga: Streaming vs brak streamingu** — Każda strona wzorca oferuje dwa przyciski: **🔴 Stream Response (Live)** oraz opcję **bez streamingu**. Streaming korzysta z Server-Sent Events (SSE), aby wyświetlać tokeny w czasie rzeczywistym, gdy model je generuje, więc widzisz postęp od razu. Opcja bez streamingu czeka na całą odpowiedź przed jej pokazaniem. Przy podpowiedziach powodujących głębokie rozumowanie (np. High Eagerness, Self-Reflecting Code) wywołanie bez streamingu może trwać bardzo długo — czasem minuty — bez widocznej informacji zwrotnej. **Używaj streamingu podczas eksperymentów z złożonymi podpowiedziami**, aby widzieć pracę modelu i uniknąć wrażenia, że żądanie się zawiesiło.
>
> **Uwaga: Wymagania przeglądarki** — Funkcja streamingu korzysta z Fetch Streams API (`response.body.getReader()`), które wymaga pełnej przeglądarki (Chrome, Edge, Firefox, Safari). Nie działa w wbudowanej w VS Code prostej przeglądarce (Simple Browser), ponieważ jej webview nie wspiera ReadableStream API. Jeśli używasz Simple Browser, przyciski bez streamingu będą działać normalnie — tylko przyciski streamingu są ograniczone. Otwórz `http://localhost:8083` w zewnętrznej przeglądarce, aby mieć pełną funkcjonalność.

### Niskie vs Wysokie Zaangażowanie

Zadaj proste pytanie, np. "Ile to jest 15% z 200?" używając Low Eagerness. Otrzymasz natychmiastową, bezpośrednią odpowiedź. Teraz zadaj coś skomplikowanego, np. "Zaprojektuj strategię cache’owania dla API o dużym ruchu" używając High Eagerness. Kliknij **🔴 Stream Response (Live)** i obserwuj, jak pojawia się szczegółowe rozumowanie modelu token po tokenie. Ten sam model, ta sama struktura pytania – ale podpowiedź mówi mu, ile czasu ma poświęcić na myślenie.

### Wykonywanie zadań (wstępne instrukcje narzędzi)

Wielostopniowe procesy korzystają z wcześniejszego planowania i narracji postępów. Model opisuje, co zrobi, narracyjnie przechodzi przez każdy krok, a następnie podsumowuje wyniki.

### Samooceniający się kod

Wypróbuj "Utwórz serwis walidacji emaili". Zamiast tylko generować kod i przerywać, model generuje, ocenia względem kryteriów jakości, identyfikuje słabości i poprawia. Zobaczysz, jak iteruje, aż kod spełni standardy produkcyjne.

### Analiza strukturalna

Przeglądy kodu wymagają spójnych ram oceny. Model analizuje kod według ustalonych kategorii (poprawność, praktyki, wydajność, bezpieczeństwo) z poziomami istotności.

### Wiele kroków konwersacji

Zadaj pytanie "Co to jest Spring Boot?" a następnie od razu "Pokaż mi przykład". Model pamięta Twoje pierwsze pytanie i podaje konkretny przykład z Spring Boot. Bez pamięci drugie pytanie byłoby zbyt ogólne.

### Rozumowanie krok po kroku

Wybierz zadanie matematyczne i spróbuj rozwiązać je zarówno z Rozumowaniem krok po kroku, jak i z Low Eagerness. Low eagerness daje tylko odpowiedź – szybko, ale bez przejrzystości. Rozumowanie krok po kroku pokazuje każdy rachunek i decyzję.

### Ograniczona odpowiedź

Gdy potrzebujesz określonych formatów lub liczby słów, ten wzorzec wymusza ścisłe przestrzeganie zasad. Spróbuj wygenerować podsumowanie dokładnie w 100 słowach w formacie wypunktowanym.

## Czego naprawdę się uczysz

**Wysiłek rozumowy zmienia wszystko**

GPT-5.2 pozwala kontrolować nakład obliczeniowy przez podpowiedzi. Niski nakład oznacza szybkie odpowiedzi z minimalną eksploracją. Wysoki nakład oznacza, że model poświęca czas na głębokie myślenie. Uczysz się dopasowywać wysiłek do złożoności zadania – nie marnuj czasu na proste pytania, ale nie spiesz się z trudnymi decyzjami.

**Struktura kieruje zachowaniem**

Zauważ tagi XML w podpowiedziach? Nie są ozdobne. Modele bardziej niezawodnie podążają za instrukcjami o strukturze niż za tekstem wolnym. Gdy potrzebujesz wieloetapowych procesów lub złożonej logiki, struktura pomaga modelowi śledzić, gdzie jest i co dalej.

<img src="../../../translated_images/pl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomia dobrze ustrukturyzowanej podpowiedzi z wyraźnymi sekcjami i organizacją w stylu XML*

**Jakość przez samoocenę**

Wzorce samooceniające działają, czyniąc kryteria jakości jawne. Zamiast liczyć, że model "zrobi to dobrze", mówisz mu dokładnie, co oznacza "dobrze": poprawna logika, obsługa błędów, wydajność, bezpieczeństwo. Model może wtedy ocenić własny output i ulepszyć go. To zmienia generowanie kodu z loterii w proces.

**Kontekst jest ograniczony**

Wielokrotne rozmowy opierają się na dołączaniu historii wiadomości do każdego zapytania. Ale jest limit – każdy model ma maksymalną liczbę tokenów. W miarę rosnącej rozmowy musisz stosować strategie, aby utrzymać istotny kontekst bez osiągnięcia limitu. Ten moduł pokazuje, jak działa pamięć; później nauczysz się, kiedy podsumowywać, kiedy zapominać, a kiedy przywoływać.

## Kolejne kroki

**Następny moduł:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Nawigacja:** [← Poprzedni: Moduł 01 - Wprowadzenie](../01-introduction/README.md) | [Powrót do głównej](../README.md) | [Następny: Moduł 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:  
Ten dokument został przetłumaczony przy użyciu usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mimo że dążymy do dokładności, prosimy pamiętać, że tłumaczenia automatyczne mogą zawierać błędy lub nieścisłości. Oryginalny dokument w języku źródłowym należy uznać za źródło autorytatywne. W przypadku informacji o kluczowym znaczeniu zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->