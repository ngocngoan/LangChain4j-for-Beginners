# Moduł 03: RAG (Retrieval-Augmented Generation)

## Spis treści

- [Przewodnik wideo](../../../03-rag)
- [Czego się nauczysz](../../../03-rag)
- [Wymagania wstępne](../../../03-rag)
- [Zrozumienie RAG](../../../03-rag)
  - [Którego podejścia RAG używa ten samouczek?](../../../03-rag)
- [Jak to działa](../../../03-rag)
  - [Przetwarzanie dokumentów](../../../03-rag)
  - [Tworzenie embeddingów](../../../03-rag)
  - [Wyszukiwanie semantyczne](../../../03-rag)
  - [Generowanie odpowiedzi](../../../03-rag)
- [Uruchom aplikację](../../../03-rag)
- [Korzystanie z aplikacji](../../../03-rag)
  - [Prześlij dokument](../../../03-rag)
  - [Zadawaj pytania](../../../03-rag)
  - [Sprawdź odniesienia źródłowe](../../../03-rag)
  - [Eksperymentuj z pytaniami](../../../03-rag)
- [Kluczowe koncepcje](../../../03-rag)
  - [Strategia dzielenia na fragmenty](../../../03-rag)
  - [Wyniki podobieństwa](../../../03-rag)
  - [Przechowywanie w pamięci](../../../03-rag)
  - [Zarządzanie oknem kontekstu](../../../03-rag)
- [Kiedy RAG ma znaczenie](../../../03-rag)
- [Kolejne kroki](../../../03-rag)

## Przewodnik wideo

Obejrzyj tę sesję na żywo, która wyjaśnia, jak rozpocząć pracę z tym modułem:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## Czego się nauczysz

W poprzednich modułach nauczyłeś się, jak prowadzić rozmowy z AI i jak skutecznie strukturyzować swoje zapytania. Ale istnieje podstawowe ograniczenie: modele językowe wiedzą tylko to, czego nauczyły się podczas treningu. Nie potrafią odpowiedzieć na pytania dotyczące polityk Twojej firmy, dokumentacji projektu ani żadnych informacji, których nie uwzględniono podczas treningu.

RAG (Retrieval-Augmented Generation) rozwiązuje ten problem. Zamiast próbować nauczyć model Twoich informacji (co jest kosztowne i niepraktyczne), dajesz mu możliwość przeszukiwania dokumentów. Kiedy ktoś zada pytanie, system znajdzie odpowiednie informacje i dołączy je do zapytania. Model odpowie w oparciu o ten pobrany kontekst.

Pomyśl o RAG jak o podarowaniu modelowi biblioteki referencyjnej. Kiedy zadasz pytanie, system:

1. **Zapytanie użytkownika** - Ty zadajesz pytanie  
2. **Embedding** - Zamienia twoje pytanie na wektor  
3. **Wyszukiwanie wektorowe** - Znajduje podobne fragmenty dokumentów  
4. **Tworzenie kontekstu** - Dodaje odpowiednie fragmenty do zapytania  
5. **Odpowiedź** - LLM generuje odpowiedź na podstawie kontekstu

Dzięki temu odpowiedzi modelu opierają się na faktycznych danych, a nie na wiedzy zdobytej podczas treningu lub na domysłach.

## Wymagania wstępne

- Ukończony [Moduł 00 - Szybki Start](../00-quick-start/README.md) (dla przykładu Easy RAG, na który jest odniesienie w tym module)  
- Ukończony [Moduł 01 - Wprowadzenie](../01-introduction/README.md) (wdrożone zasoby Azure OpenAI, w tym model embeddingowy `text-embedding-3-small`)  
- Plik `.env` w katalogu głównym z poświadczeniami Azure (utworzony przez `azd up` w Module 01)  

> **Uwaga:** Jeśli nie ukończyłeś Modułu 01, najpierw wykonaj instrukcje wdrożenia tam zawarte. Polecenie `azd up` wdraża zarówno model czatu GPT, jak i model embeddingowy używany w tym module.

## Zrozumienie RAG

