# Moduł 02: Inżynieria Podpowiedzi z GPT-5.2

## Spis Treści

- [Czego się nauczysz](../../../02-prompt-engineering)
- [Wymagania wstępne](../../../02-prompt-engineering)
- [Zrozumienie Inżynierii Podpowiedzi](../../../02-prompt-engineering)
- [Podstawy Inżynierii Podpowiedzi](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Szablony Podpowiedzi](../../../02-prompt-engineering)
- [Wzorce Zaawansowane](../../../02-prompt-engineering)
- [Korzystanie z Istniejących Zasobów Azure](../../../02-prompt-engineering)
- [Zrzuty Ekranu Aplikacji](../../../02-prompt-engineering)
- [Eksploracja Wzorców](../../../02-prompt-engineering)
  - [Niskie vs Wysokie Zaangażowanie](../../../02-prompt-engineering)
  - [Wykonywanie Zadań (Wstępy Narzędzi)](../../../02-prompt-engineering)
  - [Kod z Samo-Refleksją](../../../02-prompt-engineering)
  - [Strukturalna Analiza](../../../02-prompt-engineering)
  - [Wieloturnowa Rozmowa](../../../02-prompt-engineering)
  - [Rozumowanie Krok po Kroku](../../../02-prompt-engineering)
  - [Ograniczona Odpowiedź](../../../02-prompt-engineering)
- [Co Naprawdę Się Uczysz](../../../02-prompt-engineering)
- [Kolejne Kroki](../../../02-prompt-engineering)

## Czego się nauczysz

<img src="../../../translated_images/pl/what-youll-learn.c68269ac048503b2.webp" alt="Czego się nauczysz" width="800"/>

W poprzednim module zobaczyłeś, jak pamięć umożliwia konwersacyjną AI i korzystałeś z modeli GitHub do podstawowych interakcji. Teraz skupimy się na tym, jak zadawać pytania — czyli na samych podpowiedziach — używając GPT-5.2 w Azure OpenAI. Sposób, w jaki strukturyzujesz podpowiedzi, dramatycznie wpływa na jakość otrzymywanych odpowiedzi. Zaczniemy od przeglądu podstawowych technik promptowania, a następnie przejdziemy do ośmiu zaawansowanych wzorców, które w pełni wykorzystują możliwości GPT-5.2.

Użyjemy GPT-5.2, ponieważ wprowadza on kontrolę rozumowania — możesz powiedzieć modelowi, ile ma myśleć przed udzieleniem odpowiedzi. To sprawia, że różne strategie podpowiedzi stają się bardziej widoczne i pomaga zrozumieć, kiedy stosować którą metodę. Skorzystamy także z mniejszych ograniczeń tempa w Azure dla GPT-5.2 w porównaniu do modeli GitHub.

## Wymagania wstępne

- Zakończony Moduł 01 (zasoby Azure OpenAI wdrożone)
- Plik `.env` w katalogu głównym z danymi uwierzytelniającymi Azure (utworzony przez `azd up` w Module 01)

> **Uwaga:** Jeśli nie ukończyłeś Modułu 01, najpierw postępuj zgodnie z instrukcjami wdrożenia tam zawartymi.

## Zrozumienie Inżynierii Podpowiedzi

<img src="../../../translated_images/pl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Czym jest Inżynieria Podpowiedzi?" width="800"/>

Inżynieria podpowiedzi to projektowanie tekstu wejściowego, który konsekwentnie daje ci potrzebne rezultaty. To nie tylko zadawanie pytań — to strukturyzowanie zapytań tak, by model dokładnie rozumiał, czego chcesz i jak to dostarczyć.

Pomyśl o tym jak o dawaniu instrukcji współpracownikowi. „Napraw błąd” jest nieprecyzyjne. „Napraw wyjątek null pointer w UserService.java w linii 45, dodając sprawdzenie null” jest konkretne. Modele językowe działają tak samo — liczy się precyzja i struktura.

<img src="../../../translated_images/pl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Jak LangChain4j się wpisuje" width="800"/>

LangChain4j dostarcza infrastrukturę — połączenia modeli, pamięć i typy wiadomości — podczas gdy wzorce podpowiedzi to po prostu starannie zorganizowany tekst przesyłany przez tę infrastrukturę. Kluczowymi elementami są `SystemMessage` (które ustawia zachowanie i rolę AI) oraz `UserMessage` (które niesie twoje właściwe żądanie).

## Podstawy Inżynierii Podpowiedzi

