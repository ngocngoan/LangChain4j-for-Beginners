# Modul 03: RAG (Retrieval-Augmented Generation)

## Spis treści

- [Czego się nauczysz](../../../03-rag)
- [Zrozumienie RAG](../../../03-rag)
- [Wymagania wstępne](../../../03-rag)
- [Jak to działa](../../../03-rag)
  - [Przetwarzanie dokumentów](../../../03-rag)
  - [Tworzenie osadzeń](../../../03-rag)
  - [Wyszukiwanie semantyczne](../../../03-rag)
  - [Generowanie odpowiedzi](../../../03-rag)
- [Uruchomienie aplikacji](../../../03-rag)
- [Korzystanie z aplikacji](../../../03-rag)
  - [Prześlij dokument](../../../03-rag)
  - [Zadaj pytania](../../../03-rag)
  - [Sprawdź odwołania do źródeł](../../../03-rag)
  - [Eksperymentuj z pytaniami](../../../03-rag)
- [Kluczowe pojęcia](../../../03-rag)
  - [Strategia dzielenia na fragmenty](../../../03-rag)
  - [Wyniki podobieństwa](../../../03-rag)
  - [Przechowywanie w pamięci operacyjnej](../../../03-rag)
  - [Zarządzanie oknem kontekstu](../../../03-rag)
- [Kiedy RAG ma znaczenie](../../../03-rag)
- [Kolejne kroki](../../../03-rag)

## Czego się nauczysz

W poprzednich modułach nauczyłeś się, jak prowadzić rozmowy z AI i jak skutecznie strukturyzować prompty. Istnieje jednak podstawowe ograniczenie: modele językowe znają tylko to, czego nauczyły się podczas treningu. Nie potrafią odpowiadać na pytania dotyczące polityk Twojej firmy, dokumentacji projektowej ani żadnych informacji, na których nie były trenowane.

RAG (Retrieval-Augmented Generation) rozwiązuje ten problem. Zamiast próbować nauczyć model Twoich informacji (co jest kosztowne i niepraktyczne), dajesz mu możliwość przeszukiwania Twoich dokumentów. Gdy ktoś zada pytanie, system znajduje odpowiednie informacje i dołącza je do promptu. Model odpowiada wtedy bazując na tym pobranym kontekście.

Pomyśl o RAG jak o przekazaniu modelowi biblioteki referencyjnej. Kiedy zadasz pytanie, system:

1. **Zapytanie użytkownika** - Zadajesz pytanie  
2. **Osadzenie** - Zmienia Twoje pytanie w wektor  
3. **Wyszukiwanie wektorowe** - Znajduje podobne fragmenty dokumentów  
4. **Składanie kontekstu** - Dodaje odpowiednie fragmenty do promptu  
5. **Odpowiedź** - LLM generuje odpowiedź na podstawie kontekstu  

Dzięki temu odpowiedzi modelu są osadzone w Twoich rzeczywistych danych, zamiast opierać się jedynie na wiedzy z treningu lub wymyślaniu odpowiedzi.

## Zrozumienie RAG

Poniższy diagram ilustruje główną ideę: zamiast polegać tylko na danych treningowych modelu, RAG daje mu bibliotekę Twoich dokumentów do konsultacji przed wygenerowaniem każdej odpowiedzi.

<img src="../../../translated_images/pl/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

Oto jak elementy łączą się w całość. Pytanie użytkownika przechodzi przez cztery etapy — osadzanie, wyszukiwanie wektorowe, składanie kontekstu i generowanie odpowiedzi — gdzie każdy etap opiera się na poprzednim:

<img src="../../../translated_images/pl/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

W pozostałej części modułu szczegółowo przechodzimy każdy etap, z kodem, który możesz uruchomić i modyfikować.

## Wymagania wstępne

- Ukończony Modul 01 (wdrożone zasoby Azure OpenAI)  
- Plik `.env` w katalogu głównym z poświadczeniami Azure (utworzony przez `azd up` w Module 01)

