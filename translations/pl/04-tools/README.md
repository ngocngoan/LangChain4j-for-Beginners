# Moduł 04: Agenci AI z Narzędziami

## Spis treści

- [Czego się nauczysz](../../../04-tools)
- [Wymagania wstępne](../../../04-tools)
- [Zrozumienie agentów AI z narzędziami](../../../04-tools)
- [Jak działa wywoływanie narzędzi](../../../04-tools)
  - [Definicje narzędzi](../../../04-tools)
  - [Podejmowanie decyzji](../../../04-tools)
  - [Wykonanie](../../../04-tools)
  - [Generowanie odpowiedzi](../../../04-tools)
  - [Architektura: Auto-wiring w Spring Boot](../../../04-tools)
- [Łańcuchowanie narzędzi](../../../04-tools)
- [Uruchom aplikację](../../../04-tools)
- [Korzystanie z aplikacji](../../../04-tools)
  - [Wypróbuj proste użycie narzędzia](../../../04-tools)
  - [Przetestuj łańcuchowanie narzędzi](../../../04-tools)
  - [Zobacz przebieg konwersacji](../../../04-tools)
  - [Eksperymentuj z różnymi zapytaniami](../../../04-tools)
- [Kluczowe koncepcje](../../../04-tools)
  - [Wzorzec ReAct (rozumowanie i działanie)](../../../04-tools)
  - [Opis narzędzi ma znaczenie](../../../04-tools)
  - [Zarządzanie sesją](../../../04-tools)
  - [Obsługa błędów](../../../04-tools)
- [Dostępne narzędzia](../../../04-tools)
- [Kiedy używać agentów opartych na narzędziach](../../../04-tools)
- [Narzędzia vs RAG](../../../04-tools)
- [Kolejne kroki](../../../04-tools)

## Czego się nauczysz

Do tej pory nauczyłeś się prowadzić rozmowy z AI, skutecznie strukturyzować prompt’y i osadzać odpowiedzi w dokumentach. Ale wciąż istnieje fundamentalne ograniczenie: modele językowe mogą generować tylko tekst. Nie potrafią sprawdzić pogody, wykonać obliczeń, zapytać bazy danych ani wchodzić w interakcję z systemami zewnętrznymi.

Narzędzia to zmieniają. Dając modelowi dostęp do funkcji, które może wywołać, przekształcasz go z generatora tekstu w agenta, który może podejmować działania. Model decyduje, kiedy potrzebuje narzędzia, które narzędzie wybrać i jakie przekazać parametry. Twój kod wykonuje funkcję i zwraca wynik. Model integruje ten wynik w odpowiedzi.

## Wymagania wstępne

- Ukończony [Moduł 01 - Wprowadzenie](../01-introduction/README.md) (wdrożone zasoby Azure OpenAI)
- Zalecane ukończenie poprzednich modułów (ten moduł odwołuje się do [konceptów RAG z Modułu 03](../03-rag/README.md) w porównaniu Narzędzia vs RAG)
- Plik `.env` w katalogu głównym z poświadczeniami Azure (utworzony przez `azd up` w Moduł 01)

> **Uwaga:** Jeśli nie ukończyłeś Modułu 01, najpierw postępuj zgodnie z instrukcjami wdrażania tam zawartymi.

## Zrozumienie agentów AI z narzędziami

> **📝 Uwaga:** Termin „agenci” w tym module odnosi się do asystentów AI rozszerzonych o możliwość wywoływania narzędzi. To różni się od wzorców **Agentic AI** (autonomicznych agentów z planowaniem, pamięcią i wieloetapowym rozumowaniem), które omówimy w [Moduł 05: MCP](../05-mcp/README.md).

Bez narzędzi model językowy może jedynie generować tekst na podstawie danych treningowych. Zapytaj go o aktualną pogodę, a będzie musiał zgadywać. Daj mu narzędzia, a będzie mógł wywołać API pogodowe, wykonać obliczenia lub zapytać bazę danych — a następnie wpleść te prawdziwe wyniki w odpowiedź.

<img src="../../../translated_images/pl/what-are-tools.724e468fc4de64da.webp" alt="Bez narzędzi vs z narzędziami" width="800"/>

*Bez narzędzi model tylko zgaduje — z narzędziami może wywoływać API, wykonywać obliczenia i zwracać dane w czasie rzeczywistym.*

