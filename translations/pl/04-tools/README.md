# Moduł 04: Agenci AI z narzędziami

## Spis treści

- [Film instruktażowy](../../../04-tools)
- [Czego się nauczysz](../../../04-tools)
- [Wymagania wstępne](../../../04-tools)
- [Zrozumienie agentów AI z narzędziami](../../../04-tools)
- [Jak działa wywoływanie narzędzi](../../../04-tools)
  - [Definicje narzędzi](../../../04-tools)
  - [Podejmowanie decyzji](../../../04-tools)
  - [Wykonanie](../../../04-tools)
  - [Generowanie odpowiedzi](../../../04-tools)
  - [Architektura: automatyczne wiązanie Spring Boot](../../../04-tools)
- [Łańcuchowanie narzędzi](../../../04-tools)
- [Uruchomienie aplikacji](../../../04-tools)
- [Korzystanie z aplikacji](../../../04-tools)
  - [Wypróbuj proste użycie narzędzi](../../../04-tools)
  - [Przetestuj łańcuchowanie narzędzi](../../../04-tools)
  - [Zobacz przebieg rozmowy](../../../04-tools)
  - [Eksperymentuj z różnymi zapytaniami](../../../04-tools)
- [Kluczowe pojęcia](../../../04-tools)
  - [Wzorzec ReAct (Rozumowanie i Działanie)](../../../04-tools)
  - [Opisy narzędzi mają znaczenie](../../../04-tools)
  - [Zarządzanie sesją](../../../04-tools)
  - [Obsługa błędów](../../../04-tools)
- [Dostępne narzędzia](../../../04-tools)
- [Kiedy używać agentów opartych na narzędziach](../../../04-tools)
- [Narzędzia a RAG](../../../04-tools)
- [Kolejne kroki](../../../04-tools)

## Film instruktażowy

Obejrzyj tę sesję na żywo, która wyjaśnia, jak zacząć pracę z tym modułem:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="Agenci AI z narzędziami i MCP - sesja na żywo" width="800"/></a>

## Czego się nauczysz

Do tej pory nauczyłeś się, jak prowadzić rozmowy z AI, skutecznie budować prompt’y i opierać odpowiedzi na twoich dokumentach. Jednak nadal istnieje podstawowe ograniczenie: modele językowe mogą generować tylko tekst. Nie mogą sprawdzać pogody, wykonywać obliczeń, zapytać bazy danych ani współdziałać z zewnętrznymi systemami.

Narzędzia to zmieniają. Dając modelowi dostęp do funkcji, które może wywoływać, przekształcasz go z generatora tekstu w agenta, który może podejmować działania. Model decyduje, kiedy potrzebuje narzędzia, którego narzędzia użyć i jakie argumenty przekazać. Twój kod wykonuje funkcję i zwraca wynik. Model włącza ten wynik do odpowiedzi.

## Wymagania wstępne

- Ukończony [Moduł 01 - Wprowadzenie](../01-introduction/README.md) (zasoby Azure OpenAI wdrożone)
- Zalecane ukończenie poprzednich modułów (ten moduł odnosi się do [koncepcji RAG z Modułu 03](../03-rag/README.md) w porównaniu Narzędzia vs RAG)
- Plik `.env` w katalogu głównym z poświadczeniami Azure (utworzony przez `azd up` w Module 01)

> **Uwaga:** Jeśli nie ukończyłeś Modułu 01, najpierw wykonaj instrukcje wdrożeniowe tam zawarte.

## Zrozumienie agentów AI z narzędziami

> **📝 Uwaga:** Termin „agenci” w tym module odnosi się do asystentów AI rozszerzonych o możliwość wywoływania narzędzi. To różni się od wzorców **Agentic AI** (agentów autonomicznych z planowaniem, pamięcią i rozumowaniem wieloetapowym), które omówimy w [Moduł 05: MCP](../05-mcp/README.md).