> **Uwaga:** Jeśli nie ukończyłeś Modułu 01, najpierw postępuj zgodnie z instrukcjami wdrożenia tam.

## Jak to działa

### Przetwarzanie dokumentów

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Po przesłaniu dokumentu system parsuje go (PDF lub zwykły tekst), dołącza metadane takie jak nazwa pliku, a następnie dzieli go na fragmenty — mniejsze części mieszczące się wygodnie w oknie kontekstowym modelu. Fragmenty nakładają się nieznacznie, aby nie stracić kontekstu na granicach.

```java
// Analizuj przesłany plik i opakuj go w dokument LangChain4j
Document document = Document.from(content, metadata);

// Podziel na fragmenty po 300 tokenów z 30-tokenowym nakładaniem
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Poniższy diagram pokazuje, jak to wygląda wizualnie. Zauważ, że każdy fragment dzieli część tokenów z sąsiadami — nakładka 30 tokenów zapewnia, że żaden ważny kontekst nie zostaje pominięty:

<img src="../../../translated_images/pl/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) i zapytaj:  
> - "Jak LangChain4j dzieli dokumenty na fragmenty i dlaczego nakładka jest ważna?"  
> - "Jaki jest optymalny rozmiar fragmentu dla różnych typów dokumentów i dlaczego?"  
> - "Jak obsłużyć dokumenty w wielu językach lub ze specjalnym formatowaniem?"

### Tworzenie osadzeń

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Każdy fragment jest zamieniany na reprezentację numeryczną zwaną osadzeniem — to matematyczny odcisk palca, który uchwytuje znaczenie tekstu. Podobny tekst generuje podobne osadzenia.

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
  
Poniższy diagram klas pokazuje, jak łączą się komponenty LangChain4j. `OpenAiOfficialEmbeddingModel` przekształca tekst w wektory, `InMemoryEmbeddingStore` przechowuje wektory wraz z oryginalnymi danymi `TextSegment`, a `EmbeddingSearchRequest` kontroluje parametry wyszukiwania jak `maxResults` i `minScore`:

<img src="../../../translated_images/pl/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

Po zapisaniu osadzeń podobne treści naturalnie grupują się w przestrzeni wektorowej. Poniższa wizualizacja pokazuje, jak dokumenty o podobnej tematyce tworzą sąsiednie punkty, co umożliwia wyszukiwanie semantyczne:

<img src="../../../translated_images/pl/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

### Wyszukiwanie semantyczne

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kiedy zadasz pytanie, ono również zostaje zamienione na osadzenie. System porównuje osadzenie Twojego pytania z osadzeniami wszystkich fragmentów dokumentów. Znajduje te, które mają najbardziej zbliżone znaczenie — nie tylko pasujące słowa kluczowe, ale rzeczywiste podobieństwo semantyczne.

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
  
Poniższy diagram kontrastuje wyszukiwanie semantyczne z tradycyjnym wyszukiwaniem słów kluczowych. Wyszukiwanie słowa kluczowego „vehicle” pomija fragment o „samochodach i ciężarówkach”, ale wyszukiwanie semantyczne rozumie, że to to samo i zwraca go jako dobrze dopasowany wynik:

<img src="../../../translated_images/pl/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) i zapytaj:  
> - "Jak działa wyszukiwanie podobieństwa z osadzeniami i co determinuje wynik?"  
> - "Jaki próg podobieństwa powinienem stosować i jak wpływa on na wyniki?"  
> - "Jak radzić sobie z sytuacjami, gdy nie znaleziono odpowiednich dokumentów?"

### Generowanie odpowiedzi

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Najbardziej istotne fragmenty są składane w uporządkowany prompt, który zawiera wyraźne instrukcje, pobrany kontekst i pytanie użytkownika. Model czyta te konkretne fragmenty i odpowiada na podstawie tych informacji — może wykorzystać tylko to, co jest przed nim, co zapobiega halucynacjom.

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
  
Poniższy diagram pokazuje składanie promptu w akcji — najwyżej oceniane fragmenty z etapu wyszukiwania są wstrzykiwane do szablonu promptu, a `OpenAiOfficialChatModel` generuje odpowiedź osadzoną w kontekście:

<img src="../../../translated_images/pl/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

## Uruchomienie aplikacji

**Weryfikacja wdrożenia:**  

Upewnij się, że plik `.env` istnieje w katalogu głównym z poświadczeniami Azure (utworzony podczas Modułu 01):  
```bash
cat ../.env  # Powinien pokazywać AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Uruchom aplikację:**  