Agent AI z narzędziami podąża za wzorcem **Reasoning and Acting (ReAct)**. Model nie tylko odpowiada — myśli o tym, czego potrzebuje, działa wywołując narzędzie, obserwuje wynik, a potem decyduje, czy wykonać kolejne działanie, czy dostarczyć ostateczną odpowiedź:

1. **Reason (rozumuj)** — Agent analizuje pytanie użytkownika i określa, jakich informacji potrzebuje
2. **Act (działaj)** — Agent wybiera odpowiednie narzędzie, generuje właściwe parametry i je wywołuje
3. **Observe (obserwuj)** — Agent otrzymuje wynik narzędzia i ocenia rezultat
4. **Repeat or Respond (powtórz lub odpowiedz)** — Jeśli potrzebne są kolejne dane, agent wraca do początku; w przeciwnym razie formułuje odpowiedź w języku naturalnym

<img src="../../../translated_images/pl/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Wzorzec ReAct" width="800"/>

*Cykl ReAct — agent rozważa, co zrobić, działa wywołując narzędzie, obserwuje rezultat i powtarza, aż może udzielić ostatecznej odpowiedzi.*

Dzieje się to automatycznie. Definiujesz narzędzia i ich opisy. Model zajmuje się podejmowaniem decyzji, kiedy i jak ich używać.

## Jak działa wywoływanie narzędzi

### Definicje narzędzi

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Definiujesz funkcje z jasnymi opisami i specyfikacjami parametrów. Model widzi te opisy w swoim systemowym promptcie i rozumie, co robi każde narzędzie.

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

Poniższy diagram rozkłada każdą adnotację i pokazuje, jak każdy element pomaga AI zrozumieć, kiedy wywołać narzędzie i jakie argumenty przekazać:

<img src="../../../translated_images/pl/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomia definicji narzędzi" width="800"/>

*Anatomia definicji narzędzia — @Tool mówi AI, kiedy go użyć, @P opisuje każdy parametr, a @AiService łączy wszystko razem przy starcie.*

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) i zapytaj:
> - „Jak zintegrować prawdziwe API pogodowe, np. OpenWeatherMap, zamiast danych mockowanych?”
> - „Co sprawia, że opis narzędzia jest dobry i pomaga AI używać go poprawnie?”
> - „Jak obsłużyć błędy API i limity zapytań w implementacji narzędzi?”

### Podejmowanie decyzji

Gdy użytkownik pyta „Jaka jest pogoda w Seattle?”, model nie wybiera narzędzia losowo. Porównuje intencję użytkownika z opisami wszystkich dostępnych narzędzi, ocenia ich trafność i wybiera najlepsze dopasowanie. Następnie generuje ustrukturyzowane wywołanie funkcji z właściwymi parametrami — w tym przypadku ustawiając `location` na `"Seattle"`.

Jeśli żadne narzędzie nie pasuje do zapytania użytkownika, model odpowiada na podstawie własnej wiedzy. Jeśli pasuje kilka, wybiera to najbardziej precyzyjne.

<img src="../../../translated_images/pl/decision-making.409cd562e5cecc49.webp" alt="Jak AI decyduje, którego narzędzia użyć" width="800"/>

*Model ocenia każde dostępne narzędzie pod kątem intencji użytkownika i wybiera najlepsze dopasowanie — dlatego ważne jest pisanie jasnych, konkretnych opisów narzędzi.*

### Wykonanie

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot automatycznie łączy deklaratywny interfejs `@AiService` ze wszystkimi zarejestrowanymi narzędziami, a LangChain4j wykonuje wywołania narzędzi automatycznie. Za kulisami pełen przebieg wywołania narzędzia przechodzi przez sześć etapów — od pytania użytkownika w języku naturalnym aż po odpowiedź naturalnym językiem:

<img src="../../../translated_images/pl/tool-calling-flow.8601941b0ca041e6.webp" alt="Przebieg wywoływania narzędzi" width="800"/>

*Przebieg end-to-end — użytkownik zadaje pytanie, model wybiera narzędzie, LangChain4j je wykonuje, a model wplata wynik w naturalną odpowiedź.*

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) i zapytaj:
> - „Jak działa wzorzec ReAct i dlaczego jest skuteczny dla agentów AI?”
> - „Jak agent decyduje, którego narzędzia użyć i w jakiej kolejności?”
> - „Co się stanie, jeśli wykonanie narzędzia się nie powiedzie — jak powinienem solidnie obsługiwać błędy?”

