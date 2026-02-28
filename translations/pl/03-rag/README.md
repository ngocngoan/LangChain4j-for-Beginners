# Moduł 03: RAG (Retrieval-Augmented Generation)

## Spis treści

- [Przegląd wideo](../../../03-rag)
- [Czego się nauczysz](../../../03-rag)
- [Wymagania wstępne](../../../03-rag)
- [Zrozumienie RAG](../../../03-rag)
  - [Które podejście RAG jest używane w tym samouczku?](../../../03-rag)
- [Jak to działa](../../../03-rag)
  - [Przetwarzanie dokumentów](../../../03-rag)
  - [Tworzenie osadzeń (embeddingów)](../../../03-rag)
  - [Wyszukiwanie semantyczne](../../../03-rag)
  - [Generowanie odpowiedzi](../../../03-rag)
- [Uruchom aplikację](../../../03-rag)
- [Używanie aplikacji](../../../03-rag)
  - [Prześlij dokument](../../../03-rag)
  - [Zadawaj pytania](../../../03-rag)
  - [Sprawdź odniesienia źródłowe](../../../03-rag)
  - [Eksperymentuj z pytaniami](../../../03-rag)
- [Kluczowe pojęcia](../../../03-rag)
  - [Strategia podziału na fragmenty](../../../03-rag)
  - [Wskaźniki podobieństwa](../../../03-rag)
  - [Przechowywanie w pamięci](../../../03-rag)
  - [Zarządzanie oknem kontekstu](../../../03-rag)
- [Kiedy RAG ma znaczenie](../../../03-rag)
- [Kolejne kroki](../../../03-rag)

## Przegląd wideo