> **Uwaga:** Jeśli już uruchomiłeś wszystkie aplikacje za pomocą `./start-all.sh` w Module 01, ten moduł już działa na porcie 8081. Możesz pominąć poniższe polecenia startowe i przejść bezpośrednio do http://localhost:8081.

**Opcja 1: Korzystanie z Spring Boot Dashboard (zalecane dla użytkowników VS Code)**

Kontener developerski zawiera rozszerzenie Spring Boot Dashboard, które zapewnia wizualny interfejs do zarządzania wszystkimi aplikacjami Spring Boot. Znajdziesz je na pasku aktywności po lewej stronie VS Code (ikona Spring Boot).

Z poziomu Spring Boot Dashboard możesz:  
- Zobaczyć wszystkie dostępne aplikacje Spring Boot w przestrzeni roboczej  
- Uruchamiać/zatrzymywać aplikacje jednym kliknięciem  
- Przeglądać logi aplikacji w czasie rzeczywistym  
- Monitorować stan aplikacji  

Kliknij przycisk play obok „rag”, aby uruchomić ten moduł, lub uruchom wszystkie moduły jednocześnie.

<img src="../../../translated_images/pl/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

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
cd 03-rag
./start.sh
```
  
**PowerShell:**  
```powershell
cd 03-rag
.\start.ps1
```
  
Oba skrypty automatycznie ładują zmienne środowiskowe z pliku `.env` w katalogu głównym i zbudują pliki JAR, jeśli ich nie ma.

> **Uwaga:** Jeśli wolisz samodzielnie zbudować wszystkie moduły przed uruchomieniem:  
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
  
Otwórz http://localhost:8081 w przeglądarce.

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

Aplikacja udostępnia interfejs webowy do przesyłania dokumentów i zadawania pytań.

<a href="images/rag-homepage.png"><img src="../../../translated_images/pl/rag-homepage.d90eb5ce1b3caa94.webp" alt="Interfejs aplikacji RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Interfejs aplikacji RAG – prześlij dokumenty i zadawaj pytania*

### Prześlij dokument

Zacznij od przesłania dokumentu – pliki TXT są najlepsze do testów. W tym katalogu jest dostępny `sample-document.txt` zawierający informacje o funkcjach LangChain4j, implementacji RAG i najlepszych praktykach – idealny do testowania systemu.

System przetwarza Twój dokument, dzieli go na fragmenty i tworzy osadzenia dla każdego fragmentu. Dzieje się to automatycznie po przesłaniu.

### Zadaj pytania

Teraz zadaj konkretne pytania dotyczące treści dokumentu. Spróbuj czegoś faktycznego, co jest wyraźnie napisane w dokumencie. System wyszukuje odpowiednie fragmenty, dołącza je do promptu i generuje odpowiedź.

### Sprawdź odwołania do źródeł

Zauważ, że każda odpowiedź zawiera odwołania do źródeł z wynikami podobieństwa. Wartości te (od 0 do 1) pokazują, jak bardzo dany fragment był istotny dla Twojego pytania. Wyższe wyniki oznaczają lepsze dopasowania. Dzięki temu możesz zweryfikować odpowiedź względem materiału źródłowego.

<a href="images/rag-query-results.png"><img src="../../../translated_images/pl/rag-query-results.6d69fcec5397f355.webp" alt="Wyniki zapytania RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Wyniki zapytania pokazujące odpowiedź z odwołaniami do źródeł i wynikami istotności*

### Eksperymentuj z pytaniami

Wypróbuj różne typy pytań:  
- Konkretne fakty: „Jaki jest główny temat?”  
- Porównania: „Jaka jest różnica między X a Y?”  
- Podsumowania: „Podsumuj kluczowe punkty dotyczące Z”

Obserwuj, jak wyniki istotności zmieniają się w zależności od tego, jak dobrze Twoje pytanie pasuje do treści dokumentu.

## Kluczowe pojęcia

### Strategia dzielenia na fragmenty

Dokumenty są dzielone na fragmenty po 300 tokenów z nakładką 30 tokenów. To zapewnia, że każdy fragment ma wystarczająco kontekstu, by być znaczącym, a jednocześnie jest na tyle mały, by można było zamieścić wiele fragmentów w prompt.

### Wyniki podobieństwa

Każdy pobrany fragment ma wynik podobieństwa w skali od 0 do 1, który pokazuje, jak blisko odpowiada pytaniu użytkownika. Poniższy diagram wizualizuje zakresy wyników i jak system używa ich do filtrowania wyników:

<img src="../../../translated_images/pl/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

Wyniki mieszczą się w zakresie od 0 do 1:  
- 0.7-1.0: Bardzo istotne, dokładne dopasowanie  
- 0.5-0.7: Istotne, dobry kontekst  
- Poniżej 0.5: Odrzucone, zbyt małe podobieństwo  

System pobiera tylko fragmenty wyżej niż minimalny próg, aby zapewnić jakość.

### Przechowywanie w pamięci operacyjnej

Ten moduł używa przechowywania w pamięci operacyjnej dla uproszczenia. Po restarcie aplikacji przesłane dokumenty są tracone. Systemy produkcyjne korzystają z trwałych baz danych wektorowych, takich jak Qdrant lub Azure AI Search.

### Zarządzanie oknem kontekstu

Każdy model ma maksymalny rozmiar okna kontekstu. Nie można dołączyć wszystkich fragmentów z dużego dokumentu. System pobiera N najbardziej istotnych fragmentów (domyślnie 5), aby pozostać w limitach i zapewnić wystarczający kontekst do dokładnych odpowiedzi.

## Kiedy RAG ma znaczenie

RAG nie zawsze jest właściwym podejściem. Poniższy przewodnik pomoże określić, kiedy RAG dodaje wartość, a kiedy wystarczą prostsze metody — jak bezpośredni zawartość w prompt lub poleganie na wbudowanej wiedzy modelu:

<img src="../../../translated_images/pl/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

**Używaj RAG, gdy:**
- Odpowiadanie na pytania dotyczące dokumentów zastrzeżonych  
- Informacje często się zmieniają (polityki, ceny, specyfikacje)  
- Dokładność wymaga przypisania źródła  
- Treść jest zbyt obszerna, aby zmieścić się w jednym zapytaniu  
- Potrzebujesz weryfikowalnych, opartych na faktach odpowiedzi  

**Nie używaj RAG, gdy:**  
- Pytania wymagają ogólnej wiedzy, którą model już posiada  
- Potrzebne są dane w czasie rzeczywistym (RAG działa na przesłanych dokumentach)  
- Treść jest na tyle mała, że można ją bezpośrednio umieścić w zapytaniu  

## Kolejne kroki  

**Następny moduł:** [04-tools - AI Agents with Tools](../04-tools/README.md)  

---  

**Nawigacja:** [← Poprzedni: Moduł 02 - Inżynieria promptów](../02-prompt-engineering/README.md) | [Powrót do głównego](../README.md) | [Następny: Moduł 04 - Narzędzia →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:  
Niniejszy dokument został przetłumaczony za pomocą usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mimo że dokładamy starań, aby tłumaczenie było jak najbardziej poprawne, prosimy pamiętać, że automatyczne tłumaczenia mogą zawierać błędy lub nieścisłości. Za oficjalne i wiążące źródło należy uważać oryginalny dokument w jego rodzimym języku. W przypadku informacji o istotnym znaczeniu zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikłe z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->