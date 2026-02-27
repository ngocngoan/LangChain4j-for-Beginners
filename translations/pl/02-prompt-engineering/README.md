# Moduł 02: Inżynieria podpowiedzi z GPT-5.2

## Spis treści

- [Przegląd wideo](../../../02-prompt-engineering)
- [Czego się nauczysz](../../../02-prompt-engineering)
- [Wymagania wstępne](../../../02-prompt-engineering)
- [Zrozumienie inżynierii podpowiedzi](../../../02-prompt-engineering)
- [Podstawy inżynierii podpowiedzi](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Szablony podpowiedzi](../../../02-prompt-engineering)
- [Wzorce zaawansowane](../../../02-prompt-engineering)
- [Korzystanie z istniejących zasobów Azure](../../../02-prompt-engineering)
- [Zrzuty ekranu aplikacji](../../../02-prompt-engineering)
- [Eksploracja wzorców](../../../02-prompt-engineering)
  - [Niskie vs wysokie zaangażowanie](../../../02-prompt-engineering)
  - [Wykonywanie zadań (wstępne instrukcje narzędzi)](../../../02-prompt-engineering)
  - [Kod z autorefleksją](../../../02-prompt-engineering)
  - [Analiza strukturalna](../../../02-prompt-engineering)
  - [Czat wieloetapowy](../../../02-prompt-engineering)
  - [Rozumowanie krok po kroku](../../../02-prompt-engineering)
  - [Wynik ograniczony](../../../02-prompt-engineering)
- [Co tak naprawdę się uczysz](../../../02-prompt-engineering)
- [Kolejne kroki](../../../02-prompt-engineering)

## Przegląd wideo

