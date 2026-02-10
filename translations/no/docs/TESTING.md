# Testing LangChain4j-applikasjoner

## Innholdsfortegnelse

- [Rask start](../../../docs)
- [Hva testene dekker](../../../docs)
- [Kjøre testene](../../../docs)
- [Kjøre tester i VS Code](../../../docs)
- [Testmønstre](../../../docs)
- [Testfilosofi](../../../docs)
- [Neste steg](../../../docs)

Denne veiledningen leder deg gjennom testene som viser hvordan du tester AI-applikasjoner uten å kreve API-nøkler eller eksterne tjenester.

## Rask start

Kjør alle tester med en enkelt kommando:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/no/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Vellykket testkjøring som viser at alle tester passerer uten feil*

## Hva testene dekker

Dette kurset fokuserer på **enhetstester** som kjøres lokalt. Hver test demonstrerer et spesifikt LangChain4j-konsept isolert.

<img src="../../../translated_images/no/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Testpyramide som viser balansen mellom enhetstester (raske, isolerte), integrasjonstester (virkelige komponenter) og ende-til-ende tester. Denne treningen dekker enhetstesting.*

| Modul | Tester | Fokus | Nøkkelfiler |
|--------|-------|-------|-----------|
| **00 - Rask start** | 6 | Promptmaler og variabelsubstitusjon | `SimpleQuickStartTest.java` |
| **01 - Introduksjon** | 8 | Samtaleminne og tilstandsbasert chat | `SimpleConversationTest.java` |
| **02 - Prompt Engineering** | 12 | GPT-5.2 mønstre, iver-nivåer, strukturert output | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Dokumentinntak, embeddinger, likhetssøk | `DocumentServiceTest.java` |
| **04 - Verktøy** | 12 | Funksjonskall og verktøykjeding | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol med Stdio-transport | `SimpleMcpTest.java` |

## Kjøre testene

**Kjør alle tester fra rot:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Kjør tester for en bestemt modul:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Eller fra rot
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Eller fra rot
mvn --% test -pl 01-introduction
```

**Kjør en enkelt testklasse:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Kjør en spesifikk testmetode:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#børOpprettholdeSamtalehistorikk
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#børOpprettholdeSamtalehistorikk
```

## Kjøre tester i VS Code

Hvis du bruker Visual Studio Code, tilbyr Test Explorer et grafisk grensesnitt for å kjøre og feilsøke tester.

<img src="../../../translated_images/no/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer viser testtreet med alle Java-testklasser og individuelle testmetoder*

**For å kjøre tester i VS Code:**

1. Åpne Test Explorer ved å klikke på kolbe-ikonet i Aktivitetslinjen
2. Utvid testtreet for å se alle moduler og testklasser
3. Klikk på spill-knappen ved siden av en test for å kjøre den individuelt
4. Klikk "Run All Tests" for å kjøre hele settet
5. Høyreklikk på en test og velg "Debug Test" for å sette breakpoint og gå gjennom koden

Test Explorer viser grønne hake for beståtte tester og gir detaljerte feilmeldinger når tester feiler.

## Testmønstre

### Mønster 1: Teste promptmaler

Det enkleste mønsteret tester promptmaler uten å kalle noe AI-modell. Du verifiserer at variabelsubstitusjon fungerer riktig og at promptene er formatert som forventet.

<img src="../../../translated_images/no/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Testing av promptmaler som viser flyt for variabelsubstitusjon: mal med plassholdere → verdier brukt → formatert output verifisert*

```java
@Test
@DisplayName("Should format prompt template with variables")
void testPromptTemplateFormatting() {
    PromptTemplate template = PromptTemplate.from(
        "Best time to visit {{destination}} for {{activity}}?"
    );
    
    Prompt prompt = template.apply(Map.of(
        "destination", "Paris",
        "activity", "sightseeing"
    ));
    
    assertThat(prompt.text()).isEqualTo("Best time to visit Paris for sightseeing?");
}
```

