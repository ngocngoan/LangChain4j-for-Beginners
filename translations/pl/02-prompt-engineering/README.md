# Moduł 02: Projektowanie promptów z GPT-5.2

## Spis treści

- [Czego się nauczysz](../../../02-prompt-engineering)
- [Wymagania wstępne](../../../02-prompt-engineering)
- [Zrozumienie projektowania promptów](../../../02-prompt-engineering)
- [Podstawy projektowania promptów](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Szablony promptów](../../../02-prompt-engineering)
- [Wzorce zaawansowane](../../../02-prompt-engineering)
- [Wykorzystanie istniejących zasobów Azure](../../../02-prompt-engineering)
- [Zrzuty ekranu aplikacji](../../../02-prompt-engineering)
- [Eksploracja wzorców](../../../02-prompt-engineering)
  - [Niska vs wysoka chęć działania](../../../02-prompt-engineering)
  - [Wykonywanie zadań (wstępy narzędzi)](../../../02-prompt-engineering)
  - [Kod ze zdolnością autorefleksji](../../../02-prompt-engineering)
  - [Analiza strukturalna](../../../02-prompt-engineering)
  - [Wielokrotna rozmowa](../../../02-prompt-engineering)
  - [Rozumowanie krok po kroku](../../../02-prompt-engineering)
  - [Ograniczone wyjście](../../../02-prompt-engineering)
- [Czego naprawdę się uczysz](../../../02-prompt-engineering)
- [Kolejne kroki](../../../02-prompt-engineering)

## Czego się nauczysz

<img src="../../../translated_images/pl/what-youll-learn.c68269ac048503b2.webp" alt="Czego się nauczysz" width="800"/>

W poprzednim module zobaczyłeś, jak pamięć umożliwia konwersacyjną SI oraz korzystałeś z modeli GitHub do podstawowych interakcji. Teraz skupimy się na tym, jak zadawać pytania — czyli na samych promptach — używając GPT-5.2 w Azure OpenAI. Sposób, w jaki strukturyzujesz prompty, ma ogromny wpływ na jakość otrzymywanych odpowiedzi. Zacznijmy od przeglądu podstawowych technik promptowania, a następnie przejdziemy do ośmiu zaawansowanych wzorców, które w pełni wykorzystują możliwości GPT-5.2.

Użyjemy GPT-5.2, ponieważ wprowadza kontrolę rozumowania – możesz powiedzieć modelowi, ile ma przemyśleć odpowiedź przed jej wygenerowaniem. To sprawia, że różne strategie promptowania stają się bardziej widoczne i pomaga zrozumieć, kiedy stosować każdy z nich. Skorzystamy również z mniejszej liczby limitów rate limit w Azure dla GPT-5.2 w porównaniu do modeli GitHub.

## Wymagania wstępne

- Ukończony Moduł 01 (zasoby Azure OpenAI wdrożone)
- Plik `.env` w katalogu głównym z poświadczeniami Azure (utworzony przez `azd up` w Moduł 01)

> **Uwaga:** Jeśli nie ukończyłeś Modułu 01, najpierw wykonaj instrukcje wdrożenia tam zawarte.

## Zrozumienie projektowania promptów

<img src="../../../translated_images/pl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Czym jest projektowanie promptów?" width="800"/>

Projektowanie promptów polega na tworzeniu tekstu wejściowego, który konsekwentnie zapewnia oczekiwane wyniki. To nie tylko zadawanie pytań – chodzi o strukturyzowanie żądań tak, aby model dokładnie rozumiał, czego chcesz i jak to dostarczyć.

Pomyśl o tym jak o dawaniu instrukcji koledze z pracy. „Napraw błąd” jest niejasne. „Napraw wyjątek null pointer w UserService.java linia 45, dodając sprawdzenie null” jest konkretne. Modele językowe działają tak samo – liczy się precyzja i struktura.

<img src="../../../translated_images/pl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Jak pasuje LangChain4j" width="800"/>

LangChain4j zapewnia infrastrukturę — połączenia z modelami, pamięć i typy wiadomości — podczas gdy wzorce promptowania to po prostu starannie zbudowany tekst przesyłany przez tę infrastrukturę. Kluczowymi elementami są `SystemMessage` (która ustawia zachowanie i rolę SI) oraz `UserMessage` (niesie twoją faktyczną prośbę).

