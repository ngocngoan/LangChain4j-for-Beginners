# Moduł 02: Inżynieria promptów z GPT-5.2

## Spis treści

- [Film instruktażowy](../../../02-prompt-engineering)
- [Czego się nauczysz](../../../02-prompt-engineering)
- [Wymagania wstępne](../../../02-prompt-engineering)
- [Zrozumienie inżynierii promptów](../../../02-prompt-engineering)
- [Podstawy inżynierii promptów](../../../02-prompt-engineering)
  - [Zeroshot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Szablony promptów](../../../02-prompt-engineering)
- [Zaawansowane wzorce](../../../02-prompt-engineering)
- [Uruchom aplikację](../../../02-prompt-engineering)
- [Zrzuty ekranu aplikacji](../../../02-prompt-engineering)
- [Eksploracja wzorców](../../../02-prompt-engineering)
  - [Niskie vs wysokie zaangażowanie](../../../02-prompt-engineering)
  - [Wykonywanie zadania (nagłówki narzędzi)](../../../02-prompt-engineering)
  - [Kod autorefleksyjny](../../../02-prompt-engineering)
  - [Analiza strukturalna](../../../02-prompt-engineering)
  - [Wielokrotna rozmowa](../../../02-prompt-engineering)
  - [Rozumowanie krok po kroku](../../../02-prompt-engineering)
  - [Ograniczony wynik](../../../02-prompt-engineering)
- [Czego naprawdę się uczysz](../../../02-prompt-engineering)
- [Kolejne kroki](../../../02-prompt-engineering)

## Film instruktażowy

Obejrzyj tę sesję na żywo, która wyjaśnia, jak rozpocząć pracę z tym modułem:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Inżynieria promptów z LangChain4j – sesja na żywo" width="800"/></a>

## Czego się nauczysz

Poniższy diagram przedstawia przegląd kluczowych tematów i umiejętności, które rozwiniesz w tym module — od technik udoskonalania promptów po krok po kroku przebieg pracy, którego będziesz się trzymać.

<img src="../../../translated_images/pl/what-youll-learn.c68269ac048503b2.webp" alt="Czego się nauczysz" width="800"/>

W poprzednich modułach zapoznałeś się z podstawowymi interakcjami LangChain4j z modelami GitHub oraz zobaczyłeś, jak pamięć umożliwia konwersacyjne AI z Azure OpenAI. Teraz skupimy się na tym, jak zadajesz pytania — czyli na samych promptach — wykorzystując GPT-5.2 z Azure OpenAI. Sposób, w jaki strukturyzujesz prompt, diametralnie wpływa na jakość uzyskiwanych odpowiedzi. Zaczynamy od przeglądu podstawowych technik promptowania, a potem przechodzimy do ośmiu zaawansowanych wzorców, które w pełni wykorzystują możliwości GPT-5.2.

Użyjemy GPT-5.2, ponieważ wprowadza on kontrolę rozumowania - możesz powiedzieć modelowi, ile ma się zastanawiać przed udzieleniem odpowiedzi. To sprawia, że różne strategie promptowania stają się bardziej widoczne i pomaga zrozumieć, kiedy stosować każde podejście. Skorzystamy też z mniejszych limitów narzuconych przez Azure dla GPT-5.2 w porównaniu do modeli GitHub.

## Wymagania wstępne

- Ukończony moduł 01 (wdrożone zasoby Azure OpenAI)
- Plik `.env` w katalogu głównym z danymi uwierzytelniającymi Azure (stworzony przez `azd up` w moduł 01)

> **Uwaga:** Jeśli nie ukończyłeś modułu 01, najpierw wykonaj tam instrukcje wdrożenia.

## Zrozumienie inżynierii promptów

W istocie inżynieria promptów to różnica między niejasnymi a precyzyjnymi instrukcjami, jak pokazuje poniższe porównanie.

<img src="../../../translated_images/pl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Co to jest inżynieria promptów?" width="800"/>

Inżynieria promptów polega na projektowaniu wprowadzanego tekstu tak, by konsekwentnie uzyskiwać potrzebne wyniki. To nie tylko zadawanie pytań - to strukturyzowanie próśb tak, by model dokładnie rozumiał, czego oczekujesz i jak to dostarczyć.