### Generowanie odpowiedzi

Model otrzymuje dane pogodowe i formatuje je na odpowiedź w języku naturalnym dla użytkownika.

### Architektura: Auto-wiring w Spring Boot

Ten moduł korzysta z integracji LangChain4j z Spring Boot z deklaratywnymi interfejsami `@AiService`. Przy starcie Spring Boot odkrywa każdy `@Component` zawierający metody oznaczone `@Tool`, Twój bean `ChatModel` oraz `ChatMemoryProvider` — a następnie łączy je wszystkie w jeden interfejs `Assistant` bez potrzeby pisania boilerplate.

<img src="../../../translated_images/pl/spring-boot-wiring.151321795988b04e.webp" alt="Architektura auto-wiring Spring Boot" width="800"/>

*Interfejs @AiService łączy ChatModel, komponenty narzędzi i dostawcę pamięci — Spring Boot automatycznie obsługuje całe łączenie.*

Kluczowe zalety tego podejścia:

- **Auto-wiring Spring Boot** — ChatModel i narzędzia automatycznie wstrzykiwane
- **Wzorzec @MemoryId** — Automatyczne zarządzanie pamięcią bazującą na sesji
- **Pojedyncza instancja** — Asystent tworzony raz i używany ponownie dla lepszej wydajności
- **Bezpieczne typowo wykonanie** — Metody Java wywoływane bezpośrednio z konwersją typów
- **Orkiestracja wielowywołań** — Automatycznie obsługuje łańcuchowanie narzędzi
- **Zero boilerplate** — Brak ręcznych wywołań `AiServices.builder()` lub map pamięci HashMap

Alternatywne podejścia (ręczne `AiServices.builder()`) wymagają więcej kodu i nie oferują korzyści integracji ze Spring Boot.

## Łańcuchowanie narzędzi

**Łańcuchowanie narzędzi** — Prawdziwa moc agentów opartych na narzędziach ujawnia się, gdy jedno pytanie wymaga użycia kilku narzędzi. Zapytaj „Jaka jest pogoda w Seattle w stopniach Fahrenheita?”, a agent automatycznie połączy dwa narzędzia: najpierw wywoła `getCurrentWeather` by uzyskać temperaturę w stopniach Celsjusza, potem przekaże tę wartość do `celsiusToFahrenheit` do konwersji — wszystko w jednej turze rozmowy.

<img src="../../../translated_images/pl/tool-chaining-example.538203e73d09dd82.webp" alt="Przykład łańcuchowania narzędzi" width="800"/>

*Łańcuchowanie narzędzi w akcji — agent najpierw wywołuje getCurrentWeather, potem przekazuje wynik w Celsjuszach do celsiusToFahrenheit i dostarcza skombinowaną odpowiedź.*

**Łagodne awarie** — Zapytaj o pogodę w mieście, którego nie ma w danych mockowanych. Narzędzie zwraca komunikat o błędzie, a AI tłumaczy, że nie może pomóc, zamiast się zawieszać. Narzędzia zawodzą bezpiecznie. Poniższy diagram kontrastuje oba podejścia — z poprawną obsługą błędów agent łapie wyjątek i odpowiada pomocnie, a bez tego cały program się zawiesza:

<img src="../../../translated_images/pl/error-handling-flow.9a330ffc8ee0475c.webp" alt="Przebieg obsługi błędów" width="800"/>

*Gdy narzędzie zawodzi, agent łapie błąd i odpowiada pomocnym wyjaśnieniem zamiast się zawiesić.*

Dzieje się to w jednej turze rozmowy. Agent samodzielnie orkiestruje wiele wywołań narzędzi.

## Uruchom aplikację

**Zweryfikuj wdrożenie:**