Obejrzyj tę sesję na żywo, która wyjaśnia, jak rozpocząć pracę z tym modułem: [RAG z LangChain4j - sesja na żywo](https://www.youtube.com/watch?v=_olq75ZH_eY)

## Czego się nauczysz

W poprzednich modułach nauczyłeś się, jak prowadzić rozmowy z AI i skutecznie konstruować zapytania (prompt). Jednak istnieje fundamentalne ograniczenie: modele językowe znają tylko to, czego nauczyły się podczas treningu. Nie potrafią odpowiedzieć na pytania dotyczące polityk Twojej firmy, dokumentacji projektowej ani żadnych informacji, których nie objęto treningiem.

RAG (Retrieval-Augmented Generation) rozwiązuje ten problem. Zamiast uczyć model Twoich informacji (co jest kosztowne i niepraktyczne), dajesz mu możliwość przeszukiwania Twoich dokumentów. Kiedy ktoś zadaje pytanie, system znajduje odpowiednie informacje i dołącza je do zapytania. Model wtedy odpowiada na podstawie tego pobranego kontekstu.

Pomyśl o RAG jak o dawaniu modelowi biblioteki odniesień. Gdy zadasz pytanie, system:

1. **Zapytanie użytkownika** – zadajesz pytanie
2. **Osadzanie** – zamienia pytanie na wektor
3. **Wyszukiwanie wektorowe** – znajduje podobne fragmenty dokumentu
4. **Złożenie kontekstu** – dodaje odpowiednie fragmenty do zapytania
5. **Odpowiedź** – LLM generuje odpowiedź na podstawie kontekstu

Dzięki temu odpowiedzi modelu opierają się na Twoich faktycznych danych, zamiast na wiedzy ze szkolenia lub wymyślaniu odpowiedzi.

## Wymagania wstępne

- Ukończony [Moduł 00 - Szybki start](../00-quick-start/README.md) (dla przytoczonego przykładu Easy RAG)
- Ukończony [Moduł 01 - Wprowadzenie](../01-introduction/README.md) (rozmieszczone zasoby Azure OpenAI, w tym model osadzania `text-embedding-3-small`)
- Plik `.env` w katalogu głównym z danymi dostępowymi do Azure (utworzony przez `azd up` w Moduł 01)

> **Uwaga:** Jeśli nie ukończyłeś Modułu 01, najpierw postępuj zgodnie z instrukcjami tam zawartymi. Polecenie `azd up` wdraża zarówno model chat GPT, jak i model osadzania używany przez ten moduł.

## Zrozumienie RAG

Poniższy diagram ilustruje podstawową ideę: zamiast polegać wyłącznie na danych treningowych modelu, RAG daje mu dostęp do biblioteki Twoich dokumentów, które może konsultować przed wygenerowaniem każdej odpowiedzi.

<img src="../../../translated_images/pl/what-is-rag.1f9005d44b07f2d8.webp" alt="Czym jest RAG" width="800"/>

*Ten diagram pokazuje różnicę pomiędzy standardowym LLM (który zgaduje na podstawie danych treningowych) a LLM z RAG (który najpierw konsultuje Twoje dokumenty).*

Tak wyglądają połączenia pomiędzy etapami end-to-end. Pytanie użytkownika przechodzi przez cztery etapy — osadzanie, wyszukiwanie wektorowe, łączenie kontekstu i generowanie odpowiedzi — każdy buduje się na poprzednim:

<img src="../../../translated_images/pl/rag-architecture.ccb53b71a6ce407f.webp" alt="Architektura RAG" width="800"/>

*Ten diagram pokazuje całą ścieżkę RAG — zapytanie użytkownika przechodzi przez osadzanie, wyszukiwanie wektorowe, łączenie kontekstu oraz generowanie odpowiedzi.*

Reszta modułu przeprowadza cię przez każdy etap szczegółowo, z kodem, który możesz uruchomić i modyfikować.

### Które podejście RAG jest używane w tym samouczku?

LangChain4j oferuje trzy sposoby implementacji RAG, każdy o innym poziomie abstrakcji. Poniższy diagram pokazuje je obok siebie:

<img src="../../../translated_images/pl/rag-approaches.5b97fdcc626f1447.webp" alt="Trzy podejścia RAG w LangChain4j" width="800"/>

*Ten diagram porównuje trzy podejścia RAG w LangChain4j — Easy, Native i Advanced — pokazując ich kluczowe komponenty i kiedy ich używać.*

| Podejście | Co robi | Wada |
|---|---|---|
| **Easy RAG** | Automatycznie podłącza wszystko przez `AiServices` i `ContentRetriever`. Adnotujesz interfejs, dołączasz retriever, a LangChain4j automatycznie obsługuje osadzanie, wyszukiwanie i składanie zapytania za kulisami. | Minimalna ilość kodu, ale nie widzisz, co dzieje się na każdym etapie. |
| **Native RAG** | Sam wywołujesz model osadzania, przeszukujesz magazyn, budujesz zapytanie i generujesz odpowiedź — krok po kroku, jawnie. | Więcej kodu, ale każdy etap jest widoczny i można go zmieniać. |
| **Advanced RAG** | Wykorzystuje framework `RetrievalAugmentor` z wymiennymi transformatorami zapytań, routerami, re-rankingiem i wtryskiwaczami treści do produkcyjnych pipeline’ów. | Maksymalna elastyczność, ale zdecydowanie większa złożoność. |

**Ten samouczek używa podejścia Native.** Każdy etap pipeline’u RAG — osadzanie zapytania, wyszukiwanie wektorowe, składanie kontekstu i generowanie odpowiedzi — jest wyraźnie napisany w [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). To celowe: jako materiał edukacyjny ważniejsze jest, byś zobaczył i zrozumiał każdy etap, niż by kod był maksymalnie skrócony. Gdy już zrozumiesz, jak te elementy razem działają, możesz przejść do Easy RAG dla szybkich prototypów lub Advanced RAG na systemy produkcyjne.

> **💡 Już widziałeś Easy RAG w działaniu?** Moduł [Szybki start](../00-quick-start/README.md) zawiera przykład Pytań i Odpowiedzi na Dokument ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) używający Easy RAG — LangChain4j automatycznie obsługuje osadzanie, wyszukiwanie i składanie zapytania. Ten moduł robi krok dalej, rozbijając ten pipeline, abyś mógł zobaczyć i kontrolować każdy etap samodzielnie.