## Podstawy projektowania promptów

<img src="../../../translated_images/pl/five-patterns-overview.160f35045ffd2a94.webp" alt="Przegląd pięciu wzorców projektowania promptów" width="800"/>

Zanim przejdziemy do zaawansowanych wzorców w tym module, przejrzyjmy pięć podstawowych technik promptowania. Są to fundamenty, które każdy inżynier promptów powinien znać. Jeśli już przeszedłeś przez [moduł szybkiego startu](../00-quick-start/README.md#2-prompt-patterns), widziałeś je w praktyce — oto konceptualne ramy stojące za nimi.

### Zero-Shot Prompting

Najprostsze podejście: podaj modelowi bezpośrednią instrukcję bez przykładów. Model polega wyłącznie na swoim wytrenowaniu, aby zrozumieć i wykonać zadanie. Działa dobrze przy prostych prośbach, gdzie oczekiwane zachowanie jest oczywiste.

<img src="../../../translated_images/pl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Bezpośrednia instrukcja bez przykładów — model wywodzi zadanie wyłącznie z instrukcji*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odpowiedź: "Pozytywna"
```

**Kiedy używać:** Proste klasyfikacje, bezpośrednie pytania, tłumaczenia lub dowolne zadania, które model może wykonać bez dodatkowych wskazówek.

### Few-Shot Prompting

Podaj przykłady, które pokazują wzorzec, jaki model ma stosować. Model uczy się oczekiwanego formatu wejścia-wyjścia z twoich przykładów i stosuje go do nowych danych. Znacznie poprawia to spójność tam, gdzie oczekiwany format lub zachowanie nie są oczywiste.

<img src="../../../translated_images/pl/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Nauka na przykładach — model rozpoznaje wzorzec i stosuje go do nowych danych*

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

**Kiedy używać:** Niestandardowe klasyfikacje, spójne formatowanie, zadania specjalistyczne lub gdy wyniki zero-shot są niespójne.

### Chain of Thought

Poproś model, by pokazał swoje rozumowanie krok po kroku. Zamiast od razu przechodzić do odpowiedzi, model rozbija problem i przetwarza każdą część oddzielnie. Poprawia to dokładność przy zadaniach matematycznych, logicznych i wieloetapowych.

<img src="../../../translated_images/pl/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Rozumowanie krok po kroku — rozbijanie złożonych problemów na jawne kroki logiczne*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model pokazuje: 15 - 8 = 7, następnie 7 + 12 = 19 jabłek
```

**Kiedy używać:** Zadania matematyczne, łamigłówki logiczne, debugowanie lub każde zadanie, gdzie pokazanie procesu rozumowania zwiększa dokładność i zaufanie.

### Role-Based Prompting

Ustaw personę lub rolę AI zanim zadasz pytanie. Dostarcza to kontekstu, który kształtuje ton, głębokość i fokus odpowiedzi. „Architekt oprogramowania” daje inne wskazówki niż „młodszy programista” czy „audytor bezpieczeństwa”.

<img src="../../../translated_images/pl/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Ustawianie kontekstu i persony — to samo pytanie otrzymuje różną odpowiedź w zależności od przypisanej roli*

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

**Kiedy używać:** Przeglądy kodu, korepetycje, analiza specjalistyczna lub gdy potrzebujesz odpowiedzi dopasowanych do konkretnego poziomu wiedzy czy perspektywy.

### Szablony promptów

Twórz wielokrotnego użytku prompty z zmiennymi zastępczymi. Zamiast pisać nowy prompt za każdym razem, zdefiniuj raz szablon i wypełniaj różne wartości. Klasa `PromptTemplate` w LangChain4j ułatwia to, używając składni `{{zmienna}}`.

<img src="../../../translated_images/pl/prompt-templates.14bfc37d45f1a933.webp" alt="Szablony promptów" width="800"/>

*Wielokrotnego użytku prompty ze zmiennymi zastępczymi — jeden szablon, wiele zastosowań*

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

**Kiedy używać:** Powtarzające się zapytania z różnymi danymi, przetwarzanie wsadowe, tworzenie powtarzalnych przepływów pracy SI lub każde użycie, gdzie struktura promptu pozostaje stała, a dane się zmieniają.

---

Te pięć podstaw daje solidny zestaw narzędzi dla większości zadań promptowania. Reszta tego modułu rozwija je o **osiem wzorców zaawansowanych**, które wykorzystują kontrolę rozumowania GPT-5.2, samoocenę i możliwości strukturalnego wyjścia.

## Wzorce zaawansowane

Po omówieniu podstaw przejdźmy do ośmiu wzorców zaawansowanych, które wyróżniają ten moduł. Nie każde zadanie wymaga tego samego podejścia. Niektóre pytania potrzebują szybkich odpowiedzi, inne głębokiego przemyślenia. Jedne potrzebują widocznego rozumowania, inne tylko wyników. Każdy z poniższych wzorców jest zoptymalizowany pod inny scenariusz — a kontrola rozumowania GPT-5.2 podkreśla te różnice.

<img src="../../../translated_images/pl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osiem wzorców promptowania" width="800"/>

*Przegląd ośmiu wzorców projektowania promptów i ich zastosowań*

<img src="../../../translated_images/pl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrola rozumowania z GPT-5.2" width="800"/>

*Kontrola rozumowania GPT-5.2 pozwala ustalić, ile model ma myśleć — od szybkich, bezpośrednich odpowiedzi po głęboką eksplorację*

**Niska chęć (Szybko i Skoncentrowanie)** – Dla prostych pytań, gdzie chcesz szybkich i bezpośrednich odpowiedzi. Model wykonuje minimalne rozumowanie – maksymalnie 2 kroki. Używaj do obliczeń, wyszukiwań lub prostych pytań.

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
> - „Jaka jest różnica między wzorcami niskiej a wysokiej chęci?”
> - „Jak tagi XML w promptach pomagają strukturyzować odpowiedź SI?”
> - „Kiedy korzystać ze wzorców autorefleksji, a kiedy z bezpośredniej instrukcji?”

**Wysoka chęć (Głęboko i Dokładnie)** – Dla złożonych problemów, gdzie chcesz wszechstronnej analizy. Model eksploruje dokładnie i pokazuje szczegółowe rozumowanie. Używaj do projektowania systemów, decyzji architektonicznych lub złożonych badań.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Wykonywanie zadań (postęp krok po kroku)** – Dla wieloetapowych procesów. Model podaje plan na początku, narrację kroków w trakcie pracy, a potem streszczenie. Używaj do migracji, implementacji lub procesów wieloetapowych.

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

Promptowanie Chain-of-Thought wyraźnie prosi model o pokazanie procesu rozumowania, co poprawia dokładność przy złożonych zadaniach. Podział krok po kroku pomaga zarówno ludziom, jak i SI zrozumieć logikę.

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Zapytaj o ten wzorzec:
> - „Jak dostosować wzorzec wykonywania zadań do długotrwałych operacji?”
> - „Jakie są najlepsze praktyki w konstruowaniu wstępów narzędzi w aplikacjach produkcyjnych?”
> - „Jak przechwytywać i wyświetlać pośrednie aktualizacje postępu w interfejsie użytkownika?”

<img src="../../../translated_images/pl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Wzorzec wykonywania zadań" width="800"/>

*Planowanie → Wykonywanie → Podsumowanie procesu dla zadań wieloetapowych*

**Kod ze zdolnością autorefleksji** – Do generowania kodu produkcyjnej jakości. Model generuje kod zgodny ze standardami produkcyjnymi z prawidłowym obsługiwaniem błędów. Używaj do tworzenia nowych funkcji lub usług.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cykl autorefleksji" width="800"/>

*Iteracyjna pętla ulepszania – generuj, oceniaj, identyfikuj problemy, poprawiaj, powtarzaj*

**Analiza strukturalna** – Do spójnej oceny. Model przegląda kod używając ustalonego schematu (poprawność, praktyki, wydajność, bezpieczeństwo, konserwowalność). Używaj do przeglądów kodu lub oceny jakości.

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
> - „Jak dostosować ramy analityczne do różnych typów przeglądów kodu?”
> - „Jaki jest najlepszy sposób programistycznego parsowania i działania na wyjściu strukturalnym?”
> - „Jak zapewnić spójne poziomy ważności między różnymi sesjami przeglądu?”

<img src="../../../translated_images/pl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Wzorzec analizy strukturalnej" width="800"/>

*Schemat do spójnych przeglądów kodu z poziomami ważności*

**Wielokrotna rozmowa** – Do konwersacji wymagających kontekstu. Model zapamiętuje poprzednie wiadomości i buduje na ich podstawie. Używaj do interaktywnych sesji pomocy lub złożonych Q&A.

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

**Rozumowanie krok po kroku** – Do problemów wymagających widocznej logiki. Model pokazuje jawne rozumowanie dla każdego kroku. Używaj do zadań matematycznych, łamigłówek logicznych lub gdy chcesz zrozumieć proces myślenia.

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

*Rozbijanie problemów na jawne kroki logiczne*

**Ograniczone wyjście** – Do odpowiedzi z wymaganiami co do konkretnego formatu. Model ściśle przestrzega reguł formatu i długości. Używaj do streszczeń lub gdy potrzebujesz precyzyjnej struktury wyjścia.

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

<img src="../../../translated_images/pl/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Wzorzec ograniczonego wyjścia" width="800"/>

*Wymuszanie określonego formatu, długości i struktury*

## Wykorzystanie istniejących zasobów Azure

**Weryfikacja wdrożenia:**

Upewnij się, że plik `.env` istnieje w katalogu głównym i zawiera poświadczenia Azure (utworzony podczas Modułu 01):
```bash
cat ../.env  # Powinno wyświetlać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Uruchom aplikację:**

> **Uwaga:** Jeśli już uruchomiłeś wszystkie aplikacje za pomocą `./start-all.sh` z Modułu 01, ten moduł działa już na porcie 8083. Możesz pominąć poniższe polecenia uruchomienia i przejść bezpośrednio do http://localhost:8083.

**Opcja 1: Użycie Spring Boot Dashboard (zalecane dla użytkowników VS Code)**

Kontener deweloperski zawiera rozszerzenie Spring Boot Dashboard, które zapewnia wizualny interfejs do zarządzania wszystkimi aplikacjami Spring Boot. Znajdziesz je na pasku aktywności po lewej stronie VS Code (szukaj ikony Spring Boot).

Z poziomu Spring Boot Dashboard możesz:
- Zobaczyć wszystkie dostępne aplikacje Spring Boot w obszarze roboczym
- Uruchamiać/zatrzymywać aplikacje jednym kliknięciem
- Przeglądać logi aplikacji w czasie rzeczywistym
- Monitorować status aplikacji
Kliknij przycisk odtwarzania obok „prompt-engineering”, aby rozpocząć ten moduł, lub uruchom wszystkie moduły naraz.

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

Oba skrypty automatycznie ładują zmienne środowiskowe z głównego pliku `.env` i zbudują pliki JAR, jeśli ich nie ma.

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

*Główny pulpit pokazujący wszystkie 8 wzorców prompt engineering z ich charakterystyką i przypadkami użycia*

## Eksploracja wzorców

Interfejs webowy pozwala eksperymentować z różnymi strategiami promptowania. Każdy wzorzec rozwiązuje różne problemy — wypróbuj je, aby zobaczyć, kiedy każda metoda działa najlepiej.

### Niskie vs wysokie zaangażowanie

Zadaj proste pytanie, takie jak „Jaka jest 15% z 200?” używając Niskiego zaangażowania. Otrzymasz natychmiastową, bezpośrednią odpowiedź. Teraz zapytaj coś złożonego, jak „Zaprojektuj strategię cache’owania dla API o dużym natężeniu ruchu” używając Wysokiego zaangażowania. Zobacz, jak model zwalnia i podaje szczegółowe uzasadnienie. Ten sam model, ta sama struktura pytania — ale prompt mówi mu, ile myślenia ma wykonać.

<img src="../../../translated_images/pl/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Szybkie obliczenie z minimalnym rozumowaniem*

<img src="../../../translated_images/pl/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Kompleksowa strategia cache’owania (2,8 MB)*

### Wykonanie zadania (wstępne komunikaty narzędzi)

Przepływy pracy wieloetapowej korzystają z planowania i narracji postępu. Model opisuje, co zrobi, relacjonuje każdy krok, a następnie podsumowuje wyniki.

<img src="../../../translated_images/pl/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Tworzenie punktu końcowego REST z narracją krok po kroku (3,9 MB)*

### Samorefleksyjny kod

Spróbuj „Stwórz usługę walidacji emaili”. Zamiast tylko generować kod i przerywać, model generuje, ocenia względem kryteriów jakości, identyfikuje słabości i poprawia. Zobaczysz, jak iteruje, aż kod spełni standardy produkcyjne.

<img src="../../../translated_images/pl/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Kompletna usługa walidacji emaila (5,2 MB)*

### Strukturalna analiza

Przeglądy kodu wymagają spójnych ram oceny. Model analizuje kod używając stałych kategorii (poprawność, dobre praktyki, wydajność, bezpieczeństwo) z poziomami nasilenia.

<img src="../../../translated_images/pl/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Przegląd kodu oparty na ramach*

### Czatu wieloetapowy

Zapytaj „Czym jest Spring Boot?”, a następnie od razu dodaj „Pokaż mi przykład”. Model pamięta pierwsze pytanie i podaje konkretny przykład Spring Boot. Bez pamięci drugie pytanie byłoby zbyt niejasne.

<img src="../../../translated_images/pl/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Zachowanie kontekstu między pytaniami*

### Rozumowanie krok po kroku

Wybierz zadanie matematyczne i spróbuj z użyciem zarówno Rozumowania krok po kroku, jak i Niskiego zaangażowania. Niskie zaangażowanie daje tylko odpowiedź — szybko, ale bez przejrzystości. Rozumowanie krok po kroku pokazuje każdy etap obliczeń i decyzji.

<img src="../../../translated_images/pl/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Zadanie matematyczne z wyraźnymi krokami*

### Ograniczona odpowiedź

Gdy potrzebujesz konkretnych formatów lub liczby słów, ten wzorzec wymusza ścisłe przestrzeganie wymogów. Spróbuj wygenerować podsumowanie dokładnie w 100 słowach w formacie punktowanym.

<img src="../../../translated_images/pl/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Podsumowanie machine learning z kontrolą formatu*

## Czego naprawdę się uczysz

**Wysiłek rozumowania zmienia wszystko**

GPT-5.2 pozwala kontrolować nakład obliczeniowy poprzez prompt. Niski wysiłek oznacza szybkie odpowiedzi z minimalną eksploracją. Wysoki wysiłek oznacza, że model poświęca czas na głębokie myślenie. Uczysz się dostosowywać wysiłek do złożoności zadania — nie marnuj czasu na proste pytania, ale też nie śpiesz się z trudnymi decyzjami.

**Struktura kieruje zachowaniem**

Zauważyłeś tagi XML w promptach? Nie są dekoracją. Modele lepiej przestrzegają strukturalnych instrukcji niż swobodnego tekstu. Gdy potrzebujesz procesów wieloetapowych lub złożonej logiki, struktura pomaga modelowi śledzić, gdzie jest i co nastąpi dalej.

<img src="../../../translated_images/pl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomia dobrze zbudowanego prompta z jasnymi sekcjami i organizacją w stylu XML*

**Jakość przez samoocenę**

Wzorce samorefleksyjne działają przez wyraźne określanie kryteriów jakości. Zamiast mieć nadzieję, że model „zrobi to poprawnie”, mówisz mu dokładnie, co znaczy „poprawnie”: prawidłowa logika, obsługa błędów, wydajność, bezpieczeństwo. Model może wtedy ocenić własny wynik i go poprawić. To zmienia generowanie kodu z loterii w proces.

**Kontekst jest ograniczony**

Rozmowy wieloetapowe działają przez dołączanie historii wiadomości do każdego zapytania. Ale jest limit — każdy model ma maksymalną liczbę tokenów. W miarę rozwoju rozmów potrzebujesz strategii, by zachować istotny kontekst bez przekraczania limitu. Ten moduł pokazuje, jak działa pamięć; później nauczysz się, kiedy podsumowywać, kiedy zapominać, a kiedy przywoływać.

## Kolejne kroki

**Następny moduł:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Nawigacja:** [← Poprzedni: Moduł 01 - Wprowadzenie](../01-introduction/README.md) | [Powrót do głównego](../README.md) | [Następny: Moduł 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:
Niniejszy dokument został przetłumaczony za pomocą usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Chociaż dążymy do dokładności, prosimy pamiętać, że automatyczne tłumaczenia mogą zawierać błędy lub nieścisłości. Oryginalny dokument w języku źródłowym należy uznać za źródło wiarygodne. W przypadku informacji krytycznych zaleca się skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->