# Moduł 04: Agenci AI z narzędziami

## Spis treści

- [Czego się nauczysz](../../../04-tools)
- [Wymagania wstępne](../../../04-tools)
- [Zrozumienie agentów AI z narzędziami](../../../04-tools)
- [Jak działa wywoływanie narzędzi](../../../04-tools)
  - [Definicje narzędzi](../../../04-tools)
  - [Podejmowanie decyzji](../../../04-tools)
  - [Wykonanie](../../../04-tools)
  - [Generowanie odpowiedzi](../../../04-tools)
  - [Architektura: Automatyczne łączenie w Spring Boot](../../../04-tools)
- [Łączenie narzędzi](../../../04-tools)
- [Uruchom aplikację](../../../04-tools)
- [Korzystanie z aplikacji](../../../04-tools)
  - [Wypróbuj proste użycie narzędzi](../../../04-tools)
  - [Przetestuj łączenie narzędzi](../../../04-tools)
  - [Zobacz przebieg rozmowy](../../../04-tools)
  - [Eksperymentuj z różnymi zapytaniami](../../../04-tools)
- [Kluczowe pojęcia](../../../04-tools)
  - [Wzorzec ReAct (rozumowanie i działanie)](../../../04-tools)
  - [Opisy narzędzi mają znaczenie](../../../04-tools)
  - [Zarządzanie sesjami](../../../04-tools)
  - [Obsługa błędów](../../../04-tools)
- [Dostępne narzędzia](../../../04-tools)
- [Kiedy używać agentów opartych na narzędziach](../../../04-tools)
- [Narzędzia vs RAG](../../../04-tools)
- [Następne kroki](../../../04-tools)

## Czego się nauczysz

Do tej pory nauczyłeś się, jak prowadzić rozmowy z AI, jak efektywnie budować prompt’y oraz jak oprzeć odpowiedzi na Twoich dokumentach. Jednak nadal istnieje fundamentalne ograniczenie: modele językowe potrafią tylko generować tekst. Nie mogą sprawdzić pogody, wykonać obliczeń, zapytać bazy danych ani wchodzić w interakcje z zewnętrznymi systemami.

Narzędzia to zmieniają. Dzięki dostępowi do wywoływalnych funkcji, przekształcasz model z generatora tekstu w agenta, który może podejmować działania. Model decyduje, kiedy potrzebuje narzędzia, którego narzędzia użyć i jakie parametry przekazać. Twój kod wykonuje funkcję i zwraca wynik. Model włącza ten wynik do swojej odpowiedzi.

## Wymagania wstępne

- Ukończony Moduł 01 (wdrożone zasoby Azure OpenAI)
- Plik `.env` w katalogu głównym z poświadczeniami Azure (utworzony przez `azd up` w Module 01)

> **Uwaga:** Jeśli nie ukończyłeś Modułu 01, najpierw postępuj zgodnie z instrukcjami wdrożenia tam zawartymi.

## Zrozumienie agentów AI z narzędziami

> **📝 Uwaga:** Termin „agenci” w tym module odnosi się do asystentów AI wyposażonych w możliwość wywoływania narzędzi. To różni się od wzorców **Agentic AI** (autonomicznych agentów z planowaniem, pamięcią i wieloetapowym rozumowaniem), które omówimy w [Moduł 05: MCP](../05-mcp/README.md).

Bez narzędzi model językowy może jedynie generować tekst na podstawie danych treningowych. Zapytaj go o aktualną pogodę, a będzie musiał zgadywać. Daj mu narzędzia, a może wywołać API pogodowe, wykonać obliczenia lub zapytać bazę danych — a następnie wpleść te rzeczywiste wyniki w swoją odpowiedź.

<img src="../../../translated_images/pl/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*Bez narzędzi model tylko zgaduje — z narzędziami może wywoływać API, wykonywać obliczenia i zwracać dane w czasie rzeczywistym.*

Agent AI z narzędziami stosuje wzorzec **Reasoning and Acting (ReAct)**. Model nie tylko odpowiada — myśli o tym, czego potrzebuje, działa wywołując narzędzie, obserwuje wynik, a potem decyduje, czy działać ponownie, czy dostarczyć ostateczną odpowiedź:

1. **Rozumowanie** — Agent analizuje pytanie użytkownika i określa, jakich informacji potrzebuje
2. **Działanie** — Agent wybiera odpowiednie narzędzie, generuje poprawne parametry i wywołuje je
3. **Obserwacja** — Agent odbiera wynik narzędzia i ocenia rezultat
4. **Powtarzanie lub odpowiedź** — Jeśli potrzeba więcej danych, agent wraca do kroku 1; w przeciwnym razie formułuje odpowiedź w języku naturalnym

<img src="../../../translated_images/pl/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*Cykl ReAct — agent rozważa, co zrobić, działa wywołując narzędzie, obserwuje wynik i powtarza, aż może dostarczyć ostateczną odpowiedź.*

Dzieje się to automatycznie. Definiujesz narzędzia i ich opisy. Model sam podejmuje decyzje, kiedy i jak ich użyć.

## Jak działa wywoływanie narzędzi

### Definicje narzędzi

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Definiujesz funkcje z jasnymi opisami i specyfikacją parametrów. Model widzi te opisy w swoim prompt’cie systemowym i rozumie, co każde narzędzie wykonuje.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Twoja logika wyszukiwania pogody
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Asystent jest automatycznie konfigurowany przez Spring Boot z:
// - Bean ChatModel
// - Wszystkie metody @Tool z klas oznaczonych @Component
// - ChatMemoryProvider do zarządzania sesją
```

Poniższy diagram rozbija każdą adnotację i pokazuje, jak każda część pomaga AI rozumieć, kiedy wywołać narzędzie i jakie argumenty przekazać:

<img src="../../../translated_images/pl/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*Budowa definicji narzędzia — @Tool informuje AI, kiedy go użyć, @P opisuje każdy parametr, a @AiService łączy wszystko razem podczas uruchamiania.*

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) i zapytaj:
> - „Jak zintegrować prawdziwe API pogodowe, takie jak OpenWeatherMap, zamiast danych mockowych?”
> - „Co tworzy dobry opis narzędzia, który pomaga AI poprawnie z niego korzystać?”
> - „Jak obsługiwać błędy API i limity wywołań w implementacjach narzędzi?”

### Podejmowanie decyzji

Gdy użytkownik pyta „Jaka jest pogoda w Seattle?”, model nie wybiera narzędzia losowo. Porównuje intencję użytkownika z opisami wszystkich dostępnych narzędzi, ocenia je pod kątem trafności i wybiera najlepsze dopasowanie. Następnie generuje ustrukturyzowane wywołanie funkcji z odpowiednimi parametrami — w tym przypadku ustawia `location` na `"Seattle"`.

Jeśli żadne narzędzie nie pasuje do zapytania użytkownika, model odpowiada z własnej wiedzy. Jeśli pasuje wiele narzędzi, wybiera to najbardziej szczegółowe.

<img src="../../../translated_images/pl/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*Model ocenia każde dostępne narzędzie względem intencji użytkownika i wybiera najlepsze dopasowanie — dlatego ważne jest pisanie jasnych, precyzyjnych opisów narzędzi.*

### Wykonanie

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot automatycznie łączy deklaratywny interfejs `@AiService` ze wszystkimi zarejestrowanymi narzędziami, a LangChain4j wykonuje wywołania narzędzi automatycznie. W tle wywołanie narzędzia przechodzi przez sześć etapów — od pytania użytkownika w języku naturalnym aż po odpowiedź w języku naturalnym:

<img src="../../../translated_images/pl/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*Cały proces — użytkownik zadaje pytanie, model wybiera narzędzie, LangChain4j je wykonuje, a model wplata wynik w naturalną odpowiedź.*

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) i zapytaj:
> - „Jak działa wzorzec ReAct i dlaczego jest efektywny dla agentów AI?”
> - „Jak agent decyduje, którego narzędzia użyć i w jakiej kolejności?”
> - „Co się stanie, jeśli wykonanie narzędzia się nie powiedzie – jak solidnie obsługiwać błędy?”

### Generowanie odpowiedzi

Model otrzymuje dane pogodowe i formatuje je na odpowiedź w języku naturalnym dla użytkownika.

### Architektura: Automatyczne łączenie w Spring Boot

Ten moduł używa integracji LangChain4j ze Spring Boot z deklaratywnymi interfejsami `@AiService`. Przy uruchomieniu Spring Boot wykrywa każdy `@Component` zawierający metody `@Tool`, Twój bean `ChatModel` oraz `ChatMemoryProvider` — i łączy je wszystkie w pojedynczy interfejs `Assistant` bez żadnego boilerplate.

<img src="../../../translated_images/pl/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*Interfejs @AiService łączy ChatModel, komponenty narzędzi i dostawcę pamięci — Spring Boot obsługuje całą konfigurację automatycznie.*

Kluczowe zalety tego podejścia:

- **Automatyczne łączenie Spring Boot** — ChatModel i narzędzia wstrzykiwane automatycznie
- **Wzorzec @MemoryId** — Automatyczne zarządzanie pamięcią opartą na sesji
- **Pojedyncza instancja** — Assistant tworzony raz i używany ponownie dla lepszej wydajności
- **Wykonanie typowane** — Metody Java wywoływane bezpośrednio z konwersją typów
- **Orkiestracja wielokrotna** — Automatyczna obsługa łączenia narzędzi
- **Zero boilerplate** — Brak ręcznych wywołań `AiServices.builder()` lub map pamięci

Alternatywne podejścia (ręczne `AiServices.builder()`) wymagają więcej kodu i nie korzystają z zalet integracji Spring Boot.

## Łączenie narzędzi

**Łączenie narzędzi** — Prawdziwa moc agentów opartych na narzędziach pokazuje się, gdy pojedyncze pytanie wymaga kilku narzędzi. Zapytaj „Jaka jest pogoda w Seattle w stopniach Fahrenheita?”, a agent automatycznie połączy dwa narzędzia: najpierw wywoła `getCurrentWeather`, aby uzyskać temperaturę w stopniach Celsjusza, a potem przekaże tę wartość do `celsiusToFahrenheit` na konwersję — wszystko w jednej turze rozmowy.

<img src="../../../translated_images/pl/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*Łączenie narzędzi w akcji — agent najpierw wywołuje getCurrentWeather, potem przekazuje wynik w Celsjuszach do celsiusToFahrenheit i dostarcza połączoną odpowiedź.*

Tak wygląda to w działającej aplikacji — agent łączy dwa wywołania narzędzi w jednej turze rozmowy:

<a href="images/tool-chaining.png"><img src="../../../translated_images/pl/tool-chaining.3b25af01967d6f7b.webp" alt="Tool Chaining" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Rzeczywiste wyjście aplikacji — agent automatycznie łączy getCurrentWeather → celsiusToFahrenheit w jednej turze.*

**Płynne awarie** — Zapytaj o pogodę w mieście, które nie znajduje się w danych mockowych. Narzędzie zwraca komunikat o błędzie, a AI wyjaśnia, że nie może pomóc, zamiast się zawiesić. Narzędzia zawiodą bezpiecznie.

<img src="../../../translated_images/pl/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*Gdy narzędzie zawodzi, agent przechwytuje błąd i odpowiada pomocnym wyjaśnieniem zamiast zawieszać się.*

Dzieje się to w jednej turze rozmowy. Agent autonomicznie orkiestruje wiele wywołań narzędzi.

## Uruchom aplikację

**Sprawdź wdrożenie:**

Upewnij się, że plik `.env` istnieje w katalogu głównym z poświadczeniami Azure (utworzony podczas Modułu 01):  
```bash
cat ../.env  # Powinno pokazywać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Uruchom aplikację:**