Poniższy diagram ilustruje podstawową koncepcję: zamiast polegać wyłącznie na danych treningowych modelu, RAG dostarcza mu bibliotekę referencyjną Twoich dokumentów, do której może się odwołać przed wygenerowaniem każdej odpowiedzi.

<img src="../../../translated_images/pl/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Ten diagram pokazuje różnicę między standardowym LLM (który zgaduje na podstawie danych treningowych) a LLM z rozszerzeniem RAG (który najpierw odwołuje się do Twoich dokumentów).*

Oto jak elementy łączą się w całość. Pytanie użytkownika przechodzi przez cztery etapy — embedding, wyszukiwanie wektorowe, składanie kontekstu oraz generowanie odpowiedzi — z których każdy opiera się na poprzednim:

<img src="../../../translated_images/pl/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Ten diagram pokazuje end-to-end pipeline RAG — zapytanie użytkownika przechodzi przez embedding, wyszukiwanie wektorowe, składanie kontekstu i generowanie odpowiedzi.*

W dalszej części tego modułu omówimy każdy etap szczegółowo, z kodem, który możesz uruchomić i modyfikować.

### Którego podejścia RAG używa ten samouczek?

LangChain4j oferuje trzy sposoby implementacji RAG, każdy na innym poziomie abstrakcji. Poniższy diagram porównuje je obok siebie:

<img src="../../../translated_images/pl/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Ten diagram porównuje trzy podejścia RAG w LangChain4j — Easy, Native i Advanced — pokazując ich kluczowe komponenty i kiedy stosować każde z nich.*

| Podejście | Co robi | Wady i zalety |
|---|---|---|
| **Easy RAG** | Łączy wszystko automatycznie przez `AiServices` i `ContentRetriever`. Adnotujesz interfejs, dołączasz retriever, a LangChain4j obsługuje embedding, wyszukiwanie i składanie promptu w tle. | Minimalna ilość kodu, ale nie widzisz, co dzieje się na każdym etapie. |
| **Native RAG** | Sam wywołujesz model embeddingowy, przeszukujesz magazyn, budujesz prompt i generujesz odpowiedź — krok po kroku, jawnie. | Więcej kodu, ale każdy etap jest widoczny i modyfikowalny. |
| **Advanced RAG** | Używa frameworka `RetrievalAugmentor` z wtyczkami przekształceń zapytań, trasowania, ponownego oceniania i wstrzykiwania zawartości dla produkcyjnych pipeline’ów. | Maksymalna elastyczność, ale znacznie większa złożoność. |

**Ten samouczek używa podejścia Native.** Każdy etap pipeline’u RAG — tworzenie embeddinga zapytania, wyszukiwanie w sklepie wektorowym, składanie kontekstu i generowanie odpowiedzi — jest jawnie zapisany w [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). To celowe: jako materiał do nauki ważniejsze jest, byś widział i rozumiał każdy etap, niż by kod był maksymalnie zminimalizowany. Gdy poczujesz się swobodnie z tym, jak elementy się łączą, możesz przejść do Easy RAG dla szybkich prototypów lub Advanced RAG dla systemów produkcyjnych.

> **💡 Znasz już Easy RAG z praktyki?** Moduł [Quick Start](../00-quick-start/README.md) zawiera przykład Document Q&A ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)), który używa podejścia Easy RAG — LangChain4j automatycznie obsługuje embedding, wyszukiwanie i składanie promptu. Ten moduł idzie krok dalej, rozbijając ten pipeline, abyś mógł zobaczyć i kontrolować każdy etap samodzielnie.

Poniższy diagram pokazuje pipeline Easy RAG z tego przykładu Quick Start. Zwróć uwagę, jak `AiServices` i `EmbeddingStoreContentRetriever` ukrywają całą złożoność — ładujesz dokument, dołączasz retriever i dostajesz odpowiedzi. Podejście Native w tym module rozbija te ukryte kroki:

<img src="../../../translated_images/pl/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Ten diagram pokazuje pipeline Easy RAG z `SimpleReaderDemo.java`. Porównaj go z podejściem Native używanym w tym module: Easy RAG ukrywa embedding, wyszukiwanie i składanie promptu za `AiServices` i `ContentRetriever` — ładujesz dokument, dołączasz retriever i dostajesz odpowiedzi. Podejście Native w tym module rozbija ten pipeline, więc wywołujesz każdy etap (embedding, wyszukiwanie, składanie kontekstu, generowanie) samodzielnie, z pełną kontrolą i widocznością.*