Pomyśl o tym jak o dawaniu instrukcji współpracownikowi. "Napraw błąd" jest niejasne. "Napraw wyjątek null pointer w UserService.java w linii 45, dodając sprawdzenie na null" to już konkret. Modele językowe działają podobnie - specyfika i struktura mają znaczenie.

Poniższy diagram pokazuje, jak LangChain4j wpisuje się w ten obraz — łącząc twoje wzorce promptów z modelem poprzez bloki budujące SystemMessage i UserMessage.

<img src="../../../translated_images/pl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Jak LangChain4j pasuje" width="800"/>

LangChain4j dostarcza infrastrukturę — połączenia z modelem, pamięć i typy komunikatów — podczas gdy wzorce promptów to po prostu starannie zbudowany tekst przekazywany przez tę infrastrukturę. Kluczowymi elementami są `SystemMessage` (ustawia zachowanie i rolę AI) oraz `UserMessage` (przekazuje twoją rzeczywistą prośbę).

## Podstawy inżynierii promptów

Pięć podstawowych technik pokazanych poniżej stanowi fundament skutecznej inżynierii promptów. Każda z nich odnosi się do innego aspektu komunikacji z modelami językowymi.

<img src="../../../translated_images/pl/five-patterns-overview.160f35045ffd2a94.webp" alt="Przegląd pięciu wzorców inżynierii promptów" width="800"/>