<img src="../../../translated_images/pl/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Pipeline Easy RAG - LangChain4j" width="800"/>

*Ten diagram pokazuje pipeline Easy RAG z `SimpleReaderDemo.java`. Porównaj to z podejściem Native użytym w tym module: Easy RAG ukrywa osadzanie, wyszukiwanie i składanie zapytania za `AiServices` i `ContentRetriever` — ładujesz dokument, podłączasz retriever i otrzymujesz odpowiedzi. Podejście Native w tym module rozbija ten pipeline, abyś wywoływał każdy etap (osadzanie, szukanie, tworzenie kontekstu, generowanie) sam, dając pełną widoczność i kontrolę.*

## Jak to działa

Pipeline RAG w tym module dzieli się na cztery etapy wykonywane kolejno za każdym razem, gdy użytkownik zada pytanie. Najpierw przesłany dokument jest **parsowany i dzielony na fragmenty** łatwe do obsłużenia. Te fragmenty są następnie konwertowane na **wektorowe osadzenia** i przechowywane, aby można je było matematycznie porównywać. Gdy nadejdzie zapytanie, system wykonuje **wyszukiwanie semantyczne** aby znaleźć najbardziej odpowiednie fragmenty, a następnie przekazuje je jako kontekst do LLM do **generowania odpowiedzi**. Poniższe sekcje pokazują każdy etap z faktycznym kodem i diagramami. Przyjrzyjmy się pierwszemu krokowi.

### Przetwarzanie dokumentów

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Gdy przesyłasz dokument, system go parsuje (PDF lub czysty tekst), dołącza metadane takie jak nazwa pliku, a następnie dzieli go na fragmenty — mniejsze kawałki mieszczące się wygodnie w oknie kontekstu modelu. Fragmenty te nakładają się trochę, aby nie utracić kontekstu na granicach.

```java
// Przetwórz przesłany plik i zapakuj go do dokumentu LangChain4j
Document document = Document.from(content, metadata);

// Podziel na części o 300 tokenach z 30-tokenowym nakładaniem
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Poniższy diagram pokazuje to wizualnie. Zwróć uwagę, że każdy fragment dzieli część tokenów z sąsiadami — 30-tokenowe nakładanie zapewnia, że żaden ważny kontekst nie zginie między fragmentami:

<img src="../../../translated_images/pl/document-chunking.a5df1dd1383431ed.webp" alt="Podział dokumentu na fragmenty" width="800"/>

*Ten diagram pokazuje dokument dzielony na fragmenty po 300 tokenów z 30-tokenowym nakładaniem się, co zachowuje kontekst na granicach fragmentów.*

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) i zapytaj:
> - "Jak LangChain4j dzieli dokumenty na fragmenty i dlaczego nakładanie tokenów jest ważne?"
> - "Jaki jest optymalny rozmiar fragmentów dla różnych typów dokumentów i dlaczego?"
> - "Jak obsługiwać dokumenty w wielu językach lub ze specjalnym formatowaniem?"

### Tworzenie osadzeń (embeddingów)

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Każdy fragment jest konwertowany na reprezentację numeryczną zwaną osadzeniem — w skrócie tłumaczeniem znaczenia na liczby. Model osadzania nie jest „inteligentny” jak model czatowy; nie potrafi wykonywać poleceń, rozumować ani odpowiadać na pytania. Jego zadaniem jest mapowanie tekstu do przestrzeni matematycznej, gdzie podobne znaczenia leżą blisko siebie — „samochód” blisko „auto”, „polityka zwrotów” blisko „zwrot pieniędzy”. Można porównać model czatowy do osoby, z którą rozmawiasz, a model osadzania do świetnego systemu archiwizacji.

<img src="../../../translated_images/pl/embedding-model-concept.90760790c336a705.webp" alt="Koncepcja modelu osadzeń" width="800"/>

*Ten diagram pokazuje, jak model osadzania zamienia tekst na wektory numeryczne, umieszczając podobne znaczenia — jak "samochód" i "auto" — blisko siebie w przestrzeni wektorów.*

```java
@Bean
public EmbeddingModel embeddingModel() {
    return OpenAiOfficialEmbeddingModel.builder()
        .baseUrl(azureOpenAiEndpoint)
        .apiKey(azureOpenAiKey)
        .modelName(azureEmbeddingDeploymentName)
        .build();
}