Upewnij się, że plik `.env` znajduje się w katalogu głównym z poświadczeniami Azure (utworzony podczas Modułu 01). Uruchom to z katalogu modułu (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Powinien pokazać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Powinien pokazać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Uruchom aplikację:**

> **Uwaga:** Jeśli już uruchomiłeś wszystkie aplikacje za pomocą `./start-all.sh` z katalogu głównego (zgodnie z opisem w Moduł 01), ten moduł jest już uruchomiony na porcie 8084. Możesz pominąć poniższe polecenia startu i przejść bezpośrednio do http://localhost:8084.

**Opcja 1: Korzystanie z Spring Boot Dashboard (zalecane dla użytkowników VS Code)**

Kontener deweloperski zawiera rozszerzenie Spring Boot Dashboard, które umożliwia zarządzanie wszystkimi aplikacjami Spring Boot przez GUI. Znajdziesz je w pasku aktywności po lewej stronie VS Code (szukaj ikony Spring Boot).

W Spring Boot Dashboard możesz:
- Zobaczyć wszystkie dostępne aplikacje Spring Boot w workspace
- Uruchamiać/zatrzymywać aplikacje jednym kliknięciem
- Przeglądać logi aplikacji w czasie rzeczywistym
- Monitorować status aplikacji

Po prostu kliknij przycisk play obok „tools”, aby uruchomić ten moduł lub uruchom wszystkie moduły naraz.

Tak wygląda Spring Boot Dashboard w VS Code:

<img src="../../../translated_images/pl/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard w VS Code — uruchamiaj, zatrzymuj i monitoruj wszystkie moduły z jednego miejsca*

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
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

Oba skrypty automatycznie ładują zmienne środowiskowe z głównego pliku `.env` i zbudują JAR-y, jeśli nie istnieją.

> **Uwaga:** Jeśli wolisz zbudować wszystkie moduły ręcznie przed startem:
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

Otwórz http://localhost:8084 w przeglądarce.

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

Aplikacja udostępnia interfejs webowy, w którym możesz wchodzić w interakcję z agentem AI mającym dostęp do narzędzi pogodowych i konwersji temperatur. Tak wygląda interfejs — zawiera przykłady szybkiego startu i panel czatu do wysyłania zapytań:
<a href="images/tools-homepage.png"><img src="../../../translated_images/pl/tools-homepage.4b4cd8b2717f9621.webp" alt="Interfejs narzędzi agenta AI" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Interfejs narzędzi agenta AI – szybkie przykłady i interfejs czatu do interakcji z narzędziami*

### Wypróbuj proste użycie narzędzia

Zacznij od prostego zapytania: „Przelicz 100 stopni Fahrenheita na Celsjusza”. Agent rozpoznaje, że potrzebuje narzędzia do konwersji temperatury, wywołuje je z odpowiednimi parametrami i zwraca wynik. Zwróć uwagę, jak naturalne to się wydaje – nie określiłeś, którego narzędzia użyć ani jak je wywołać.

### Przetestuj łączenie narzędzi

Teraz spróbuj czegoś bardziej złożonego: „Jaka jest pogoda w Seattle i przelicz ją na Fahrenheita?” Obserwuj, jak agent wykonuje to krok po kroku. Najpierw pobiera pogodę (która zwraca Celsjusza), rozpoznaje, że musi przekonwertować na Fahrenheita, wywołuje narzędzie konwersji i łączy oba wyniki w jedną odpowiedź.

### Zobacz przebieg rozmowy

Interfejs czatu zachowuje historię rozmowy, umożliwiając prowadzenie wielokrotnych wymian. Możesz zobaczyć wszystkie wcześniejsze zapytania i odpowiedzi, co ułatwia śledzenie konwersacji i rozumienie, jak agent buduje kontekst na przestrzeni wielu wymian.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/pl/tools-conversation-demo.89f2ce9676080f59.webp" alt="Rozmowa z wieloma wywołaniami narzędzi" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Wielokrotna wymiana pokazująca proste konwersje, zapytania o pogodę i łączenie narzędzi*

### Eksperymentuj z różnymi zapytaniami

Wypróbuj różne kombinacje:
- Zapytania o pogodę: „Jaka jest pogoda w Tokio?”
- Konwersje temperatur: „Ile to 25°C w kelwinach?”
- Zapytania łączone: „Sprawdź pogodę w Paryżu i powiedz, czy jest powyżej 20°C”

Zauważ, jak agent interpretuje język naturalny i mapuje go na odpowiednie wywołania narzędzi.

## Kluczowe koncepcje

### Wzorzec ReAct (Rozumowanie i działanie)

Agent na przemian rozważa (decyduje, co zrobić) i działa (używa narzędzi). Ten wzorzec umożliwia autonomiczne rozwiązywanie problemów, a nie tylko reagowanie na instrukcje.

### Opisy narzędzi mają znaczenie

Jakość opisów narzędzi bezpośrednio wpływa na sposób, w jaki agent z nich korzysta. Jasne, konkretne opisy pomagają modelowi zrozumieć, kiedy i jak wywołać każde narzędzie.

### Zarządzanie sesją

Adnotacja `@MemoryId` umożliwia automatyczne zarządzanie pamięcią na podstawie sesji. Każde ID sesji dostaje własną instancję `ChatMemory`, zarządzaną przez komponent `ChatMemoryProvider`, dzięki czemu wielu użytkowników może jednocześnie korzystać z agenta bez mieszania konwersacji. Poniższy diagram pokazuje, jak wielu użytkowników jest kierowanych do izolowanych pamięci na podstawie ich ID sesji:

<img src="../../../translated_images/pl/session-management.91ad819c6c89c400.webp" alt="Zarządzanie sesją z @MemoryId" width="800"/>

*Każde ID sesji mapuje się na izolowaną historię rozmowy — użytkownicy nigdy nie widzą wiadomości innych.*

### Obsługa błędów

Narzędzia mogą zawodzić — API mogą ulec timeoutom, parametry mogą być nieprawidłowe, zewnętrzne usługi mogą przestać działać. Agenci produkcyjni potrzebują obsługi błędów, aby model mógł wyjaśnić problemy lub spróbować alternatyw zamiast crashować całą aplikację. Gdy narzędzie zgłasza wyjątek, LangChain4j go przechwytuje i przekazuje komunikat o błędzie do modelu, który następnie może wytłumaczyć problem językiem naturalnym.

## Dostępne narzędzia

Poniższy diagram pokazuje szeroki ekosystem narzędzi, które możesz budować. Ten moduł demonstruje narzędzia pogodowe i do temperatury, ale ten sam wzorzec `@Tool` działa dla dowolnej metody Javy – od zapytań do bazy danych po przetwarzanie płatności.

<img src="../../../translated_images/pl/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ekosystem narzędzi" width="800"/>

*Każda metoda Javy oznaczona `@Tool` staje się dostępna dla AI — wzorzec rozszerza się na bazy danych, API, e-maile, operacje na plikach i inne.*

## Kiedy używać agentów opartych na narzędziach

Nie każde zapytanie wymaga użycia narzędzi. Decyzja zależy od tego, czy AI musi wchodzić w interakcje z systemami zewnętrznymi, czy może odpowiadać z własnej wiedzy. Poniższy przewodnik podsumowuje, kiedy narzędzia dodają wartość, a kiedy są zbędne:

<img src="../../../translated_images/pl/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Kiedy używać narzędzi" width="800"/>

*Szybki przewodnik — narzędzia są do danych w czasie rzeczywistym, obliczeń i akcji; wiedza ogólna i zadania twórcze nie wymagają ich.*

## Narzędzia vs RAG

Moduły 03 i 04 rozszerzają możliwości AI, ale w zasadniczo różny sposób. RAG daje modelowi dostęp do **wiedzy** przez pobieranie dokumentów. Narzędzia dają modelowi możliwość wykonywania **akcji** przez wywoływanie funkcji. Poniższy diagram porównuje te dwa podejścia — od sposobu działania każdego przepływu po kompromisy między nimi:

<img src="../../../translated_images/pl/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Porównanie narzędzi i RAG" width="800"/>

*RAG pobiera informacje ze statycznych dokumentów — narzędzia wykonują akcje i pobierają dynamiczne, aktualne dane. Wiele systemów produkcyjnych łączy oba podejścia.*

W praktyce wiele systemów produkcyjnych łączy oba podejścia: RAG do ugruntowania odpowiedzi w dokumentacji oraz narzędzia do pobierania danych na żywo lub wykonywania operacji.

## Kolejne kroki

**Następny moduł:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Nawigacja:** [← Poprzedni: Moduł 03 - RAG](../03-rag/README.md) | [Powrót do głównego](../README.md) | [Następny: Moduł 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:  
Niniejszy dokument został przetłumaczony za pomocą usługi tłumaczeń AI [Co-op Translator](https://github.com/Azure/co-op-translator). Pomimo naszych starań o dokładność, prosimy pamiętać, że automatyczne tłumaczenia mogą zawierać błędy lub niedokładności. Oryginalny dokument w języku źródłowym powinien być uznawany za źródło autorytatywne. W przypadku informacji krytycznych zalecane jest skorzystanie z profesjonalnego, ludzkiego tłumaczenia. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z użycia tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->