Zanim zanurzymy się w zaawansowane wzorce tego modułu, zróbmy przegląd pięciu podstawowych technik promptowania. To podstawowe elementy, które każdy inżynier promptów powinien znać. Jeśli już przeszedłeś przez [moduł szybkiego startu](../00-quick-start/README.md#2-prompt-patterns), widziałeś je w praktyce — oto koncepcyjna rama, która za nimi stoi.

### Zeroshot Prompting

Najprostsze podejście: daj modelowi bezpośrednią instrukcję bez przykładów. Model polega całkowicie na swoim treningu, by zrozumieć i wykonać zadanie. Sprawdza się dobrze przy prostych prośbach, gdzie oczekiwane zachowanie jest oczywiste.

<img src="../../../translated_images/pl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zeroshot Prompting" width="800"/>

*Bezpośrednia instrukcja bez przykładów — model wywnioskuje zadanie tylko z instrukcji*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odpowiedź: "Pozytywna"
```
  
**Kiedy używać:** Proste klasyfikacje, bezpośrednie pytania, tłumaczenia lub każde zadanie, które model może wykonać bez dodatkowego wsparcia.

### Few-Shot Prompting

Podaj przykłady, które pokazują wzorzec, jaki chcesz, by model naśladował. Model uczy się oczekiwanego formatu wejścia-wyjścia z twoich przykładów i stosuje go do nowych danych. To znacznie poprawia spójność przy zadaniach, gdzie pożądany format lub zachowanie nie jest oczywiste.

<img src="../../../translated_images/pl/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Nauka na przykładach — model identyfikuje wzorzec i stosuje go do nowych danych*

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
  
**Kiedy używać:** Niestandardowe klasyfikacje, spójne formatowanie, zadania specyficzne dla dziedziny lub gdy zeroshot daje niespójne wyniki.

### Chain of Thought

Poproś model o pokazanie swojego rozumowania krok po kroku. Zamiast od razu podawać odpowiedź, model rozbija problem i przechodzi przez każdy etap jawnie. Poprawia to dokładność w zadaniach matematycznych, logicznych i wieloetapowych.

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
  
**Kiedy używać:** Problemy matematyczne, zagadki logiczne, debugowanie lub każde zadanie, gdzie pokazanie procesu rozumowania zwiększa dokładność i zaufanie.

### Role-Based Prompting

Ustaw personę lub rolę dla AI przed zadaniem pytania. To daje kontekst, który kształtuje ton, głębokość i skupienie odpowiedzi. "Architekt oprogramowania" doradzi inaczej niż "młodszy programista" czy "audytor bezpieczeństwa".

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
  
**Kiedy używać:** Przeglądy kodu, korepetycje, analizy specyficzne dla dziedziny lub gdy potrzebujesz odpowiedzi dostosowanych do poziomu wiedzy lub perspektywy.

### Szablony promptów

Twórz wielokrotnie używalne prompty z zmiennymi zastępczymi. Zamiast pisać nowy prompt za każdym razem, zdefiniuj szablon raz i wypełniaj różne wartości. Klasa `PromptTemplate` LangChain4j ułatwia to ze składnią `{{zmienna}}`.

<img src="../../../translated_images/pl/prompt-templates.14bfc37d45f1a933.webp" alt="Szablony promptów" width="800"/>

*Wielokrotnie używalne prompty z miejscami na zmienne — jeden szablon, wiele zastosowań*

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
  
**Kiedy używać:** Powtarzalne zapytania z różnymi danymi, wsadowe przetwarzanie, budowanie powtarzalnych przepływów AI lub każda sytuacja, gdzie struktura promptu pozostaje ta sama, ale zmieniają się dane.

---

Te pięć podstawowych technik daje solidny zestaw narzędzi do większości zadań promptowania. Reszta tego modułu opiera się na nich, prezentując **osiem zaawansowanych wzorców** wykorzystujących kontrolę rozumowania GPT-5.2, samoocenę i możliwości strukturalnego wyjścia.

## Zaawansowane wzorce

Po omówieniu podstaw przejdźmy do ośmiu zaawansowanych wzorców, które czynią ten moduł wyjątkowym. Nie każdy problem wymaga tego samego podejścia. Niektóre pytania potrzebują szybkich odpowiedzi, inne głębokiego namysłu. Niektóre wymagają widocznego rozumowania, inne tylko wyników. Każdy z poniższych wzorców jest zoptymalizowany pod kątem innego scenariusza — a kontrola rozumowania GPT-5.2 jeszcze bardziej uwydatnia różnice.

<img src="../../../translated_images/pl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osiem wzorców promptowania" width="800"/>

*Przegląd ośmiu wzorców inżynierii promptów i ich zastosowań*

GPT-5.2 dodaje kolejną warstwę do tych wzorców: *kontrolę rozumowania*. Suwak poniżej pokazuje, jak możesz regulować wysiłek myślowy modelu — od szybkich, bezpośrednich odpowiedzi do głębokiej, dokładnej analizy.

<img src="../../../translated_images/pl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrola rozumowania w GPT-5.2" width="800"/>

*Kontrola rozumowania GPT-5.2 pozwala określić, ile myślenia powinien wykonać model — od szybkich odpowiedzi do głębokich analiz*

**Niskie zaangażowanie (szybkie i skupione)** – Dla prostych pytań, gdzie chcesz szybkich, bezpośrednich odpowiedzi. Model wykonuje minimalne rozumowanie – maksimum 2 kroki. Używaj tego do obliczeń, wyszukiwań lub prostych pytań.

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
> - „Jaka jest różnica między wzorcami niskiego a wysokiego zaangażowania?”  
> - „Jak znaczniki XML w promptach pomagają strukturyzować odpowiedź AI?”  
> - „Kiedy używać wzorców autorefleksji vs bezpośredniej instrukcji?”

**Wysokie zaangażowanie (głębokie i dokładne)** – Dla złożonych problemów, gdzie chcesz kompleksowej analizy. Model dokładnie bada temat i prezentuje szczegółowe rozumowanie. Używaj tego do projektowania systemów, decyzji architektonicznych lub złożonych badań.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**Wykonywanie zadania (postęp krok po kroku)** – Dla wieloetapowych przepływów pracy. Model przedstawia z góry plan, opisuje każdy krok w trakcie działania, a potem podsumowuje. Używaj do migracji, wdrożeń lub dowolnych procesów wieloetapowych.

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
  
Chain-of-Thought explicite prosi model o pokazanie procesu rozumowania, co poprawia dokładność w złożonych zadaniach. Rozbicie krok po kroku pomaga zarówno ludziom, jak i AI zrozumieć logikę.

> **🤖 Wypróbuj z czatem [GitHub Copilot](https://github.com/features/copilot):** Zapytaj o ten wzorzec:  
> - „Jak dostosować wzorzec wykonywania zadania do długotrwałych operacji?”  
> - „Jakie są najlepsze praktyki dotyczące struktury nagłówków narzędzi w produkcyjnych aplikacjach?”  
> - „Jak mogę przechwytywać i wyświetlać informacje o postępie w UI?”

Poniższy diagram ilustruje przepływ pracy Plan → Wykonaj → Podsumuj.

<img src="../../../translated_images/pl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Wzorzec wykonywania zadania" width="800"/>

*Przepływ pracy Plan → Wykonaj → Podsumuj dla wieloetapowych zadań*

**Kod autorefleksyjny** – Do generowania kodu gotowego do produkcji. Model tworzy kod zgodny ze standardami produkcyjnymi z właściwą obsługą błędów. Używaj tego podczas budowania nowych funkcji lub usług.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```
  
Poniższy diagram pokazuje pętlę iteracyjnej poprawy — generuj, oceniaj, identyfikuj słabości i udoskonalaj, aż kod spełni standardy produkcyjne.

<img src="../../../translated_images/pl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cykl autorefleksji" width="800"/>

*Pętla iteracyjnej poprawy – generowanie, ocena, identyfikacja problemów, ulepszanie, powtarzanie*

**Analiza strukturalna** – Do spójnej oceny. Model przegląda kod używając stałego schematu (poprawność, praktyki, wydajność, bezpieczeństwo, łatwość utrzymania). Używaj tego do przeglądów kodu lub oceny jakości.

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
  
> **🤖 Wypróbuj z czatem [GitHub Copilot](https://github.com/features/copilot):** Zapytaj o analizę strukturalną:  
> - „Jak dostosować ramy analizy do różnych typów przeglądów kodu?”  
> - „Jaki jest najlepszy sposób na programowe parsowanie i wykorzystanie strukturalnego wyniku?”  
> - „Jak zapewnić spójność poziomów ważności w różnych sesjach przeglądu?”

Poniższy diagram pokazuje, jak ten uporządkowany schemat organizuje przegląd kodu w spójne kategorie z poziomami ważności.

<img src="../../../translated_images/pl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Wzorzec analizy strukturalnej" width="800"/>

*Schemat dla spójnych przeglądów kodu z poziomami ważności*

**Wielokrotna rozmowa** – Do konwersacji wymagających kontekstu. Model pamięta poprzednie wiadomości i na nich bazuje. Używaj do interaktywnych sesji pomocy lub złożonych Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
Poniższy diagram wizualizuje, jak kontekst rozmowy kumuluje się z każdą turą i jak odnosi się do limitu tokenów modelu.

<img src="../../../translated_images/pl/context-memory.dff30ad9fa78832a.webp" alt="Pamięć kontekstowa" width="800"/>

*Jak kontekst rozmowy narasta podczas wielu tur aż do osiągnięcia limitu tokenów*
**Rozumowanie krok po kroku** - Dla problemów wymagających widocznej logiki. Model pokazuje explicite rozumowanie dla każdego kroku. Używaj tego do zadań matematycznych, łamigłówek logicznych lub gdy potrzebujesz zrozumieć proces myślenia.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Poniższy diagram ilustruje, jak model dzieli problemy na wyraźne, ponumerowane kroki logiczne.

<img src="../../../translated_images/pl/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Rozkładanie problemów na wyraźne kroki logiczne*

**Wymuszony format odpowiedzi** - Dla odpowiedzi z konkretnymi wymaganiami co do formatu. Model ściśle przestrzega zasad formatu i długości. Używaj tego do streszczeń lub gdy potrzebujesz precyzyjnej struktury wyjścia.

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

Poniższy diagram pokazuje, jak ograniczenia wymuszają na modelu produkcję odpowiedzi ściśle zgodnej z wymaganiami co do formatu i długości.

<img src="../../../translated_images/pl/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Wymuszanie specyficznego formatu, długości i wymagań strukturalnych*

## Uruchomienie aplikacji

**Weryfikacja wdrożenia:**

Upewnij się, że plik `.env` istnieje w katalogu głównym i zawiera poświadczenia Azure (utworzone podczas Modułu 01). Uruchom to z katalogu modułu (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # Powinno wyświetlać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Powinno wyświetlać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Uruchom aplikację:**

> **Uwaga:** Jeśli już uruchomiłeś wszystkie aplikacje używając `./start-all.sh` z katalogu głównego (zgodnie z opisem w Module 01), ten moduł jest już uruchomiony na porcie 8083. Możesz pominąć poniższe polecenia startowe i od razu przejść do http://localhost:8083.

**Opcja 1: Korzystanie z Spring Boot Dashboard (zalecane dla użytkowników VS Code)**

Kontener deweloperski zawiera rozszerzenie Spring Boot Dashboard, które zapewnia wizualny interfejs do zarządzania wszystkimi aplikacjami Spring Boot. Znajdziesz je na pasku aktywności po lewej stronie VS Code (ikona Spring Boot).

Z poziomu Spring Boot Dashboard możesz:
- Zobaczyć wszystkie dostępne aplikacje Spring Boot w przestrzeni roboczej
- Uruchamiać/zatrzymywać aplikacje jednym kliknięciem
- Przeglądać logi aplikacji w czasie rzeczywistym
- Monitorować status aplikacji

Wystarczy kliknąć przycisk „play” obok „prompt-engineering”, aby uruchomić ten moduł lub uruchomić wszystkie moduły jednocześnie.

<img src="../../../translated_images/pl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard w VS Code — uruchamiaj, zatrzymuj i monitoruj wszystkie moduły z jednego miejsca*

**Opcja 2: Korzystanie z skryptów shell**

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

> **Uwaga:** Jeśli wolisz najpierw ręcznie zbudować wszystkie moduły przed uruchomieniem:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

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

Oto główny interfejs modułu do inżynierii promptów, gdzie możesz eksperymentować ze wszystkimi ośmioma wzorcami obok siebie.

<img src="../../../translated_images/pl/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Główny panel pokazujący 8 wzorców inżynierii promptów wraz z ich cechami i przypadkami użycia*

## Eksploracja wzorców

Interfejs webowy pozwala eksperymentować z różnymi strategiami promptowania. Każdy wzorzec rozwiązuje inne problemy – wypróbuj je, aby zobaczyć, kiedy każde podejście się sprawdza.

> **Uwaga: Streaming vs Brak streamingu** — Każda strona wzorca oferuje dwa przyciski: **🔴 Stream Response (Live)** oraz opcję **Bez streamingu**. Streaming wykorzystuje Server-Sent Events (SSE) do wyświetlania tokenów na żywo w trakcie generowania przez model, więc widzisz postęp natychmiast. Opcja bez streamingu czeka na całą odpowiedź przed jej wyświetleniem. Przy promptach wywołujących głębokie rozumowanie (np. High Eagerness, Self-Reflecting Code), wywołanie bez streamingu może trwać bardzo długo – czasem minuty – bez widocznego feedbacku. **Używaj streamingu podczas eksperymentów z zaawansowanymi promptami**, aby widzieć pracę modelu i uniknąć wrażenia, że żądanie się zawiesiło.  
>
> **Uwaga: Wymagania przeglądarki** — funkcja streamingu używa Fetch Streams API (`response.body.getReader()`), które wymaga pełnej przeglądarki (Chrome, Edge, Firefox, Safari). Nie działa ona w wbudowanej przeglądarce Simple Browser VS Code, ponieważ jej webview nie obsługuje ReadableStream API. Jeśli korzystasz z Simple Browser, przyciski bez streamingu nadal działają normalnie – tylko przyciski streamingu są dotknięte tym ograniczeniem. Otwórz `http://localhost:8083` w zewnętrznej przeglądarce, aby w pełni korzystać z funkcji.

### Niska vs Wysoka gorliwość (Eagerness)

Zadaj proste pytanie, np. „Ile to jest 15% z 200?” używając Low Eagerness. Otrzymasz natychmiastową, bezpośrednią odpowiedź. Teraz zapytaj o coś złożonego, np. „Zaprojektuj strategię cache’owania dla API o dużym ruchu” używając High Eagerness. Kliknij **🔴 Stream Response (Live)** i zobacz szczegółowe rozumowanie modelu token po tokenie. Ten sam model, ta sama struktura pytania – ale prompt mówi mu, ile myślenia ma wykonać.

### Wykonywanie zadań (wstępne instrukcje dla narzędzi)

Wielokrokowe przepływy pracy korzystają na wcześniejszym planowaniu i narracji postępu. Model opisuje, co zrobi, opowiada o każdym kroku, a następnie podsumowuje wyniki.

### Samooceniający kod

Spróbuj „Stwórz usługę walidacji email”. Zamiast tylko generować kod i przerywać, model generuje, ocenia zgodnie z kryteriami jakości, identyfikuje słabości i poprawia. Zobaczysz, jak iteruje aż kod spełni standardy produkcyjne.

### Analiza strukturalna

Przeglądy kodu potrzebują spójnych ram oceny. Model analizuje kod używając stałych kategorii (poprawność, praktyki, wydajność, bezpieczeństwo) z poziomami wagi.

### Czaty wieloetapowe

Zapytaj „Co to jest Spring Boot?”, a zaraz potem „Pokaż przykład”. Model pamięta Twoje pierwsze pytanie i daje konkretny przykład Spring Boot. Bez pamięci drugie pytanie byłoby zbyt ogólne.

### Rozumowanie krok po kroku

Wybierz problem matematyczny i spróbuj go z Step-by-Step Reasoning i Low Eagerness. Low eagerness szybko daje odpowiedź – szybka, ale nieprzejrzysta. Step-by-step pokazuje każdy krok obliczeń i decyzji.

### Wymuszony format odpowiedzi

Gdy potrzebujesz konkretnych formatów lub limitów słów, ten wzorzec wymusza ścisłą zgodność. Spróbuj wygenerować streszczenie z dokładnie 100 słowami w formacie punktowanym.

## Czego naprawdę się uczysz

**Wysiłek rozumowania zmienia wszystko**

GPT-5.2 pozwala kontrolować wysiłek obliczeniowy przez Twoje promptowanie. Niski wysiłek oznacza szybkie odpowiedzi z minimalną eksploracją. Wysoki wysiłek oznacza, że model poświęca czas na głębokie myślenie. Uczysz się dopasowywać wysiłek do złożoności zadania – nie trać czasu na proste pytania, ale też nie spiesz się przy skomplikowanych decyzjach.

**Struktura kieruje zachowaniem**

Zauważyłeś tagi XML w promptach? Nie są one dekoracją. Modele chętniej stosują się do uporządkowanych instrukcji niż do tekstu swobodnego. Gdy potrzebujesz procesów wieloetapowych lub złożonej logiki, struktura pomaga modelowi śledzić, gdzie jest i co następuje dalej. Poniższy diagram rozbija dobrze zbudowany prompt, pokazując jak tagi takie jak `<system>`, `<instructions>`, `<context>`, `<user-input>`, i `<constraints>` organizują instrukcje w klarowne sekcje.

<img src="../../../translated_images/pl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomia dobrze zbudowanego promptu z wyraźnymi sekcjami i organizacją w stylu XML*

**Jakość przez samoocenę**

Wzorce samoocenia działają przez explicite wprowadzenie kryteriów jakości. Zamiast mieć nadzieję, że model „zrobi to poprawnie”, mówisz mu dokładnie, co oznacza „poprawnie”: poprawna logika, obsługa błędów, wydajność, bezpieczeństwo. Model może wtedy ocenić własny output i go ulepszyć. To zmienia generowanie kodu z loterii w proces.

**Kontekst jest ograniczony**

Rozmowy wieloetapowe działają przez dołączanie historii wiadomości do każdego żądania. Ale jest limit – każdy model ma maksymalną liczbę tokenów. Wraz z rosnącą rozmową musisz stosować strategie, by utrzymać istotny kontekst bez przekraczania limitu. Ten moduł pokazuje, jak działa pamięć; później nauczysz się, kiedy streszczać, kiedy zapominać, a kiedy pobierać.

## Kolejne kroki

**Następny moduł:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Nawigacja:** [← Poprzedni: Moduł 01 - Wprowadzenie](../01-introduction/README.md) | [Powrót do głównego](../README.md) | [Następny: Moduł 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zrzeczenie się odpowiedzialności**:
Niniejszy dokument został przetłumaczony przy użyciu usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mimo że dążymy do dokładności, prosimy pamiętać, że tłumaczenia automatyczne mogą zawierać błędy lub niedokładności. Oryginalny dokument w języku źródłowym powinien być uznawany za autorytatywne źródło. W przypadku informacji krytycznych zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->