EmbeddingStore<TextSegment> embeddingStore = 
    new InMemoryEmbeddingStore<>();
```

Diagram klas poniżej pokazuje dwa oddzielne przepływy w pipeline RAG i klasy LangChain4j, które je implementują. **Przepływ przetwarzania** (działa raz podczas przesyłania) dzieli dokument, osadza fragmenty i przechowuje je przez `.addAll()`. **Przepływ zapytania** (działa za każdym razem, gdy użytkownik zadaje pytanie) osadza pytanie, wyszukuje w magazynie przez `.search()` i przekazuje dopasowany kontekst do modelu czatu. Oba przepływy łączą się na wspólnym interfejsie `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/pl/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="Klasy RAG w LangChain4j" width="800"/>

*Ten diagram pokazuje dwa przepływy w pipeline RAG — przetwarzanie i zapytanie — i jak łączą się przez wspólny EmbeddingStore.*

Gdy osadzenia są przechowywane, podobne treści naturalnie grupują się razem w przestrzeni wektorowej. Wizualizacja poniżej pokazuje, jak dokumenty dotyczące powiązanych tematów tworzą skupiska, co umożliwia wyszukiwanie semantyczne:

<img src="../../../translated_images/pl/vector-embeddings.2ef7bdddac79a327.webp" alt="Przestrzeń wektorowych osadzeń" width="800"/>

*Ta wizualizacja pokazuje, jak powiązane dokumenty klastrowane są razem w 3D przestrzeni wektorowej, z tematami takimi jak Dokumentacja techniczna, Zasady biznesowe i FAQ tworzącymi oddzielne grupy.*

Gdy użytkownik wyszukuje, system wykonuje cztery kroki: raz osadza dokumenty, przy każdym wyszukiwaniu osadza zapytanie, porównuje wektor zapytania ze wszystkimi wektorami w magazynie używając podobieństwa cosinusowego i zwraca top-K najwyżej ocenionych fragmentów. Poniższy diagram pokazuje każdy krok i zaangażowane klasy LangChain4j:

<img src="../../../translated_images/pl/embedding-search-steps.f54c907b3c5b4332.webp" alt="Kroki wyszukiwania osadzeń" width="800"/>

*Ten diagram pokazuje czterostopniowy proces wyszukiwania osadzeń: osadzanie dokumentów, osadzanie zapytania, porównywanie wektorów za pomocą podobieństwa cosinusowego i zwracanie najlepszych wyników.*

### Wyszukiwanie semantyczne

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kiedy zadasz pytanie, Twoje pytanie także jest osadzane. System porównuje osadzenie twojego pytania ze wszystkimi osadzeniami fragmentów dokumentów. Znajduje te fragmenty, które mają najbardziej podobne znaczenie — nie tylko pasujące słowa kluczowe, ale rzeczywistą podobność semantyczną.

```java
Embedding queryEmbedding = embeddingModel.embed(question).content();

EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
    .queryEmbedding(queryEmbedding)
    .maxResults(5)
    .minScore(0.5)
    .build();

EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);
List<EmbeddingMatch<TextSegment>> matches = searchResult.matches();

for (EmbeddingMatch<TextSegment> match : matches) {
    String relevantText = match.embedded().text();
    double score = match.score();
}
```

Poniższy diagram kontrastuje wyszukiwanie semantyczne z tradycyjnym wyszukiwaniem słów kluczowych. Wyszukiwanie słowa „pojazd” nie znajduje fragmentu mówiącego o „samochodach i ciężarówkach”, ale wyszukiwanie semantyczne rozumie, że to to samo i zwraca ten fragment jako wysoko oceniony:

<img src="../../../translated_images/pl/semantic-search.6b790f21c86b849d.webp" alt="Wyszukiwanie semantyczne" width="800"/>