## Jak to działa

Pipeline RAG w tym module dzieli się na cztery etapy, które wykonują się kolejno za każdym razem, gdy użytkownik zada pytanie. Najpierw przesłany dokument jest **parsowany i dzielony na fragmenty** o rozmiarze do zarządzania. Te fragmenty są następnie konwertowane na **wektorowe embeddingi** i przechowywane, by można było je matematycznie porównywać. Gdy przychodzi zapytanie, system wykonuje **wyszukiwanie semantyczne**, by znaleźć najbardziej trafne fragmenty, a na końcu przekazuje je jako kontekst do LLM w celu **generowania odpowiedzi**. Poniższe sekcje przechodzą przez każdy etap z rzeczywistym kodem i diagramami. Zaczynamy od pierwszego kroku.

### Przetwarzanie dokumentów

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Kiedy przesyłasz dokument, system go parsuje (PDF lub tekst zwykły), dołącza metadane takie jak nazwa pliku, a następnie dzieli go na fragmenty — mniejsze części mieszczące się komfortowo w oknie kontekstowym modelu. Te fragmenty nakładają się częściowo, aby nie utracić kontekstu na granicach.

```java
// Przetwórz przesłany plik i zapakuj go w dokument LangChain4j
Document document = Document.from(content, metadata);

// Podziel na fragmenty po 300 tokenów z nakładaniem 30 tokenów
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Poniższy diagram pokazuje, jak to działa wizualnie. Zauważ, że każdy fragment dzieli część tokenów ze swoimi sąsiadami — nakładka 30 tokenów zapewnia, że żaden ważny kontekst nie zostanie utracony:

<img src="../../../translated_images/pl/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Ten diagram pokazuje dokument dzielony na fragmenty po 300 tokenów z nakładką 30-tokenową, zachowując kontekst na granicach fragmentów.*

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) i zapytaj:
> - „Jak LangChain4j dzieli dokumenty na fragmenty i dlaczego nakładka jest ważna?”
> - „Jaki jest optymalny rozmiar fragmentu dla różnych typów dokumentów i dlaczego?”
> - „Jak radzić sobie z dokumentami w wielu językach lub ze specjalnym formatowaniem?”

### Tworzenie embeddingów

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Każdy fragment jest zamieniany na reprezentację numeryczną zwaną embeddingiem — praktycznie konwerter znaczenia na liczby. Model embeddingowy nie jest „inteligentny” jak model czatu; nie potrafi wykonywać instrukcji, rozumować ani odpowiadać na pytania. Potrafi jednak odzwierciedlić tekst w przestrzeni matematycznej, gdzie zbliżone znaczenia znajdują się blisko siebie — „samochód” blisko „automobil”, „polityka zwrotów” blisko „zwróć mi pieniądze”. Pomyśl o modelu czatu jak o osobie, z którą rozmawiasz; model embeddingowy to system archiwizacji na wysokim poziomie.

Poniższy diagram wizualizuje tę koncepcję — tekst wchodzi, wychodzą wektory liczbowe, a podobne znaczenia dają bliskie wektory:

<img src="../../../translated_images/pl/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Ten diagram pokazuje, jak model embeddingowy zamienia tekst na numeryczne wektory, umieszczając podobne znaczenia — jak „samochód” i „automobil” — blisko siebie w przestrzeni wektorowej.*

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
  
Poniższy diagram klas pokazuje dwa oddzielne przebiegi w pipeline RAG oraz klasy LangChain4j, które je implementują. **Przebieg ingestii** (wykonuje się raz przy przesłaniu) dzieli dokument, tworzy embeddingi fragmentów i przechowuje je za pomocą `.addAll()`. **Przebieg zapytania** (wykonuje się za każdym razem, gdy użytkownik pyta) tworzy embedding zapytania, przeszukuje sklep za pomocą `.search()`, i przekazuje dopasowany kontekst do modelu czatu. Oba przebiegi łączy wspólny interfejs `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/pl/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Ten diagram pokazuje dwa przebiegi pipeline RAG — ingestii i zapytania — oraz ich połączenie przez wspólny EmbeddingStore.*