Obejrzyj tę sesję na żywo, która wyjaśnia, jak rozpocząć pracę z tym modułem: [Inżynieria podpowiedzi z LangChain4j - sesja na żywo](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## Czego się nauczysz

<img src="../../../translated_images/pl/what-youll-learn.c68269ac048503b2.webp" alt="Czego się nauczysz" width="800"/>

W poprzednim module zobaczyłeś, jak pamięć umożliwia konwersacyjną AI i użyłeś modeli GitHub do podstawowych interakcji. Teraz skupimy się na tym, jak zadawać pytania — same podpowiedzi — używając GPT-5.2 z Azure OpenAI. Sposób, w jaki formułujesz podpowiedzi, ma ogromny wpływ na jakość uzyskiwanych odpowiedzi. Zaczynamy od przeglądu podstawowych technik podpowiadania, a następnie przechodzimy do ośmiu zaawansowanych wzorców, które w pełni wykorzystują możliwości GPT-5.2.

Używamy GPT-5.2, ponieważ wprowadza kontrolę rozumowania - możesz powiedzieć modelowi, jak dużo ma myśleć przed udzieleniem odpowiedzi. To sprawia, że różne strategie podpowiadania są bardziej widoczne i pomaga zrozumieć, kiedy używać której z nich. Skorzystamy też na mniejszych limitach przepustowości Azure dla GPT-5.2 w porównaniu z modelami GitHub.

## Wymagania wstępne

- Zakończony Moduł 01 (wdrożone zasoby Azure OpenAI)
- Plik `.env` w katalogu głównym z danymi uwierzytelniającymi Azure (utworzony przez `azd up` w Module 01)

> **Uwaga:** Jeśli nie ukończyłeś Modułu 01, najpierw wykonaj instrukcje wdrożenia z tamtego modułu.

## Zrozumienie inżynierii podpowiedzi

<img src="../../../translated_images/pl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Co to jest inżynieria podpowiedzi?" width="800"/>

Inżynieria podpowiedzi to projektowanie tekstu wejściowego, który konsekwentnie dostarcza potrzebne wyniki. To nie tylko zadawanie pytań – to strukturyzowanie zapytań tak, aby model dokładnie zrozumiał, czego chcesz i jak to dostarczyć.

Pomyśl o tym jak o dawaniu instrukcji współpracownikowi. „Napraw błąd” jest niejasne. „Napraw wyjątek null pointer w UserService.java linia 45, dodając sprawdzenie na null” jest konkretne. Modele językowe działają tak samo – liczy się konkretność i struktura.

<img src="../../../translated_images/pl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Jak LangChain4j się wpisuje" width="800"/>

LangChain4j zapewnia infrastrukturę — połączenia z modelami, pamięć i typy wiadomości — podczas gdy wzorce podpowiedzi to po prostu starannie ustrukturyzowany tekst przesyłany przez tę infrastrukturę. Kluczowymi elementami są `SystemMessage` (które ustawia zachowanie i rolę AI) oraz `UserMessage` (które niesie twoją właściwą prośbę).

## Podstawy inżynierii podpowiedzi

<img src="../../../translated_images/pl/five-patterns-overview.160f35045ffd2a94.webp" alt="Przegląd pięciu wzorców inżynierii podpowiedzi" width="800"/>

Zanim przejdziemy do wzorców zaawansowanych w tym module, przypomnijmy sobie pięć podstawowych technik podpowiadania. To fundamenty, które każdy inżynier podpowiedzi powinien znać. Jeśli już przerobiłeś [moduł szybkiego startu](../00-quick-start/README.md#2-prompt-patterns), widziałeś je w akcji — oto konceptualne ramy za nimi.

### Zero-Shot Prompting

Najprostsze podejście: daj modelowi bezpośrednią instrukcję bez przykładów. Model opiera się całkowicie na swoim treningu, aby zrozumieć i wykonać zadanie. Sprawdza się dobrze przy prostych prośbach, gdzie oczekiwane działanie jest oczywiste.

<img src="../../../translated_images/pl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Bezpośrednia instrukcja bez przykładów — model wywnioskuje zadanie z samej instrukcji*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Odpowiedź: "Pozytywna"
```

**Kiedy używać:** proste klasyfikacje, pytania bezpośrednie, tłumaczenia lub dowolne zadanie, które model może obsłużyć bez dodatkowego prowadzenia.

### Few-Shot Prompting

Podaj przykłady, które pokazują wzorzec, którego chcesz, aby model się trzymał. Model uczy się oczekiwanego formatu wejścia i wyjścia z twoich przykładów i stosuje go do nowych danych. To znacznie poprawia spójność dla zadań, gdzie pożądany format lub zachowanie nie są oczywiste.

<img src="../../../translated_images/pl/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Uczenie się na przykładach — model identyfikuje wzorzec i stosuje go do nowych danych*

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

**Kiedy używać:** niestandardowe klasyfikacje, spójne formatowanie, zadania specyficzne dla domeny lub gdy wyniki zero-shot są niespójne.

### Chain of Thought

Poproś model o pokazanie swojego rozumowania krok po kroku. Zamiast od razu udzielić odpowiedzi, model rozbija problem i explicite przechodzi przez każdą część. Poprawia to dokładność w zadaniach matematycznych, logicznych oraz wieloetapowych.

<img src="../../../translated_images/pl/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Rozumowanie krok po kroku — rozbicie złożonych problemów na explicite kroki logiczne*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model pokazuje: 15 - 8 = 7, następnie 7 + 12 = 19 jabłek
```

**Kiedy używać:** problemy matematyczne, zagadki logiczne, debugowanie lub dowolne zadanie, gdzie pokazanie procesu rozumowania poprawia dokładność i zaufanie.

### Role-Based Prompting

Ustaw osobowość lub rolę AI przed zadaniem pytania. To zapewnia kontekst, który kształtuje ton, głębokość i fokus odpowiedzi. "Architekt oprogramowania" doradzi inaczej niż "programista junior" czy "audytor bezpieczeństwa".

<img src="../../../translated_images/pl/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Ustawianie kontekstu i persony — to samo pytanie otrzymuje różną odpowiedź w zależności od przydzielonej roli*

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

**Kiedy używać:** przeglądy kodu, korepetycje, analiza specyficzna dla domeny lub gdy potrzebujesz odpowiedzi dostosowanych do określonego poziomu doświadczenia lub perspektywy.

### Szablony podpowiedzi

Twórz wielokrotnego użytku podpowiedzi z miejscami na zmienne. Zamiast pisać nową podpowiedź za każdym razem, zdefiniuj szablon raz i wypełniaj różnymi wartościami. Klasa `PromptTemplate` LangChain4j ułatwia to składnią `{{variable}}`.

<img src="../../../translated_images/pl/prompt-templates.14bfc37d45f1a933.webp" alt="Szablony podpowiedzi" width="800"/>

*Podpowiedzi wielokrotnego użytku z miejscami na zmienne — jeden szablon, wiele zastosowań*

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

**Kiedy używać:** powtarzające się zapytania z różnymi danymi, przetwarzanie wsadowe, budowanie wielorazowych przepływów AI lub każdy scenariusz, gdzie struktura podpowiedzi pozostaje taka sama, a zmieniają się dane.

---

Te pięć podstaw daje solidny zestaw narzędzi do większości zadań z podpowiadaniem. Reszta tego modułu opiera się na nich, oferując **osiem zaawansowanych wzorców**, które wykorzystują kontrolę rozumowania GPT-5.2, samoocenę i strukturalny output.

## Wzorce zaawansowane

Po omówieniu podstaw przejdźmy do ośmiu zaawansowanych wzorców, które czynią ten moduł wyjątkowym. Nie wszystkie problemy potrzebują tego samego podejścia. Niektóre pytania wymagają szybkich odpowiedzi, inne głębokiego myślenia. Niektóre wymagają widocznego rozumowania, inne liczą tylko na wynik. Każdy wzorzec poniżej jest zoptymalizowany pod inny scenariusz — a kontrola rozumowania GPT-5.2 jeszcze bardziej uwypukla różnice.

<img src="../../../translated_images/pl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Osiem wzorców podpowiedzi" width="800"/>

*Przegląd ośmiu wzorców inżynierii podpowiedzi i ich zastosowań*

<img src="../../../translated_images/pl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontrola rozumowania z GPT-5.2" width="800"/>

*Kontrola rozumowania GPT-5.2 pozwala określić, ile myślenia model ma wykonać — od szybkich, bezpośrednich odpowiedzi do głębokiej eksploracji*

**Niskie zaangażowanie (Szybkie i skoncentrowane)** - Do prostych pytań, gdzie chcesz szybkich, bezpośrednich odpowiedzi. Model robi minimalne rozumowanie - maksymalnie 2 kroki. Używaj tego do obliczeń, wyszukiwań lub jednoznacznych pytań.

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
> - "Jaka jest różnica między wzorcami niskiego i wysokiego zaangażowania?"
> - "Jak tagi XML w podpowiedziach pomagają w strukturze odpowiedzi AI?"
> - "Kiedy powinienem używać wzorców autorefleksji a kiedy bezpośredniej instrukcji?"

**Wysokie zaangażowanie (Głębokie i dokładne)** - Do złożonych problemów, gdzie chcesz kompleksowej analizy. Model eksploruje dokładnie i pokazuje szczegółowe rozumowanie. Używaj do projektowania systemów, decyzji architektonicznych lub złożonych badań.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Wykonywanie zadań (postęp krok po kroku)** - Do wieloetapowych przepływów pracy. Model przedstawia plan z góry, opisuje każdy krok podczas pracy, a potem robi podsumowanie. Używaj do migracji, implementacji lub jakiegokolwiek procesu wieloetapowego.

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

Chain-of-Thought prompting wyraźnie prosi model o pokazanie procesu rozumowania, poprawiając dokładność w złożonych zadaniach. Rozbicie krok po kroku pomaga zarówno ludziom, jak i AI zrozumieć logikę.

> **🤖 Wypróbuj z czatem [GitHub Copilot](https://github.com/features/copilot):** Zapytaj o ten wzorzec:
> - "Jak dostosować wzorzec wykonywania zadań do długotrwałych operacji?"
> - "Jakie są najlepsze praktyki przy ustalaniu wstępnych instrukcji narzędzi w aplikacjach produkcyjnych?"
> - "Jak uchwycić i wyświetlić pośrednie aktualizacje postępu w UI?"

<img src="../../../translated_images/pl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Wzorzec wykonywania zadań" width="800"/>

*Planowanie → Wykonywanie → Podsumowanie przepływu zadań wieloetapowych*

**Kod z autorefleksją** - Do generowania kodu o jakości produkcyjnej. Model generuje kod według standardów produkcyjnych z odpowiednią obsługą błędów. Używaj przy tworzeniu nowych funkcji lub usług.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Cykl autorefleksji" width="800"/>

*Iteracyjna pętla poprawy – generuj, oceniaj, identyfikuj problemy, poprawiaj, powtarzaj*

**Analiza strukturalna** - Do spójnej oceny. Model przegląda kod wg ustalonej ramy (poprawność, praktyki, wydajność, bezpieczeństwo, łatwość utrzymania). Używaj do przeglądów kodu lub oceny jakości.

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
> - "Jak dostosować ramy analizy do różnych typów przeglądów kodu?"
> - "Jaki jest najlepszy sposób programowego przetwarzania i działania na podstawie strukturalnego outputu?"
> - "Jak zapewnić spójne poziomy ważności w różnych sesjach przeglądowych?"

<img src="../../../translated_images/pl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Wzorzec analizy strukturalnej" width="800"/>

*Ramy dla spójnych przeglądów kodu z poziomami ważności*

**Czat wieloetapowy** - Do rozmów, które potrzebują kontekstu. Model pamięta poprzednie wiadomości i buduje na ich podstawie. Używaj do sesji pomocy interaktywnej lub złożonych pytań i odpowiedzi.

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

*Jak kontekst rozmowy narasta przez wiele tur aż do osiągnięcia limitu tokenów*

**Rozumowanie krok po kroku** - Do problemów wymagających widocznej logiki. Model pokazuje explicite rozumowanie dla każdego kroku. Używaj do zadań matematycznych, zagadek logicznych lub gdy potrzebujesz zrozumieć proces myślenia.

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

*Rozkładanie problemów na explicite kroki logiczne*

**Wynik ograniczony** - Do odpowiedzi z określonymi wymaganiami formatowania. Model ściśle przestrzega reguł formatu i długości. Używaj do streszczeń lub gdy potrzebujesz precyzyjnej struktury wyjścia.

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

<img src="../../../translated_images/pl/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Wzorzec ograniczonego wyniku" width="800"/>

*Wymuszanie określonych wymagań formatu, długości i struktury*

## Korzystanie z istniejących zasobów Azure

**Zweryfikuj wdrożenie:**

Upewnij się, że plik `.env` znajduje się w katalogu głównym z danymi uwierzytelniającymi Azure (utworzony podczas Modułu 01):
```bash
cat ../.env  # Powinno pokazywać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Uruchom aplikację:**

> **Uwaga:** Jeśli już uruchomiłeś wszystkie aplikacje za pomocą `./start-all.sh` z Modułu 01, ten moduł już działa na porcie 8083. Możesz pominąć poniższe polecenia uruchamiania i przejść bezpośrednio do http://localhost:8083.

**Opcja 1: Korzystanie z Spring Boot Dashboard (zalecane dla użytkowników VS Code)**
Kontener deweloperski zawiera rozszerzenie Spring Boot Dashboard, które zapewnia wizualny interfejs do zarządzania wszystkimi aplikacjami Spring Boot. Znajdziesz je na pasku aktywności po lewej stronie VS Code (szukaj ikony Spring Boot).

Z poziomu Spring Boot Dashboard możesz:
- Zobaczyć wszystkie dostępne aplikacje Spring Boot w obszarze roboczym
- Uruchamiać/zatrzymywać aplikacje jednym kliknięciem
- Oglądać logi aplikacji w czasie rzeczywistym
- Monitorować status aplikacji

Po prostu kliknij przycisk odtwarzania obok "prompt-engineering", aby uruchomić ten moduł, lub uruchom wszystkie moduły naraz.

<img src="../../../translated_images/pl/dashboard.da2c2130c904aaf0.webp" alt="Panel Spring Boot" width="400"/>

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

Oba skrypty automatycznie ładują zmienne środowiskowe z głównego pliku `.env` i zbudują pliki JAR, jeśli jeszcze nie istnieją.

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

Otwórz http://localhost:8083 w przeglądarce.

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

<img src="../../../translated_images/pl/dashboard-home.5444dbda4bc1f79d.webp" alt="Ekran główny panelu" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Główny panel pokazujący wszystkie 8 wzorców inżynierii promptów wraz z ich cechami i zastosowaniami*

## Eksploracja wzorców

Interfejs webowy pozwala eksperymentować z różnymi strategiami promptów. Każdy wzorzec rozwiązuje inne problemy – wypróbuj je, aby zobaczyć, kiedy każda metoda się sprawdza.

> **Uwaga: Streaming vs Brak streamingu** — Każda strona wzorca oferuje dwa przyciski: **🔴 Stream Response (Live)** oraz opcję **Bez streamingu**. Streaming używa Server-Sent Events (SSE), aby wyświetlać tokeny na żywo w trakcie generowania przez model, dzięki czemu widzisz postęp natychmiast. Opcja bez streamingu czeka na całą odpowiedź, zanim ją wyświetli. W przypadku promptów wywołujących głębokie rozumowanie (np. High Eagerness, Self-Reflecting Code), wywołanie bez streamingu może trwać bardzo długo – czasem minuty – bez widocznej informacji zwrotnej. **Używaj streamingu podczas eksperymentów z złożonymi promptami**, aby zobaczyć pracę modelu i uniknąć wrażenia zablokowania żądania.
>
> **Uwaga: Wymagana przeglądarka** — Funkcja streamingu korzysta z Fetch Streams API (`response.body.getReader()`), które wymaga pełnoprawnej przeglądarki (Chrome, Edge, Firefox, Safari). Nie działa to w wbudowanej przeglądarce Simple Browser w VS Code, ponieważ jej widok internetowy nie obsługuje API ReadableStream. Jeśli korzystasz z Simple Browser, przyciski bez streamingu będą działać normalnie – dotyczy to tylko przycisków streamingu. Otwórz `http://localhost:8083` w zewnętrznej przeglądarce, aby uzyskać pełne doświadczenie.

### Low vs High Eagerness

Zadaj proste pytanie typu „Jaka jest 15% z 200?” korzystając z Low Eagerness. Otrzymasz natychmiastową, bezpośrednią odpowiedź. Teraz zadaj coś skomplikowanego, np. „Zaprojektuj strategię cache’owania dla API o dużym ruchu” używając High Eagerness. Kliknij **🔴 Stream Response (Live)** i obserwuj szczegółowe rozumowanie modelu pojawiające się token po tokenie. Ten sam model, ta sama konstrukcja pytania – ale prompt mówi, ile myślenia ma wykonać.

### Wykonywanie zadań (Tool Preambles)

Wielostopniowe przepływy pracy korzystają z planowania i narracji postępu. Model opisuje, co zamierza zrobić, relacjonuje każdy krok, a następnie podsumowuje wyniki.

### Self-Reflecting Code

Spróbuj „Stwórz usługę walidacji email”. Zamiast tylko generować kod i zatrzymywać się, model tworzy, ocenia według kryteriów jakości, identyfikuje słabości i poprawia. Zobaczysz, jak iteruje aż kod osiągnie standardy produkcyjne.

### Strukturalna analiza

Przeglądy kodu potrzebują spójnych ram oceny. Model analizuje kod według stałych kategorii (poprawność, praktyki, wydajność, bezpieczeństwo) z uwzględnieniem poziomów istotności.

### Wieloturnowa rozmowa

Zadaj pytanie „Co to jest Spring Boot?”, a zaraz potem „Pokaż mi przykład”. Model pamięta pierwsze pytanie i poda Ci przykładowy kod Spring Boot specjalnie dla niego. Bez pamięci drugie pytanie byłoby zbyt ogólne.

### Rozumowanie krok po kroku

Wybierz problem matematyczny i przeprowadź go zarówno z użyciem Rozumowania krok po kroku, jak i Low Eagerness. Low eagerness daje tylko odpowiedź – szybko, ale bez szczegółów. Rozumowanie krok po kroku pokazuje każdy rachunek i decyzję.

### Ograniczona odpowiedź

Gdy potrzebujesz konkretnych formatów lub liczby słów, ten wzorzec wymusza ścisłe przestrzeganie wymagań. Spróbuj wygenerować podsumowanie dokładnie 100 słowami w formie punktów.

## Czego naprawdę się uczysz

**Wysiłek rozumowania zmienia wszystko**

GPT-5.2 pozwala kontrolować nakład obliczeń przez prompty. Niski wysiłek oznacza szybkie odpowiedzi z minimalnym badaniem. Wysoki wysiłek oznacza, że model poświęca czas na głębokie przemyślenia. Uczysz się dopasowywać wysiłek do złożoności zadania – nie trać czasu na proste pytania, ale też nie śpiesz się z trudnymi decyzjami.

**Struktura kieruje zachowaniem**

Zauważyłeś tagi XML w promptach? Nie są one ozdobą. Modele lepiej przestrzegają uporządkowanych instrukcji niż tekstu swobodnego. Kiedy potrzebujesz wieloetapowych procesów lub złożonej logiki, struktura pomaga modelowi wiedzieć, gdzie jest i co robić dalej.

<img src="../../../translated_images/pl/prompt-structure.a77763d63f4e2f89.webp" alt="Struktura prompta" width="800"/>

*Anatomia dobrze zorganizowanego prompta z wyraźnymi sekcjami i strukturą w stylu XML*

**Jakość przez samoocenę**

Wzorce self-reflecting działają przez wyraźne określenie kryteriów jakości. Zamiast mieć nadzieję, że model „zrobi to dobrze”, mówisz mu dokładnie, co oznacza „dobrze”: poprawna logika, obsługa błędów, wydajność, bezpieczeństwo. Model może potem ocenić własny output i poprawić go. To zmienia generowanie kodu z loterii w proces.

**Kontekst jest ograniczony**

Wieloturnowe rozmowy działają przez dołączanie historii wiadomości do każdego zapytania. Ale jest limit – każdy model ma maksymalną liczbę tokenów. Wraz z rozrostem konwersacji, musisz stosować strategie, by utrzymać istotny kontekst bez przekraczania limitu. Ten moduł pokazuje, jak działa pamięć; później nauczysz się, kiedy podsumowywać, zapominać i przypominać.

## Kolejne kroki

**Następny moduł:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Nawigacja:** [← Poprzedni: Moduł 01 - Wprowadzenie](../01-introduction/README.md) | [Powrót do głównego](../README.md) | [Następny: Moduł 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:  
Niniejszy dokument został przetłumaczony za pomocą automatycznej usługi tłumaczeniowej AI [Co-op Translator](https://github.com/Azure/co-op-translator). Chociaż dokładamy starań, aby tłumaczenie było jak najbardziej poprawne, prosimy pamiętać, że tłumaczenia automatyczne mogą zawierać błędy lub niedokładności. Oryginalny dokument w języku źródłowym należy traktować jako wersję autorytatywną. W przypadku istotnych informacji zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->