> **Uwaga:** Jeśli już uruchomiłeś wszystkie aplikacje za pomocą `./start-all.sh` z Modułu 01, ten moduł działa już na porcie 8084. Możesz pominąć poniższe polecenia startowe i przejść bezpośrednio do http://localhost:8084.

**Opcja 1: Korzystanie z Spring Boot Dashboard (zalecane dla użytkowników VS Code)**

Kontener deweloperski zawiera rozszerzenie Spring Boot Dashboard, które zapewnia graficzny interfejs do zarządzania wszystkimi aplikacjami Spring Boot. Znajdziesz je w pasku aktywności po lewej stronie VS Code (ikona Spring Boot).

W Spring Boot Dashboard możesz:  
- Zobaczyć wszystkie dostępne aplikacje Spring Boot w workspace  
- Uruchamiać/zatrzymywać aplikacje jednym kliknięciem  
- Przeglądać logi aplikacji w czasie rzeczywistym  
- Monitorować status aplikacji  

Wystarczy kliknąć przycisk uruchamiania obok „tools”, aby włączyć ten moduł, lub uruchomić wszystkie moduły jednocześnie.

<img src="../../../translated_images/pl/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

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
cd 04-tools
./start.sh
```
  
**PowerShell:**  
```powershell
cd 04-tools
.\start.ps1
```
  
Oba skrypty automatycznie ładują zmienne środowiskowe z pliku `.env` w katalogu głównym i zbudują pliki JAR, jeśli jeszcze nie istnieją.

> **Uwaga:** Jeśli wolisz najpierw zbudować moduły ręcznie przed uruchomieniem:
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
  
Otwórz http://localhost:8084 w swojej przeglądarce.

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
  
## Korzystanie z aplikacji

Aplikacja udostępnia interfejs webowy, w którym możesz rozmawiać z agentem AI mającym dostęp do narzędzi pogodowych i konwersji temperatur.

<a href="images/tools-homepage.png"><img src="../../../translated_images/pl/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Interfejs narzędzi agenta AI — szybkie przykłady i okno czatu do interakcji z narzędziami*

### Wypróbuj proste użycie narzędzi
Zacznij od prostego zapytania: "Przelicz 100 stopni Fahrenheita na Celsjusza". Agent rozpoznaje, że potrzebuje narzędzia do konwersji temperatury, wywołuje je z odpowiednimi parametrami i zwraca wynik. Zauważ, jak naturalne to się wydaje – nie określiłeś, którego narzędzia użyć ani jak je wywołać.

### Test Łańcuchowania Narzędzi

Teraz spróbuj czegoś bardziej złożonego: "Jaka jest pogoda w Seattle i przelicz ją na Fahrenheita?" Obserwuj, jak agent wykonuje to krok po kroku. Najpierw pobiera pogodę (która zwraca wartość w Celsjuszach), następnie rozpoznaje potrzebę konwersji na Fahrenheita, wywołuje narzędzie konwersji i łączy oba wyniki w jednej odpowiedzi.

### Zobacz Przepływ Konwersacji

Interfejs czatu przechowuje historię rozmowy, co pozwala na interakcje wieloetapowe. Możesz zobaczyć wszystkie wcześniejsze zapytania i odpowiedzi, co ułatwia śledzenie rozmowy i rozumienie, jak agent buduje kontekst przez kolejne wymiany.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/pl/tools-conversation-demo.89f2ce9676080f59.webp" alt="Rozmowa z wieloma wywołaniami narzędzi" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Wieloetapowa rozmowa pokazująca proste konwersje, wyszukiwanie pogody i łańcuchowanie narzędzi*

### Eksperymentuj z Różnymi Zapytaniami

Wypróbuj różne kombinacje:
- Wyszukiwanie pogody: "Jaka jest pogoda w Tokio?"
- Konwersje temperatur: "Ile to jest 25°C w Kelwinach?"
- Zapytania złożone: "Sprawdź pogodę w Paryżu i powiedz, czy jest powyżej 20°C"

Zauważ, jak agent interpretuje naturalny język i przypisuje go do odpowiednich wywołań narzędzi.

## Kluczowe Koncepcje

### Wzorzec ReAct (Rozumowanie i Działanie)

Agent na przemian rozważa (decyduje, co zrobić) i działa (używa narzędzi). Ten wzorzec umożliwia autonomiczne rozwiązywanie problemów, a nie tylko reagowanie na instrukcje.

### Opisy Narzędzi Mają Znaczenie

Jakość opisów narzędzi bezpośrednio wpływa na to, jak dobrze agent z nich korzysta. Jasne, precyzyjne opisy pomagają modelowi zrozumieć, kiedy i jak wywołać każde narzędzie.

### Zarządzanie Sesją

Adnotacja `@MemoryId` umożliwia automatyczne zarządzanie pamięcią opartą na sesji. Każdy identyfikator sesji otrzymuje własną instancję `ChatMemory` zarządzaną przez bean `ChatMemoryProvider`, dzięki czemu wielu użytkowników może jednocześnie prowadzić rozmowy z agentem bez mieszania się ich historii.

<img src="../../../translated_images/pl/session-management.91ad819c6c89c400.webp" alt="Zarządzanie sesjami z @MemoryId" width="800"/>

*Każdy identyfikator sesji jest mapowany na oddzielną historię rozmowy — użytkownicy nigdy nie widzą wiadomości innych.*

### Obsługa Błędów

Narzędzia mogą zawodzić — API mogą przekroczyć limit czasu, parametry mogą być nieprawidłowe, zewnętrzne usługi mogą przestać działać. Produkcyjne agentury potrzebują obsługi błędów, by model mógł wyjaśnić problemy lub spróbować alternatyw, zamiast zawieszać całą aplikację. Gdy narzędzie zgłasza wyjątek, LangChain4j go przechwytuje i przekazuje komunikat o błędzie z powrotem do modelu, który może wtedy wytłumaczyć problem w języku naturalnym.

## Dostępne Narzędzia

Poniższy diagram pokazuje szeroki ekosystem narzędzi, które możesz zbudować. Ten moduł demonstruje narzędzia pogodowe i temperatury, ale ten sam wzorzec `@Tool` działa dla dowolnej metody w Javie — od zapytań do baz danych po przetwarzanie płatności.

<img src="../../../translated_images/pl/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ekosystem narzędzi" width="800"/>

*Każda metoda Javy oznaczona `@Tool` staje się dostępna dla AI — wzorzec rozszerza się na bazy danych, API, e-maile, operacje plikowe i inne.*

## Kiedy Używać Agentów opartych na Narzędziach

<img src="../../../translated_images/pl/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Kiedy używać narzędzi" width="800"/>

*Krótki przewodnik decyzyjny — narzędzia są do danych w czasie rzeczywistym, obliczeń i działań; ogólna wiedza i zadania twórcze tego nie wymagają.*

**Używaj narzędzi, gdy:**
- Odpowiedź wymaga danych w czasie rzeczywistym (pogoda, ceny akcji, zapasy)
- Musisz wykonać obliczenia wykraczające poza prostą matematykę
- Dostęp do baz danych lub API
- Wykonanie działań (wysyłanie e-maili, tworzenie zgłoszeń, aktualizacja rekordów)
- Łączenie wielu źródeł danych

**Nie używaj narzędzi, gdy:**
- Pytania można odpowiedzieć na podstawie ogólnej wiedzy
- Odpowiedź jest wyłącznie konwersacyjna
- Opóźnienia narzędzi spowodowałyby zbyt wolne działanie

## Narzędzia a RAG

Moduły 03 i 04 rozszerzają możliwości AI, ale w zasadniczo różny sposób. RAG daje modelowi dostęp do **wiedzy** poprzez wyszukiwanie dokumentów. Narzędzia dają modelowi możliwość podejmowania **działań** poprzez wywoływanie funkcji.

<img src="../../../translated_images/pl/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Porównanie narzędzi i RAG" width="800"/>

*RAG pobiera informacje ze statycznych dokumentów — Narzędzia wykonują działania i pobierają dynamiczne, bieżące dane. Wiele produkcyjnych systemów łączy oba podejścia.*

W praktyce wiele systemów produkcyjnych łączy oba podejścia: RAG do oparcia odpowiedzi na dokumentacji, a Narzędzia do pobierania danych na żywo lub wykonywania operacji.

## Kolejne Kroki

**Następny moduł:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Nawigacja:** [← Poprzedni: Moduł 03 - RAG](../03-rag/README.md) | [Powrót do głównego](../README.md) | [Następny: Moduł 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:  
Niniejszy dokument został przetłumaczony za pomocą usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Chociaż dążymy do dokładności, prosimy pamiętać, że tłumaczenia automatyczne mogą zawierać błędy lub nieścisłości. Oryginalny dokument w języku źródłowym powinien być uznawany za autorytatywne źródło. W przypadku istotnych informacji zaleca się skorzystanie z profesjonalnego tłumaczenia wykonywanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z użycia tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->