<img src="../../../translated_images/pl/five-patterns-overview.160f35045ffd2a94.webp" alt="Przegląd pięciu wzorców inżynierii podpowiedzi" width="800"/>

Zanim przejdziemy do zaawansowanych wzorców w tym module, przypomnijmy sobie pięć podstawowych technik promptowania. To budulce, które każdy inżynier podpowiedzi powinien znać. Jeśli już przeszedłeś przez [moduł szybkiego startu](../00-quick-start/README.md#2-prompt-patterns), widziałeś je w akcji — oto koncepcyjna rama stojąca za nimi.

### Zero-Shot Prompting

Najprostsze podejście: daj modelowi bezpośrednią instrukcję bez przykładów. Model polega wyłącznie na swoim treningu, aby zrozumieć i wykonać zadanie. Działa to dobrze w przypadku prostych próśb, gdzie oczekiwane zachowanie jest oczywiste.

<img src="../../../translated_images/pl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Bezpośrednia instrukcja bez przykładów — model wywnioskowuje zadanie tylko z instrukcji*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odpowiedź: "Pozytywna"
```

**Kiedy używać:** Proste klasyfikacje, bezpośrednie pytania, tłumaczenia lub każde zadanie, które model może obsłużyć bez dodatkowego wsparcia.

### Few-Shot Prompting

Podaj przykłady, które demonstrują wzór, jaki chcesz, by model naśladował. Model uczy się oczekiwanego formatu wejścia-wyjścia z twoich przykładów i stosuje go do nowych danych. Znacząco poprawia to spójność w zadaniach, gdzie pożądany format lub zachowanie nie jest oczywiste.

<img src="../../../translated_images/pl/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

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

**Kiedy używać:** Niestandardowe klasyfikacje, spójne formatowanie, zadania specyficzne dla danej dziedziny lub gdy wyniki zero-shot są niespójne.

### Chain of Thought

Poproś model, by pokazał krok po kroku swoje rozumowanie. Zamiast skakać prosto do odpowiedzi, model rozbija problem na części i wyjaśnia każdy krok jawnie. Poprawia to dokładność w zadaniach matematycznych, logicznych i wieloetapowego rozumowania.

<img src="../../../translated_images/pl/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Rozumowanie krok po kroku — rozbijanie złożonych problemów na wyraźne logiczne etapy*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model pokazuje: 15 - 8 = 7, potem 7 + 12 = 19 jabłek
```

**Kiedy używać:** Zadania matematyczne, łamigłówki logiczne, debugowanie lub każde zadanie, gdzie pokazanie procesu rozumowania poprawia dokładność i zaufanie.

### Role-Based Prompting

Ustaw personę lub rolę dla AI przed zadaniem pytania. To dostarcza kontekstu, który kształtuje ton, głębokość i fokus odpowiedzi. „Architekt oprogramowania” udziela innych rad niż „młodszy programista” czy „audytor bezpieczeństwa”.

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

**Kiedy używać:** Przeglądy kodu, korepetycje, analiza specyficzna dla domeny lub gdy potrzebujesz odpowiedzi dostosowanych do określonego poziomu doświadczenia lub perspektywy.

### Szablony Podpowiedzi

Twórz wielokrotnego użytku podpowiedzi z zmiennymi zastępczymi. Zamiast pisać nową podpowiedź za każdym razem, zdefiniuj szablon raz i wypełniaj różnymi wartościami. Klasa `PromptTemplate` LangChain4j ułatwia to dzięki składni `{{variable}}`.

<img src="../../../translated_images/pl/prompt-templates.14bfc37d45f1a933.webp" alt="Szablony Podpowiedzi" width="800"/>

*Wielokrotnego użytku podpowiedzi ze zmiennymi — jeden szablon, wiele zastosowań*

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

**Kiedy używać:** Powtarzające się zapytania z różnymi danymi, przetwarzanie wsadowe, budowa wielokrotnego użytku workflow AI lub każda sytuacja, gdy struktura podpowiedzi pozostaje ta sama, ale dane się zmieniają.

---

Te pięć podstaw daje solidny zestaw narzędzi do większości zadań promptowania. Reszta modułu rozbudowuje je o **osiem wzorców zaawansowanych**, które wykorzystują kontrolę rozumowania, samoocenę i strukturalne wyjście GPT-5.2.

## Wzorce Zaawansowane

Po omówieniu podstaw przejdźmy do ośmiu zaawansowanych wzorców, które wyróżniają ten moduł. Nie wszystkie problemy wymagają tego samego podejścia. Niektóre pytania potrzebują szybkich odpowiedzi, inne głębokiego myślenia. Niektóre wymagają widocznego rozumowania, inne tylko wyników. Każdy poniższy wzorzec jest zoptymalizowany pod inny scenariusz — a kontrola rozumowania GPT-5.2 wyraźnie uwypukla różnice.

<img src="../../../translated_images/pl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osiem wzorców promptowania" width="800"/>

*Przegląd ośmiu wzorców inżynierii podpowiedzi i ich zastosowań*

<img src="../../../translated_images/pl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrola rozumowania w GPT-5.2" width="800"/>

*Kontrola rozumowania GPT-5.2 pozwala określić, ile model powinien myśleć — od szybkich, bezpośrednich odpowiedzi po głęboką eksplorację*

<img src="../../../translated_images/pl/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Porównanie nakładu rozumowania" width="800"/>

*Niskie zaangażowanie (szybkie, bezpośrednie) vs wysokie zaangażowanie (doktorskie, eksploracyjne)*

**Niskie Zaangażowanie (Szybkie i Skoncentrowane)** — Do prostych pytań, gdzie chcesz szybkich, bezpośrednich odpowiedzi. Model robi minimalne rozumowanie — maksymalnie 2 kroki. Użyj do obliczeń, wyszukiwań lub prostych pytań.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Eksploruj z GitHub Copilot:** Otwórz [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) i zapytaj:
> - "Jaka jest różnica między niskim a wysokim zaangażowaniem we wzorcach promptowania?"
> - "Jak znaczniki XML w podpowiedziach pomagają strukturyzować odpowiedź AI?"
> - "Kiedy powinienem używać wzorców z samo-refleksją a kiedy bezpośrednich instrukcji?"

**Wysokie Zaangażowanie (Głębokie i Dokładne)** — Do złożonych problemów, gdzie chcesz kompleksowej analizy. Model eksploruje dokładnie i pokazuje szczegółowe rozumowanie. Użyj do projektowania systemów, decyzji architektonicznych lub złożonych badań.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Wykonywanie Zadania (Postęp Krok po Kroku)** — Do workflow wieloetapowych. Model przedstawia plan z góry, opisuje każdy krok w trakcie pracy, a na końcu daje podsumowanie. Użyj do migracji, implementacji lub dowolnego procesu wieloetapowego.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought promptowanie wyraźnie prosi model o pokazanie procesu rozumowania, poprawiając trafność w złożonych zadaniach. Rozbicie krok po kroku pomaga zarówno ludziom, jak i AI zrozumieć logikę.

> **🤖 Wypróbuj z czatem [GitHub Copilot](https://github.com/features/copilot):** Zapytaj o ten wzorzec:
> - "Jak dostosowałbym wzorzec wykonania zadania dla operacji długo trwających?"
> - "Jakie są najlepsze praktyki dla strukturyzowania wstępów narzędzi w aplikacjach produkcyjnych?"
> - "Jak mogę przechwycić i wyświetlać aktualizacje postępu pośredniego w UI?"

<img src="../../../translated_images/pl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Wzorzec wykonania zadania" width="800"/>

*Workflow: planuj → wykonuj → podsumuj dla zadań wieloetapowych*

**Kod z Samo-Refleksją** — Do generowania kodu produkcyjnej jakości. Model generuje kod, sprawdza go względem kryteriów jakości i iteracyjnie ulepsza. Użyj przy tworzeniu nowych funkcji lub usług.

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

<img src="../../../translated_images/pl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cykl samo-refleksji" width="800"/>

*Iteracyjna pętla ulepszania: generuj, oceniaj, identyfikuj błędy, poprawiaj, powtarzaj*

**Strukturalna Analiza** — Do spójnej oceny. Model przegląda kod używając ustalonego schematu (poprawność, praktyki, wydajność, bezpieczeństwo). Użyj do przeglądów kodu lub oceny jakości.

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
> - "Jak mogę dostosować ramy analizy do różnych typów przeglądów kodu?"
> - "Jaki jest najlepszy sposób parsowania i programowego reagowania na wyjście strukturalne?"
> - "Jak zapewnić spójne poziomy ważności w różnych sesjach przeglądów?"

<img src="../../../translated_images/pl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Wzorzec analizy strukturalnej" width="800"/>

*Czterokategoriowy schemat do spójnych przeglądów kodu z poziomami ważności*

**Wieloturnowa Rozmowa** — Do rozmów wymagających kontekstu. Model pamięta poprzednie wiadomości i buduje na nich. Użyj do interaktywnych sesji pomocy lub złożonych pytań i odpowiedzi.

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

*Jak kontekst rozmowy kumuluje się przez wiele tur aż do osiągnięcia limitu tokenów*

**Rozumowanie Krok po Kroku** — Do problemów wymagających widocznej logiki. Model pokazuje jawne rozumowanie dla każdego kroku. Użyj do zadań matematycznych, łamigłówek logicznych lub gdy chcesz zrozumieć proces myślenia.

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

*Rozbijanie problemów na wyraźne logiczne kroki*

**Ograniczona Odpowiedź** — Do odpowiedzi z określonymi wymaganiami formatowania. Model ściśle przestrzega zasad formatu i długości. Użyj do streszczeń lub gdy potrzebujesz precyzyjnej struktury wyjścia.

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

<img src="../../../translated_images/pl/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Wzorzec ograniczonej odpowiedzi" width="800"/>

*Wymuszanie określonego formatu, długości i struktury*

## Korzystanie z Istniejących Zasobów Azure

**Weryfikacja wdrożenia:**

Upewnij się, że plik `.env` istnieje w katalogu głównym z danymi uwierzytelniającymi Azure (utworzony podczas Modułu 01):
```bash
cat ../.env  # Powinno pokazać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Uruchom aplikację:**

> **Uwaga:** Jeśli już uruchomiłeś wszystkie aplikacje używając `./start-all.sh` z Modułu 01, ten moduł już działa na porcie 8083. Możesz pominąć poniższe polecenia startowe i przejść bezpośrednio do http://localhost:8083.

**Opcja 1: Korzystanie z Spring Boot Dashboard (zalecane dla użytkowników VS Code)**

Kontener deweloperski zawiera rozszerzenie Spring Boot Dashboard, które zapewnia wizualny interfejs do zarządzania wszystkimi aplikacjami Spring Boot. Znajdziesz je na pasku aktywności po lewej stronie VS Code (szukaj ikony Spring Boot).
Z poziomu Spring Boot Dashboard możesz:
- Zobaczyć wszystkie dostępne aplikacje Spring Boot w przestrzeni roboczej
- Uruchamiać/zatrzymywać aplikacje za pomocą jednego kliknięcia
- Wyświetlać logi aplikacji w czasie rzeczywistym
- Monitorować stan aplikacji

Po prostu kliknij przycisk play obok "prompt-engineering", aby uruchomić ten moduł, lub uruchom wszystkie moduły naraz.

<img src="../../../translated_images/pl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opcja 2: Użycie skryptów powłoki**

Uruchom wszystkie aplikacje internetowe (moduły 01-04):

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

> **Uwaga:** Jeśli wolisz najpierw ręcznie zbudować wszystkie moduły przed uruchomieniem:
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

## Eksploracja wzorców

Interfejs webowy pozwala eksperymentować z różnymi strategiami promptowania. Każdy wzorzec rozwiązuje inne problemy – wypróbuj je, aby zobaczyć, kiedy każde podejście się sprawdza.

### Niska vs wysoka ochota

Zadaj proste pytanie, np. „Jaka jest 15% z 200?” używając Niskiej ochoty. Otrzymasz natychmiastową, bezpośrednią odpowiedź. Teraz zapytaj o coś skomplikowanego, np. „Zaprojektuj strategię cache’owania dla API o dużym natężeniu ruchu”, używając Wysokiej ochoty. Zobacz, jak model zwalnia i dostarcza szczegółowe uzasadnienie. Ten sam model, ta sama struktura pytania – ale prompt mówi mu, ile ma myśleć.

<img src="../../../translated_images/pl/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Szybkie obliczenie z minimalnym uzasadnieniem*

<img src="../../../translated_images/pl/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Kompleksowa strategia cache’owania (2,8MB)*

### Wykonywanie zadań (Wprowadzenia do narzędzi)

Wieloetapowe przepływy pracy korzystają na wcześniejszym planowaniu i narracji postępu. Model opisuje, co zrobi, narracyjnie przeprowadza przez każdy krok, a potem podsumowuje wyniki.

<img src="../../../translated_images/pl/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Tworzenie endpointu REST ze szczegółową narracją krok po kroku (3,9MB)*

### Samooceniający się kod

Wypróbuj „Utwórz serwis walidacji emaili”. Zamiast tylko generować kod i na tym poprzestać, model generuje, ocenia go według kryteriów jakości, identyfikuje słabości i ulepsza kod. Zobaczysz, jak iteruje, aż kod spełni standardy produkcyjne.

<img src="../../../translated_images/pl/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Kompletny serwis walidacji emaili (5,2MB)*

### Strukturalna analiza

Przeglądy kodu wymagają spójnych ram oceny. Model analizuje kod według ustalonych kategorii (poprawność, praktyki, wydajność, bezpieczeństwo) z poziomami nasilenia.

<img src="../../../translated_images/pl/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Przegląd kodu oparty na ramach oceny*

### Wieloetapowy czat

Zapytaj „Co to jest Spring Boot?”, a następnie od razu dodaj „Pokaż mi przykład”. Model pamięta twoje pierwsze pytanie i podaje ci przykład Spring Boot konkretnie. Bez pamięci drugie pytanie byłoby zbyt ogólne.

<img src="../../../translated_images/pl/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Zachowanie kontekstu między pytaniami*

### Rozumowanie krok po kroku

Wybierz problem matematyczny i spróbuj podejścia rozumowania krok po kroku oraz niskiej ochoty. Niska ochota daje tylko odpowiedź – szybko, ale bez przejrzystości. Krok po kroku pokazuje każde obliczenie i decyzję.

<img src="../../../translated_images/pl/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Problem matematyczny z explicite przedstawionymi krokami*

### Ograniczona forma wyjścia

Gdy potrzebujesz konkretnych formatów lub liczby słów, ten wzorzec wymusza ścisłe przestrzeganie wymogów. Spróbuj wygenerować podsumowanie z dokładnie 100 słowami w formacie listy punktowanej.

<img src="../../../translated_images/pl/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Podsumowanie uczenia maszynowego z kontrolą formatu*

## Czego naprawdę się uczysz

**Wysiłek rozumowania zmienia wszystko**

GPT-5.2 pozwala kontrolować wysiłek obliczeniowy za pomocą promptów. Niski wysiłek oznacza szybkie odpowiedzi z minimalnym przeszukiwaniem. Wysoki wysiłek oznacza, że model poświęca czas na dogłębne myślenie. Uczysz się dopasowywać wysiłek do złożoności zadania – nie marnuj czasu na proste pytania, lecz też nie śpiesz się z decyzjami skomplikowanymi.

**Struktura kieruje zachowaniem**

Zauważyłeś tagi XML w promptach? Nie są tylko dekoracją. Modele lepiej realizują złożone instrukcje, gdy są one odpowiednio zorganizowane. Gdy potrzebujesz wieloetapowych procesów lub złożonej logiki, struktura pomaga modelowi śledzić, gdzie jest i co nastąpi dalej.

<img src="../../../translated_images/pl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomia dobrze zorganizowanego promptu z wyraźnymi sekcjami i organizacją w stylu XML*

**Jakość poprzez samoocenę**

Wzorce samooceniające działają na zasadzie wyraźnego określenia kryteriów jakości. Zamiast mieć nadzieję, że model „zrobi to dobrze”, mówisz dokładnie, co oznacza „dobrze”: poprawna logika, obsługa błędów, wydajność, bezpieczeństwo. Model może wtedy sam ocenić swój output i go ulepszyć. To zmienia generowanie kodu z loterii w proces.

**Kontekst jest ograniczony**

Wieloetapowe rozmowy odbywają się przez dołączanie historii wiadomości do każdego zapytania. Ale jest limit – każdy model ma maksymalną liczbę tokenów. W miarę rozwoju rozmowy potrzebne są strategie zachowania istotnego kontekstu bez przekraczania limitu. Ten moduł pokazuje, jak działa pamięć; później nauczysz się, kiedy streszczać, kiedy zapominać, a kiedy przywoływać.

## Kolejne kroki

**Następny moduł:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Nawigacja:** [← Poprzedni: Moduł 01 - Wprowadzenie](../01-introduction/README.md) | [Powrót do głównego](../README.md) | [Następny: Moduł 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:
Niniejszy dokument został przetłumaczony za pomocą automatycznej usługi tłumaczeniowej AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mimo że dążymy do dokładności, prosimy mieć na uwadze, że tłumaczenia automatyczne mogą zawierać błędy lub nieścisłości. Oryginalny dokument w języku źródłowym należy traktować jako ostateczne źródło informacji. W przypadku istotnych informacji rekomendowane jest skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->