*Ten diagram porównuje wyszukiwanie oparte na słowach kluczowych z wyszukiwaniem semantycznym, pokazując jak to drugie zwraca treści koncepcyjnie powiązane mimo różnic w słowach kluczowych.*

Pod spodem podobieństwo mierzone jest przy pomocy podobieństwa cosinusowego — w skrócie pytając „czy te dwie strzałki wskazują w tym samym kierunku?” Dwa fragmenty mogą używać całkiem innych słów, ale jeśli znaczą to samo, ich wektory wskazują w tę samą stronę i uzyskują wynik bliski 1,0:

<img src="../../../translated_images/pl/cosine-similarity.9baeaf3fc3336abb.webp" alt="Podobieństwo cosinusowe" width="800"/>

*Ten diagram ilustruje podobieństwo cosinusowe jako kąt między wektorami osadzeń — bardziej zgrane wektory osiągają wynik bliższy 1,0, co oznacza wyższe podobieństwo semantyczne.*
> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) i zapytaj:
> - "Jak działa wyszukiwanie podobieństwa z użyciem embeddingów i co decyduje o wyniku?"
> - "Jakiego progu podobieństwa powinienem użyć i jak wpływa on na wyniki?"
> - "Jak radzić sobie z przypadkami, gdy nie znaleziono odpowiednich dokumentów?"

### Generowanie odpowiedzi

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Najbardziej istotne fragmenty są złożone w uporządkowany prompt, który zawiera wyraźne instrukcje, pobrany kontekst i pytanie użytkownika. Model czyta te konkretne fragmenty i odpowiada na ich podstawie — może używać tylko tego, co ma przed sobą, co zapobiega halucynacjom.

```java
String context = matches.stream()
    .map(match -> match.embedded().text())
    .collect(Collectors.joining("\n\n"));

String prompt = String.format("""
    Answer the question based on the following context.
    If the answer cannot be found in the context, say so.

    Context:
    %s

    Question: %s

    Answer:""", context, request.question());

String answer = chatModel.chat(prompt);
```

Diagram poniżej pokazuje działanie tego składania — najwyżej ocenione fragmenty z kroku wyszukiwania są wstrzykiwane do szablonu promptu, a `OpenAiOfficialChatModel` generuje ugruntowaną odpowiedź:

<img src="../../../translated_images/pl/context-assembly.7e6dd60c31f95978.webp" alt="Złożenie kontekstu" width="800"/>

*Ten diagram pokazuje, jak najwyżej ocenione fragmenty są składane w uporządkowany prompt, pozwalając modelowi wygenerować ugruntowaną odpowiedź na podstawie Twoich danych.*

## Uruchomienie aplikacji

**Weryfikacja wdrożenia:**

Upewnij się, że plik `.env` znajduje się w katalogu głównym z poświadczeniami Azure (utworzonym podczas Modułu 01):

**Bash:**
```bash
cat ../.env  # Powinno pokazywać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Powinno pokazywać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Uruchom aplikację:**

> **Uwaga:** Jeśli już uruchomiłeś wszystkie aplikacje używając `./start-all.sh` z Modułu 01, ten moduł działa już na porcie 8081. Możesz pominąć poniższe polecenia uruchomienia i przejść bezpośrednio do http://localhost:8081.

**Opcja 1: Korzystanie z Spring Boot Dashboard (zalecane dla użytkowników VS Code)**

Dev container zawiera rozszerzenie Spring Boot Dashboard, które oferuje graficzny interfejs do zarządzania wszystkimi aplikacjami Spring Boot. Znajdziesz je na pasku aktywności po lewej stronie VS Code (ikona Spring Boot).

Z poziomu Spring Boot Dashboard możesz:
- Zobaczyć wszystkie dostępne aplikacje Spring Boot w workspace
- Uruchamiać/zatrzymywać aplikacje jednym kliknięciem
- Przeglądać logi aplikacji w czasie rzeczywistym
- Monitorować status aplikacji

Wystarczy kliknąć przycisk play obok "rag", aby uruchomić ten moduł lub uruchomić wszystkie moduły naraz.

<img src="../../../translated_images/pl/dashboard.fbe6e28bf4267ffe.webp" alt="Panel Spring Boot" width="400"/>

*Ten zrzut ekranu pokazuje Spring Boot Dashboard w VS Code, gdzie możesz wizualnie uruchamiać, zatrzymywać i monitorować aplikacje.*

**Opcja 2: Korzystanie ze skryptów shellowych**

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
cd 03-rag
./start.sh
```