Bez narzędzi model językowy może generować tylko tekst na podstawie swoich danych treningowych. Zapytaj o aktualną pogodę, a będzie musiał zgadywać. Dodaj narzędzia, a model może wywołać API pogodowe, wykonać obliczenia lub zapytać bazę danych — a następnie wpleść faktyczne rezultaty w swoją odpowiedź.

<img src="../../../translated_images/pl/what-are-tools.724e468fc4de64da.webp" alt="Bez narzędzi kontra z narzędziami" width="800"/>

*Bez narzędzi model może tylko zgadywać — z narzędziami może wywoływać API, wykonywać obliczenia i zwracać dane w czasie rzeczywistym.*

Agent AI z narzędziami działa według wzorca **Reasoning and Acting (ReAct)**. Model nie tylko odpowiada — zastanawia się, czego potrzebuje, działa wywołując narzędzie, obserwuje wynik, a następnie decyduje, czy działać dalej, czy dostarczyć ostateczną odpowiedź:

1. **Rozumowanie** — agent analizuje pytanie użytkownika i określa, jakie informacje są potrzebne
2. **Działanie** — agent wybiera właściwe narzędzie, generuje odpowiednie parametry i wywołuje funkcję
3. **Obserwacja** — agent otrzymuje wynik narzędzia i ocenia rezultat
4. **Powtarzanie lub odpowiedź** — jeśli potrzeba więcej danych, agent wraca do kroku 1; w przeciwnym razie tworzy odpowiedź w języku naturalnym

<img src="../../../translated_images/pl/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Wzorzec ReAct" width="800"/>

*Cykl ReAct — agent rozważa, co zrobić, działa wywołując narzędzie, obserwuje wynik i powtarza pętlę aż do dostarczenia ostatecznej odpowiedzi.*

Dzieje się to automatycznie. Definiujesz narzędzia i ich opisy. Model sam podejmuje decyzje, kiedy i jak ich używać.

## Jak działa wywoływanie narzędzi