Denne testen ligger i `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Kjør den:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testMalformatteringForOppfordring
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testPromptMalingsformatering
```

### Mønster 2: Mocking av språkmodeller

Når du tester samtalelogikk, bruk Mockito til å lage falske modeller som returnerer forhåndsbestemte svar. Dette gjør testene raske, gratis og deterministiske.

<img src="../../../translated_images/no/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Sammenligning som viser hvorfor mocks foretrekkes for testing: de er raske, gratis, deterministiske og krever ingen API-nøkler*

```java
@ExtendWith(MockitoExtension.class)
class SimpleConversationTest {
    
    private ConversationService conversationService;
    
    @Mock
    private OpenAiOfficialChatModel mockChatModel;
    
    @BeforeEach
    void setUp() {
        ChatResponse mockResponse = ChatResponse.builder()
            .aiMessage(AiMessage.from("This is a test response"))
            .build();
        when(mockChatModel.chat(anyList())).thenReturn(mockResponse);
        
        conversationService = new ConversationService(mockChatModel);
    }
    
    @Test
    void shouldMaintainConversationHistory() {
        String conversationId = conversationService.startConversation();
        
        ChatResponse mockResponse1 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 1"))
            .build();
        ChatResponse mockResponse2 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 2"))
            .build();
        ChatResponse mockResponse3 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 3"))
            .build();
        
        when(mockChatModel.chat(anyList()))
            .thenReturn(mockResponse1)
            .thenReturn(mockResponse2)
            .thenReturn(mockResponse3);

        conversationService.chat(conversationId, "First message");
        conversationService.chat(conversationId, "Second message");
        conversationService.chat(conversationId, "Third message");

        List<ChatMessage> history = conversationService.getHistory(conversationId);
        assertThat(history).hasSize(6); // 3 bruker + 3 AI-meldinger
    }
}
```

Dette mønsteret finnes i `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mocken sikrer konsistent atferd slik at du kan verifisere at minnehåndtering fungerer riktig.

### Mønster 3: Teste samtaleisolasjon

Samtaleminne må holde flere brukere adskilt. Denne testen verifiserer at samtaler ikke blander kontekster.

<img src="../../../translated_images/no/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Testing av samtaleisolasjon som viser separate minnelagre for forskjellige brukere for å hindre kontekstblanding*

```java
@Test
void shouldIsolateConversationsByid() {
    String conv1 = conversationService.startConversation();
    String conv2 = conversationService.startConversation();
    
    ChatResponse mockResponse = ChatResponse.builder()
        .aiMessage(AiMessage.from("Response"))
        .build();
    when(mockChatModel.chat(anyList())).thenReturn(mockResponse);

    conversationService.chat(conv1, "Message for conversation 1");
    conversationService.chat(conv2, "Message for conversation 2");

    List<ChatMessage> history1 = conversationService.getHistory(conv1);
    List<ChatMessage> history2 = conversationService.getHistory(conv2);
    
    assertThat(history1).hasSize(2);
    assertThat(history2).hasSize(2);
}
```

Hver samtale opprettholder sin egen uavhengige historikk. I produksjonssystemer er denne isolasjonen kritisk for multi-bruker applikasjoner.

### Mønster 4: Teste verktøy uavhengig

Verktøy er funksjoner som AI kan kalle. Test dem direkte for å sikre at de fungerer riktig uavhengig av AI-beslutninger.

<img src="../../../translated_images/no/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Testing av verktøy uavhengig som viser mock-kjøring av verktøy uten AI-kall for å verifisere forretningslogikk*

```java
@Test
void shouldConvertCelsiusToFahrenheit() {
    TemperatureTool tempTool = new TemperatureTool();
    String result = tempTool.celsiusToFahrenheit(25.0);
    assertThat(result).containsPattern("77[.,]0°F");
}