Gdy embeddingi są przechowywane, podobne treści naturalnie tworzą klastry w przestrzeni wektorowej. Poniższa wizualizacja pokazuje, jak dokumenty o pokrewnych tematach tworzą pobliskie punkty, co umożliwia wyszukiwanie semantyczne:

<img src="../../../translated_images/pl/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Ta wizualizacja pokazuje, jak powiązane dokumenty grupują się w przestrzeni wektorowej 3D, z tematami takimi jak Dokumenty Techniczne, Reguły Biznesowe i FAQ tworzącymi odrębne grupy.*

Gdy użytkownik wyszukuje, system przechodzi przez cztery kroki: tworzy embedding dokumentów raz, tworzy embedding zapytania przy każdym wyszukiwaniu, porównuje wektor zapytania z wszystkimi przechowywanymi wektorami za pomocą podobieństwa cosinusowego i zwraca najwyżej oceniane top-K fragmenty. Poniższy diagram pokazuje krok po kroku ten proces i klasy LangChain4j zaangażowane w każdy z nich:

<img src="../../../translated_images/pl/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Ten diagram pokazuje czterostopniowy proces wyszukiwania embeddingów: stworzenie embeddingów dokumentów, embedding zapytania, porównanie wektorów za pomocą podobieństwa cosinusowego i zwrócenie top-K wyników.*

### Wyszukiwanie semantyczne

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kiedy zadajesz pytanie, twoje pytanie również jest zamieniane na embedding. System porównuje embedding pytania z embeddingami wszystkich fragmentów dokumentów. Znajduje fragmenty o najbardziej zbliżonym znaczeniu - nie tylko dopasowanie słów kluczowych, lecz rzeczywiste podobieństwo semantyczne.

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
  
Poniższy diagram kontrastuje wyszukiwanie semantyczne z tradycyjnym wyszukiwaniem słów kluczowych. Wyszukiwanie słowa „pojazd” nie znajdzie fragmentu o „samochodach i ciężarówkach”, ale wyszukiwanie semantyczne rozumie, że oznaczają to samo i zwraca go jako wysoko ocenione dopasowanie:

<img src="../../../translated_images/pl/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Ten diagram porównuje wyszukiwanie oparte na słowach kluczowych z wyszukiwaniem semantycznym, pokazując jak to drugie odzyskuje związane konceptualnie treści, nawet gdy dokładne słowa kluczowe się różnią.*
Pod maską, podobieństwo mierzone jest za pomocą podobieństwa kosinusowego — zasadniczo pytając „czy te dwie strzałki wskazują w tym samym kierunku?” Dwa fragmenty mogą używać zupełnie innych słów, ale jeśli znaczą to samo, ich wektory wskazują w tym samym kierunku i wynik jest bliski 1.0:

<img src="../../../translated_images/pl/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*Ten diagram ilustruje podobieństwo kosinusowe jako kąt między wektorami osadzeń — bardziej wyrównane wektory zdobywają wynik bliższy 1.0, co wskazuje na wyższe podobieństwo semantyczne.*

> **🤖 Spróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) i zapytaj:
> - „Jak działa wyszukiwanie podobieństwa przy użyciu osadzeń i co decyduje o wyniku?”
> - „Jaki próg podobieństwa powinienem użyć i jak wpływa to na wyniki?”
> - „Jak radzić sobie w sytuacjach, gdy nie znaleziono odpowiednich dokumentów?”

### Generowanie odpowiedzi

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Najbardziej relewantne fragmenty są składane w strukturalny prompt, który zawiera wyraźne instrukcje, pobrany kontekst oraz pytanie użytkownika. Model czyta te konkretne fragmenty i odpowiada na ich podstawie — może używać tylko tego, co ma przed sobą, co zapobiega „halucynacjom”.

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

Poniższy diagram pokazuje tę składankę w akcji — najwyżej ocenione fragmenty z kroku wyszukiwania są wstrzykiwane do szablonu promptu, a `OpenAiOfficialChatModel` generuje ugruntowaną odpowiedź:

<img src="../../../translated_images/pl/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Ten diagram pokazuje, jak najwyżej ocenione fragmenty są składane w strukturalny prompt, pozwalając modelowi na wygenerowanie ugruntowanej odpowiedzi na podstawie twoich danych.*

## Uruchom aplikację

**Zweryfikuj wdrożenie:**

Upewnij się, że plik `.env` znajduje się w katalogu głównym i zawiera poświadczenia Azure (utworzone podczas Modułu 01). Uruchom to z katalogu modułu (`03-rag/`):

**Bash:**
```bash
cat ../.env  # Powinno wyświetlać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Powinno pokazywać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Uruchom aplikację:**

> **Uwaga:** Jeśli już uruchomiłeś wszystkie aplikacje używając `./start-all.sh` z katalogu głównego (zgodnie z opisem w Module 01), ten moduł działa już na porcie 8081. Możesz pominąć poniższe polecenia startowe i przejść bezpośrednio do http://localhost:8081.

**Opcja 1: Użycie Spring Boot Dashboard (zalecane dla użytkowników VS Code)**

Kontener deweloperski zawiera rozszerzenie Spring Boot Dashboard, które zapewnia wizualny interfejs do zarządzania wszystkimi aplikacjami Spring Boot. Znajdziesz je w pasku aktywności po lewej stronie VS Code (ikona Spring Boot).

Z poziomu Spring Boot Dashboard możesz:
- Zobaczyć wszystkie dostępne aplikacje Spring Boot w przestrzeni roboczej
- Uruchamiać/zatrzymywać aplikacje jednym kliknięciem
- Przeglądać logi aplikacji w czasie rzeczywistym
- Monitorować status aplikacji

Po prostu kliknij przycisk play obok „rag”, aby uruchomić ten moduł, lub uruchom wszystkie moduły naraz.

<img src="../../../translated_images/pl/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Ten zrzut ekranu pokazuje Spring Boot Dashboard w VS Code, gdzie możesz wizualnie uruchamiać, zatrzymywać i monitorować aplikacje.*

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
cd 03-rag
./start.sh
```

**PowerShell:**
```powershell
cd 03-rag
.\start.ps1
```

Oba skrypty automatycznie ładują zmienne środowiskowe z pliku `.env` w katalogu głównym i zbudują JAR-y, jeśli nie istnieją.

> **Uwaga:** Jeśli wolisz najpierw ręcznie zbudować wszystkie moduły przed startem:
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

<a href="images/rag-homepage.png"><img src="../../../translated_images/pl/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ten zrzut ekranu pokazuje interfejs aplikacji RAG, gdzie możesz przesyłać dokumenty i zadawać pytania.*

### Prześlij dokument

Zacznij od przesłania dokumentu — najlepiej sprawdzają się pliki TXT do testów. W tym katalogu znajduje się plik `sample-document.txt` zawierający informacje o funkcjach LangChain4j, implementacji RAG i najlepszych praktykach — idealny do testowania systemu.

System przetwarza twój dokument, dzieli go na fragmenty i tworzy osadzenia dla każdego fragmentu. Dzieje się to automatycznie po przesłaniu.

### Zadawaj pytania

Teraz zadawaj konkretne pytania dotyczące zawartości dokumentu. Spróbuj czegoś faktycznego, co jest jasno napisane w dokumencie. System wyszukuje odpowiednie fragmenty, dołącza je do promptu i generuje odpowiedź.

### Sprawdź źródła

Zauważ, że każda odpowiedź zawiera odniesienia do źródeł z wynikami podobieństwa. Te wyniki (od 0 do 1) pokazują, jak bardzo dany fragment odpowiada twojemu pytaniu. Wyższe wyniki oznaczają lepsze dopasowanie. Pozwala to zweryfikować odpowiedź względem materiału źródłowego.

<a href="images/rag-query-results.png"><img src="../../../translated_images/pl/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ten zrzut ekranu pokazuje wyniki zapytania z wygenerowaną odpowiedzią, odniesieniami do źródeł i wynikami relewancji dla każdego pobranego fragmentu.*

### Eksperymentuj z pytaniami