**PowerShell:**
```powershell
cd 03-rag
.\start.ps1
```

Oba skrypty automatycznie ładują zmienne środowiskowe z pliku `.env` w katalogu głównym i zbudują pliki JAR, jeśli ich nie ma.

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

Otwórz w przeglądarce http://localhost:8081.

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

Aplikacja udostępnia interfejs webowy do przesyłania dokumentów i zadawania pytań.

<a href="images/rag-homepage.png"><img src="../../../translated_images/pl/rag-homepage.d90eb5ce1b3caa94.webp" alt="Interfejs aplikacji RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ten zrzut ekranu pokazuje interfejs aplikacji RAG, gdzie przesyłasz dokumenty i zadajesz pytania.*

### Prześlij dokument

Zacznij od przesłania dokumentu — do testów najlepiej działają pliki TXT. W tym katalogu jest plik `sample-document.txt`, który zawiera informacje o funkcjach LangChain4j, implementacji RAG i najlepszych praktykach — idealny do testowania systemu.

System przetwarza Twój dokument, dzieli go na fragmenty i tworzy embeddingi dla każdego fragmentu. Dzieje się to automatycznie po przesłaniu.

### Zadawaj pytania

Teraz zadaj konkretne pytania dotyczące treści dokumentu. Spróbuj czegoś faktograficznego, co jest wyraźnie stwierdzone w dokumencie. System wyszukuje odpowiednie fragmenty, włącza je do promptu i generuje odpowiedź.

### Sprawdź źródła

Zauważ, że każda odpowiedź zawiera odwołania do źródeł z ocenami podobieństwa. Oceny te (od 0 do 1) pokazują, jak istotny był dany fragment względem Twojego pytania. Wyższe oceny oznaczają lepsze dopasowanie. Dzięki temu możesz zweryfikować odpowiedź z materiałem źródłowym.

<a href="images/rag-query-results.png"><img src="../../../translated_images/pl/rag-query-results.6d69fcec5397f355.webp" alt="Wyniki zapytania RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ten zrzut ekranu pokazuje wyniki zapytania z wygenerowaną odpowiedzią, odwołaniami do źródeł i ocenami trafności dla każdego pobranego fragmentu.*

### Eksperymentuj z pytaniami

Wypróbuj różne typy pytań:
- Konkretne fakty: "Jaki jest główny temat?"
- Porównania: "Jaka jest różnica między X a Y?"
- Streszczenia: "Podsumuj kluczowe punkty dotyczące Z"

Obserwuj, jak zmieniają się oceny trafności w zależności od tego, jak dobrze Twoje pytanie pasuje do treści dokumentu.

## Kluczowe pojęcia

### Strategia dzielenia na fragmenty

Dokumenty dzielone są na fragmenty po 300 tokenów z 30-tokenowym nakładaniem. To zapewnia równowagę - każdy fragment ma wystarczająco dużo kontekstu, by być znaczący, a jednocześnie jest na tyle mały, by można było uwzględnić wiele fragmentów w prompt.

### Oceny podobieństwa

Każdy pobrany fragment ma ocenę podobieństwa w zakresie od 0 do 1, która wskazuje, jak bardzo odpowiada pytaniu użytkownika. Poniższy diagram wizualizuje zakresy ocen i sposób, w jaki system używa ich do filtrowania wyników:

<img src="../../../translated_images/pl/similarity-scores.b0716aa911abf7f0.webp" alt="Oceny podobieństwa" width="800"/>