### Definicje narzędzi

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Definiujesz funkcje z jasnymi opisami i specyfikacją parametrów. Model widzi te opisy w systemowym prompt i rozumie, co każde narzędzie robi.

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
// - Wszystkie metody @Tool z klas @Component
// - ChatMemoryProvider do zarządzania sesją
```

Poniższy diagram rozkłada na części każdą adnotację i pokazuje, jak każdy element pomaga SI zrozumieć, kiedy wywołać narzędzie i jakie argumenty przekazać:

<img src="../../../translated_images/pl/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomia definicji narzędzi" width="800"/>

*Anatomia definicji narzędzia — @Tool mówi SI, kiedy go użyć, @P opisuje każdy parametr, a @AiService wiąże wszystko razem przy starcie.*

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) i zapytaj:
> - „Jak zintegrowałbym prawdziwe API pogodowe, np. OpenWeatherMap, zamiast danych mock?”
> - „Co sprawia, że opis narzędzia dobrze pomaga SI używać go prawidłowo?”
> - „Jak obsługiwać błędy API i limity liczby wywołań w implementacji narzędzi?”

### Podejmowanie decyzji

Kiedy użytkownik pyta „Jaka jest pogoda w Seattle?”, model nie wybiera narzędzia losowo. Porównuje intencję użytkownika z opisami wszystkich dostępnych narzędzi, ocenia je pod kątem trafności i wybiera najlepsze. Następnie generuje znormalizowane wywołanie funkcji z odpowiednimi parametrami — w tym przypadku ustawia `location` na `"Seattle"`.

Jeśli żadne narzędzie nie pasuje do zapytania, model odpowiada na podstawie własnej wiedzy. Jeśli pasuje wiele narzędzi, wybiera to najbardziej specyficzne.

<img src="../../../translated_images/pl/decision-making.409cd562e5cecc49.webp" alt="Jak SI wybiera narzędzie" width="800"/>

*Model ocenia każde dostępne narzędzie względem intencji użytkownika i wybiera najlepsze dopasowanie — dlatego właśnie ważne jest pisanie jasnych, konkretnych opisów narzędzi.*

### Wykonanie

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot automatycznie wstrzykuje deklaratywny interfejs `@AiService` ze wszystkimi zarejestrowanymi narzędziami, a LangChain4j automatycznie wykonuje wywołania narzędzi. Za kulisami pełne wywołanie narzędzia przebiega przez sześć etapów — od pytania użytkownika w języku naturalnym po odpowiedź w tym samym języku:

<img src="../../../translated_images/pl/tool-calling-flow.8601941b0ca041e6.webp" alt="Przebieg wywoływania narzędzi" width="800"/>

*Przebieg end-to-end — użytkownik zadaje pytanie, model wybiera narzędzie, LangChain4j je wykonuje, a model wplata wynik w naturalną odpowiedź.*

Jeśli uruchomiłeś [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) w Moduł 00, już widziałeś ten wzorzec w praktyce — narzędzia `Calculator` były wywoływane w ten sam sposób. Diagram sekwencji poniżej pokazuje dokładnie, co działo się „pod maską” podczas tamtego demo:

<img src="../../../translated_images/pl/tool-calling-sequence.94802f406ca26278.webp" alt="Diagram sekwencji wywoływania narzędzi" width="800"/>

*Pętla wywoływania narzędzi z demo Quick Start — `AiServices` wysyła twoją wiadomość i schematy narzędzi do LLM, LLM odpowiada wywołaniem funkcji `add(42, 58)`, LangChain4j lokalnie wykonuje metodę `Calculator` i odsyła wynik do ostatecznej odpowiedzi.*

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) i zapytaj:
> - „Jak działa wzorzec ReAct i dlaczego jest skuteczny dla agentów AI?”
> - „Jak agent decyduje, którego narzędzia użyć i w jakiej kolejności?”
> - „Co się dzieje, jeśli wywołanie narzędzia się nie powiedzie — jak poprawnie obsługiwać błędy?”

### Generowanie odpowiedzi

Model otrzymuje dane pogodowe i formatuje je na naturalną odpowiedź dla użytkownika.

### Architektura: automatyczne wiązanie Spring Boot

Ten moduł korzysta z integracji LangChain4j ze Spring Boot i deklaratywnych interfejsów `@AiService`. Przy starcie Spring Boot wykrywa każdy `@Component` zawierający metody `@Tool`, twój bean `ChatModel` oraz `ChatMemoryProvider` — a następnie podłącza je razem do pojedynczego interfejsu `Assistant` bez żadnego boilerplate.

<img src="../../../translated_images/pl/spring-boot-wiring.151321795988b04e.webp" alt="Architektura automatycznego wiązania Spring Boot" width="800"/>

*Interfejs @AiService łączy ChatModel, komponenty narzędzi i dostawcę pamięci — Spring Boot automatycznie zarządza całym wiązaniem.*

Poniżej pełny cykl życia żądania jako diagram sekwencji — od żądania HTTP przez kontroler, usługę i auto-wiązany proxy, aż po wykonanie narzędzia i powrót:

<img src="../../../translated_images/pl/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Diagram sekwencji wywołania narzędzi w Spring Boot" width="800"/>

*Pełny cykl życia żądania w Spring Boot — żądanie HTTP przechodzi przez kontroler i serwis do auto-wiązanego proxy Assistant, które automatycznie orkiestruje wywołania LLM i narzędzi.*

Główne zalety tego podejścia:

- **Automatyczne wiązanie Spring Boot** — ChatModel i narzędzia wstrzykiwane automatycznie
- **Wzorzec @MemoryId** — Automatyczne zarządzanie pamięcią opartą na sesji
- **Pojedyncza instancja** — Asystent tworzony raz i używany ponownie dla lepszej wydajności
- **Wykonanie bezpieczne typowo** — metody Java wywoływane bezpośrednio z konwersją typów
- **Orkiestracja wielu tur** — obsługuje automatycznie łańcuchowanie narzędzi
- **Zero boilerplate** — brak ręcznych wywołań `AiServices.builder()` lub pamięci w HashMap

Alternatywne podejścia (ręczne `AiServices.builder()`) wymagają więcej kodu i nie korzystają z zalet integracji Spring Boot.

## Łańcuchowanie narzędzi

**Łańcuchowanie narzędzi** — prawdziwa moc agentów opartych na narzędziach ujawnia się, gdy pojedyncze pytanie wymaga wielu narzędzi. Zapytaj „Jaka jest pogoda w Seattle w stopniach Fahrenheita?”, a agent automatycznie połączy dwa narzędzia: najpierw wywoła `getCurrentWeather`, aby uzyskać temperaturę w stopniach Celsjusza, a następnie przekaże tę wartość do `celsiusToFahrenheit` w celu konwersji — wszystko w jednej turze rozmowy.

<img src="../../../translated_images/pl/tool-chaining-example.538203e73d09dd82.webp" alt="Przykład łańcuchowania narzędzi" width="800"/>

*Łańcuchowanie narzędzi w akcji — agent najpierw wywołuje getCurrentWeather, potem przekazuje wynik w Celsjuszach do celsiusToFahrenheit i dostarcza złożoną odpowiedź.*

**Łagodne błędy** — Poproś o pogodę w mieście, którego nie ma w danych mockowych. Narzędzie zwraca komunikat o błędzie, a AI wyjaśnia, że nie może pomóc, zamiast się zawiesić. Narzędzia zawiodą bezpiecznie. Diagram poniżej porównuje oba podejścia — przy właściwej obsłudze błędów agent łapie wyjątek i odpowiednio reaguje, a bez niej cała aplikacja się zawiesza:

<img src="../../../translated_images/pl/error-handling-flow.9a330ffc8ee0475c.webp" alt="Przebieg obsługi błędów" width="800"/>

*Gdy narzędzie zawodzi, agent łapie błąd i odpowiada pomocnym wyjaśnieniem zamiast awarii.*

Dzieje się to w jednej turze rozmowy. Agent autonomicznie orkiestruje wiele wywołań narzędzi.

## Uruchomienie aplikacji

**Potwierdzenie wdrożenia:**

Upewnij się, że plik `.env` istnieje w katalogu głównym z poświadczeniami Azure (stworzony podczas Modułu 01). Uruchom to z katalogu modułu (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Powinno pokazywać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Powinien pokazać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Uruchom aplikację:**

> **Uwaga:** Jeśli uruchomiłeś już wszystkie aplikacje za pomocą `./start-all.sh` z katalogu głównego (zgodnie z opisem w Module 01), ten moduł działa już na porcie 8084. Możesz pominąć komendy startowe poniżej i przejść bezpośrednio do http://localhost:8084.

**Opcja 1: Korzystanie z Spring Boot Dashboard (zalecane dla użytkowników VS Code)**

Kontener deweloperski zawiera rozszerzenie Spring Boot Dashboard, które udostępnia graficzny interfejs do zarządzania wszystkimi aplikacjami Spring Boot. Znajdziesz je na pasku aktywności po lewej stronie VS Code (ikona Spring Boot).

Z poziomu Spring Boot Dashboard możesz:
- Zobaczyć wszystkie dostępne aplikacje Spring Boot w przestrzeni roboczej
- Uruchamiać/wyłączać aplikacje jednym kliknięciem
- Przeglądać logi aplikacji w czasie rzeczywistym
- Monitorować status aplikacji
Po prostu kliknij przycisk odtwarzania obok "tools", aby rozpocząć ten moduł, lub uruchom wszystkie moduły jednocześnie.

Tak wygląda Spring Boot Dashboard w VS Code:

<img src="../../../translated_images/pl/dashboard.9b519b1a1bc1b30a.webp" alt="Panel kontrolny Spring Boot" width="400"/>

*Panel kontrolny Spring Boot w VS Code — uruchamiaj, zatrzymuj i monitoruj wszystkie moduły z jednego miejsca*

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

Oba skrypty automatycznie ładują zmienne środowiskowe z pliku `.env` w katalogu głównym i zbudują pliki JAR, jeśli te nie istnieją.

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

Otwórz w przeglądarce http://localhost:8084.

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

Aplikacja zapewnia interfejs webowy, w którym możesz wchodzić w interakcje z agentem AI, który ma dostęp do narzędzi pogodowych i do konwersji temperatur. Tak wygląda ten interfejs — zawiera szybką sekcję z przykładami i panel czatu do wysyłania zapytań:

<a href="images/tools-homepage.png"><img src="../../../translated_images/pl/tools-homepage.4b4cd8b2717f9621.webp" alt="Interfejs narzędzi agenta AI" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Interfejs narzędzi agenta AI - szybkie przykłady i panel czatu do interakcji z narzędziami*

### Wypróbuj proste użycie narzędzi

Zacznij od prostego zapytania: „Przelicz 100 stopni Fahrenheita na Celsjusza”. Agent rozpoznaje, że potrzebuje narzędzia do konwersji temperatury, wywołuje je z właściwymi parametrami i zwraca wynik. Zauważ, jak naturalne to jest — nie określiłeś, którego narzędzia użyć ani jak je wywołać.

### Przetestuj łączenie narzędzi

Teraz spróbuj czegoś bardziej złożonego: „Jaka jest pogoda w Seattle i przelicz ją na Fahrenheita?” Obserwuj, jak agent działa krok po kroku. Najpierw pobiera pogodę (która zwraca wartość w stopniach Celsjusza), rozpoznaje, że musi przeliczyć na Fahrenheita, wywołuje narzędzie konwersji i łączy oba wyniki w jedną odpowiedź.

### Zobacz przebieg rozmowy

Interfejs czatu utrzymuje historię rozmów, co pozwala na wielokrotne interakcje. Możesz zobaczyć wszystkie poprzednie zapytania i odpowiedzi, co ułatwia śledzenie rozmowy i zrozumienie, jak agent buduje kontekst na przestrzeni wielu wymian.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/pl/tools-conversation-demo.89f2ce9676080f59.webp" alt="Rozmowa z wieloma wywołaniami narzędzi" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Wielokrotna rozmowa pokazująca proste konwersje, wyszukiwanie pogody i łączenie narzędzi*

### Eksperymentuj z różnymi zapytaniami

Wypróbuj różne kombinacje:
- Wyszukiwanie pogody: „Jaka jest pogoda w Tokio?”
- Konwersje temperatur: „Ile to 25°C w kelwinach?”
- Zapytania łączone: „Sprawdź pogodę w Paryżu i powiedz, czy jest powyżej 20°C”

Zauważ, jak agent interpretuje język naturalny i dopasowuje go do odpowiednich wywołań narzędzi.

## Kluczowe koncepcje

### Wzorzec ReAct (Rozumowanie i Działanie)

Agent naprzemiennie rozważa (decyduje co zrobić) i działa (używa narzędzi). Ten wzorzec umożliwia autonomiczne rozwiązywanie problemów, a nie tylko odpowiadanie na polecenia.

### Opisy narzędzi są ważne

Jakość opisów narzędzi bezpośrednio wpływa na to, jak dobrze agent je wykorzystuje. Jasne, precyzyjne opisy pomagają modelowi zrozumieć, kiedy i jak wywołać każde narzędzie.

### Zarządzanie sesjami

Adnotacja `@MemoryId` umożliwia automatyczne zarządzanie pamięcią na bazie sesji. Każde ID sesji ma własną instancję `ChatMemory` zarządzaną przez bean `ChatMemoryProvider`, dzięki czemu wielu użytkowników może korzystać z agenta równocześnie bez mieszania się rozmów. Poniższy diagram pokazuje, jak wielu użytkowników jest kierowanych do izolowanych pamięci na podstawie ich identyfikatorów sesji:

<img src="../../../translated_images/pl/session-management.91ad819c6c89c400.webp" alt="Zarządzanie sesją z @MemoryId" width="800"/>

*Każde ID sesji odpowiada izolowanej historii rozmowy — użytkownicy nigdy nie widzą wiadomości innych.*

### Obsługa błędów

Narzędzia mogą zawieść — API mogą przekroczyć limit czasu, parametry mogą być nieprawidłowe, usługi zewnętrzne mogą przestać działać. Agenci produkcyjni potrzebują obsługi błędów, aby model mógł wyjaśnić problemy lub spróbować alternatyw, zamiast zawieszać całą aplikację. Gdy narzędzie wyrzuca wyjątek, LangChain4j go łapie i przekazuje komunikat o błędzie z powrotem do modelu, który może wtedy wytłumaczyć problem w języku naturalnym.

## Dostępne narzędzia

Poniższy diagram pokazuje szeroki ekosystem narzędzi, które można tworzyć. Ten moduł demonstruje narzędzia pogodowe i do konwersji temperatur, lecz ten sam wzorzec `@Tool` działa dla dowolnej metody w Javie — od zapytań do bazy danych po przetwarzanie płatności.

<img src="../../../translated_images/pl/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ekosystem narzędzi" width="800"/>

*Każda metoda Javy z adnotacją @Tool staje się dostępna dla AI — wzorzec obejmuje bazy danych, API, e-maile, operacje na plikach i więcej.*

## Kiedy używać agentów opartych na narzędziach

Nie każde zapytanie wymaga narzędzi. Decyzja zależy od tego, czy AI musi wchodzić w interakcję z systemami zewnętrznymi czy potrafi odpowiedzieć na podstawie własnej wiedzy. Poniższy przewodnik podsumowuje, kiedy narzędzia są przydatne, a kiedy zbędne:

<img src="../../../translated_images/pl/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Kiedy używać narzędzi" width="800"/>

*Szybki przewodnik — narzędzia do danych na żywo, obliczeń i działań; wiedza ogólna i zadania kreatywne nie wymagają ich.*

## Narzędzia vs RAG

Moduły 03 i 04 rozszerzają możliwości AI, ale w zasadniczo różnych aspektach. RAG daje modelowi dostęp do **wiedzy** przez wyszukiwanie dokumentów. Narzędzia dają modelowi możliwość podejmowania **działań** przez wywoływanie funkcji. Poniższy diagram porównuje oba podejścia obok siebie — od sposobu działania każdego z nich po kompromisy między nimi:

<img src="../../../translated_images/pl/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Porównanie Narzędzi i RAG" width="800"/>

*RAG pobiera informacje ze statycznych dokumentów — narzędzia wykonują działania i pobierają dane dynamiczne, w czasie rzeczywistym. Wiele systemów produkcyjnych łączy oba podejścia.*

W praktyce wiele systemów produkcyjnych łączy oba podejścia: RAG do osadzania odpowiedzi w dokumentacji oraz Narzędzia do pobierania danych na żywo lub wykonywania operacji.

## Kolejne kroki

**Następny moduł:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Nawigacja:** [← Poprzedni: Moduł 03 - RAG](../03-rag/README.md) | [Powrót do głównego](../README.md) | [Następny: Moduł 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:  
Ten dokument został przetłumaczony przy użyciu usługi tłumaczeń AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mimo że dążymy do jak największej dokładności, prosimy mieć na uwadze, że tłumaczenia automatyczne mogą zawierać błędy lub niedokładności. Oryginalny dokument w języku źródłowym należy traktować jako źródło autorytatywne. W przypadku istotnych informacji zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonane przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->