Wypróbuj różne typy pytań:
- Konkretne fakty: „Jaki jest główny temat?”
- Porównania: „Jaka jest różnica między X a Y?”
- Podsumowania: „Podsumuj kluczowe punkty dotyczące Z”

Obserwuj, jak zmieniają się wyniki relewancji w zależności od tego, jak dobrze twoje pytanie pasuje do zawartości dokumentu.

## Kluczowe koncepcje

### Strategia dzielenia na fragmenty

Dokumenty są dzielone na fragmenty o 300 tokenach z 30 tokenami nakładki. To równowaga, która zapewnia, że każdy fragment ma wystarczająco kontekstu, aby być znaczący, a jednocześnie jest wystarczająco mały, aby można było zamieścić wiele fragmentów w promptcie.

### Wyniki podobieństwa

Każdy pobrany fragment ma wynik podobieństwa między 0 a 1, który wskazuje, jak bardzo pasuje do pytania użytkownika. Poniższy diagram wizualizuje zakresy wyników i jak system ich używa do filtrowania wyników:

<img src="../../../translated_images/pl/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Ten diagram pokazuje zakresy wyników od 0 do 1, z minimalnym progiem 0.5, który filtruje nieistotne fragmenty.*

Wyniki mieszczą się w zakresie od 0 do 1:
- 0.7-1.0: Wysoce relewantne, dokładne dopasowanie
- 0.5-0.7: Relewantne, dobry kontekst
- Poniżej 0.5: Odfiltrowane, zbyt różne

System pobiera tylko fragmenty powyżej minimalnego progu, aby zapewnić jakość.

Osadzenia działają dobrze, gdy znaczenie grupuje się czysto, ale mają też słabe punkty. Poniższy diagram pokazuje typowe tryby niepowodzeń — fragmenty zbyt duże dają rozmyte wektory, fragmenty zbyt małe tracą kontekst, niejednoznaczne terminy wskazują na wiele grup, a wyszukiwania dokładne (ID, numery części) w ogóle nie działają z osadzeniami:

<img src="../../../translated_images/pl/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Ten diagram pokazuje typowe tryby niepowodzenia osadzeń: zbyt duże fragmenty, zbyt małe fragmenty, niejednoznaczne terminy wskazujące na wiele grup oraz wyszukiwania dokładne, jak numery ID.*

### Pamięć w RAM

Ten moduł korzysta z pamięci ulotnej (in-memory) dla uproszczenia. Po restarcie aplikacji przesłane dokumenty są tracone. Systemy produkcyjne używają trwałych baz danych wektorowych, takich jak Qdrant lub Azure AI Search.

### Zarządzanie oknem kontekstowym

Każdy model ma maksymalne okno kontekstowe. Nie da się zamieścić każdego fragmentu z dużego dokumentu. System pobiera N najbardziej relewantnych fragmentów (domyślnie 5), aby pozostać w limitach i dostarczyć wystarczająco kontekstu do dokładnych odpowiedzi.

## Kiedy RAG ma znaczenie

RAG nie zawsze jest najlepszym rozwiązaniem. Poniższy przewodnik decyzyjny pomaga określić, kiedy RAG wnosi wartość, a kiedy prostsze podejścia — jak bezpośrednie dołączenie treści do promptu lub poleganie na wbudowanej wiedzy modelu — są wystarczające:

<img src="../../../translated_images/pl/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Ten diagram pokazuje przewodnik decyzyjny, kiedy RAG dodaje wartość, a kiedy wystarczają prostsze podejścia.*

## Kolejne kroki

**Następny modół:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Nawigacja:** [← Poprzedni: Moduł 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Powrót do głównej](../README.md) | [Następny: Moduł 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:  
Niniejszy dokument został przetłumaczony za pomocą usługi tłumaczeniowej AI [Co-op Translator](https://github.com/Azure/co-op-translator). Choć dokładamy starań, aby tłumaczenie było precyzyjne, prosimy mieć na uwadze, że automatyczne tłumaczenia mogą zawierać błędy lub nieścisłości. Oryginalny dokument w języku źródłowym należy traktować jako wersję autorytatywną. W przypadku informacji krytycznych zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->