*Ten diagram pokazuje zakres ocen od 0 do 1, z minimalnym progiem 0,5, który filtruje nieistotne fragmenty.*

Oceny mieszczą się w zakresie od 0 do 1:
- 0,7-1,0: Bardzo trafne, dokładne dopasowanie
- 0,5-0,7: Trafne, dobry kontekst
- Poniżej 0,5: Odfiltrowane, zbyt odmienne

System pobiera tylko fragmenty powyżej minimalnego progu, aby zapewnić jakość.

Embeddingi działają dobrze, gdy znaczenie grupuje się wyraźnie, ale mają swoje słabe punkty. Diagram poniżej pokazuje typowe tryby awarii — fragmenty zbyt duże dają rozmyte wektory, fragmenty zbyt małe nie mają kontekstu, niejednoznaczne terminy wskazują na wiele klastrów, a dokładne wyszukiwania (ID, numery części) w ogóle nie działają z embeddingami:

<img src="../../../translated_images/pl/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Tryby awarii embeddingów" width="800"/>

*Ten diagram pokazuje typowe tryby awarii embeddingów: zbyt duże fragmenty, zbyt małe fragmenty, niejednoznaczne terminy wskazujące na wiele klastrów oraz wyszukiwania dokładnego dopasowania, takie jak ID.*

### Przechowywanie w pamięci

Ten moduł używa przechowywania w pamięci dla uproszczenia. Po restarcie aplikacji przesłane dokumenty są tracone. Systemy produkcyjne korzystają z trwałych baz wektorowych, takich jak Qdrant czy Azure AI Search.

### Zarządzanie oknem kontekstu

Każdy model ma maksymalne okno kontekstu. Nie możesz uwzględnić wszystkich fragmentów z dużego dokumentu. System pobiera top N najtrafniejszych fragmentów (domyślnie 5), by pozostać w limitach, zapewniając wystarczająco dużo kontekstu dla dokładnych odpowiedzi.

## Kiedy warto używać RAG

RAG nie zawsze jest najlepszym rozwiązaniem. Poniższy przewodnik pomaga zdecydować, kiedy RAG wprowadza wartość, a kiedy prostsze metody — jak włączenie treści bezpośrednio do promptu czy poleganie na wbudowanej wiedzy modelu — są wystarczające:

<img src="../../../translated_images/pl/when-to-use-rag.1016223f6fea26bc.webp" alt="Kiedy używać RAG" width="800"/>

*Ten diagram pokazuje przewodnik decyzyjny, kiedy RAG dodaje wartość, a kiedy wystarczają prostsze podejścia.*

**Używaj RAG, gdy:**
- Odpowiadasz na pytania dotyczące poufnych dokumentów
- Informacje często się zmieniają (polityki, ceny, specyfikacje)
- Dokładność wymaga podania źródła
- Treść jest zbyt obszerna, by zmieścić się w pojedynczym promptie
- Potrzebujesz weryfikowalnych, ugruntowanych odpowiedzi

**Nie używaj RAG, gdy:**
- Pytania wymagają ogólnej wiedzy, którą model już posiada
- Potrzebne są dane w czasie rzeczywistym (RAG działa na przesłanych dokumentach)
- Treść jest na tyle mała, że można ją włączyć bezpośrednio do promptów

## Kolejne kroki

**Następny moduł:** [04-tools - AI agenty z narzędziami](../04-tools/README.md)

---

**Nawigacja:** [← Poprzedni: Moduł 02 - Inżynieria promptów](../02-prompt-engineering/README.md) | [Powrót do głównego](../README.md) | [Następny: Moduł 04 - Narzędzia →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:
Niniejszy dokument został przetłumaczony za pomocą usługi tłumaczeń AI [Co-op Translator](https://github.com/Azure/co-op-translator). Chociaż dążymy do dokładności, prosimy pamiętać, że tłumaczenia automatyczne mogą zawierać błędy lub nieścisłości. Oryginalny dokument w języku źródłowym powinien być traktowany jako źródło autorytatywne. W przypadku informacji o krytycznym znaczeniu zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z użycia tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->