@Test
void shouldDemonstrateToolChaining() {
    WeatherTool weatherTool = new WeatherTool();
    TemperatureTool tempTool = new TemperatureTool();

    String weatherResult = weatherTool.getCurrentWeather("Seattle");
    assertThat(weatherResult).containsPattern("\\d+°C");

    String conversionResult = tempTool.celsiusToFahrenheit(22.0);
    assertThat(conversionResult).containsPattern("71[.,]6°F");
}
```

Disse testene fra `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` validerer verktøyets logikk uten AI-involvering. Eksempelet på kjeding viser hvordan output fra ett verktøy mates inn som input til et annet.

### Mønster 5: In-Memory RAG-testing

RAG-systemer krever tradisjonelt vektordatabaser og embedding-tjenester. In-memory-mønsteret lar deg teste hele pipelinen uten eksterne avhengigheter.

<img src="../../../translated_images/no/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*In-memory RAG-testing arbeidsflyt som viser dokumentparsing, embedding-lagring og likhetssøk uten krav om database*

```java
@Test
void testProcessTextDocument() {
    String content = "This is a test document.\nIt has multiple lines.";
    InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    
    DocumentService.ProcessedDocument result = 
        documentService.processDocument(inputStream, "test.txt");

    assertNotNull(result);
    assertTrue(result.segments().size() > 0);
    assertEquals("test.txt", result.segments().get(0).metadata().getString("filename"));
}
```

Denne testen fra `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` oppretter et dokument i minnet og verifiserer chunking og metadata-håndtering.

### Mønster 6: MCP integrasjonstesting

MCP-modulen tester Model Context Protocol-integrasjon ved hjelp av stdio-transport. Disse testene verifiserer at applikasjonen din kan starte og kommunisere med MCP-servere som underprosesser.

Testene i `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` validerer MCP-klientatferd.

**Kjør dem:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Testfilosofi

Test koden din, ikke AI-en. Testene dine skal validere koden du skriver ved å sjekke hvordan prompts bygges opp, hvordan minnet håndteres, og hvordan verktøy utføres. AI-responser varierer og bør ikke være en del av testpåstander. Spør deg selv om promptmalen din korrekt erstatter variabler, ikke om AI gir riktig svar.

Bruk mocks for språkmodeller. De er eksterne avhengigheter som er trege, dyre og ikke-deterministiske. Mocking gjør testene raske med millisekunder i stedet for sekunder, gratis uten API-kostnader, og deterministiske med samme resultat hver gang.

Hold testene uavhengige. Hver test skal sette opp sine egne data, ikke stole på andre tester, og rydde opp etter seg. Tester skal passere uansett kjørerekkefølge.

Test kanttilfeller utover den glade vei. Prøv tomme inputs, veldig store inputs, spesialtegn, ugyldige parametere, og grenseverdier. Disse avslører ofte feil som normal bruk ikke eksponerer.

Bruk beskrivende navn. Sammenlign `shouldMaintainConversationHistoryAcrossMultipleMessages()` med `test1()`. Det første forteller deg nøyaktig hva som testes, noe som gjør feilsøking mye enklere.

## Neste steg

Nå som du forstår testmønstrene, dykk dypere inn i hver modul:

- **[00 - Rask start](../00-quick-start/README.md)** - Start med grunnleggende promptmaler
- **[01 - Introduksjon](../01-introduction/README.md)** - Lær samtaleminnehåndtering
- **[02 - Prompt Engineering](../02-prompt-engineering/README.md)** - Mestre GPT-5.2 prompting-mønstre
- **[03 - RAG](../03-rag/README.md)** - Bygg retrieval-augmented generation-systemer
- **[04 - Verktøy](../04-tools/README.md)** - Implementer funksjonskall og verktøykjeder
- **[05 - MCP](../05-mcp/README.md)** - Integrer Model Context Protocol

Hver moduls README gir detaljerte forklaringer på konseptene som testes her.

---

**Navigasjon:** [← Tilbake til hoved](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vær oppmerksom på at automatiserte oversettelser kan inneholde feil eller unøyaktigheter. Det opprinnelige dokumentet på morsmålet bør betraktes som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for misforståelser eller feiltolkninger som følge av bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->