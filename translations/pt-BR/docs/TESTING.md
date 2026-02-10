# Testando Aplicações LangChain4j

## Índice

- [Início Rápido](../../../docs)
- [O Que os Testes Cobrêm](../../../docs)
- [Executando os Testes](../../../docs)
- [Executando Testes no VS Code](../../../docs)
- [Padrões de Teste](../../../docs)
- [Filosofia de Testes](../../../docs)
- [Próximos Passos](../../../docs)

Este guia apresenta os testes que demonstram como testar aplicações de IA sem precisar de chaves de API ou serviços externos.

## Início Rápido

Execute todos os testes com um único comando:

**Bash:**
```bash
mvn test
```
  
**PowerShell:**
```powershell
mvn --% test
```
  
<img src="../../../translated_images/pt-BR/test-results.ea5c98d8f3642043.webp" alt="Resultados de Testes Bem-Sucedidos" width="800"/>

*Execução bem-sucedida dos testes mostrando todos os testes passando sem falhas*

## O Que os Testes Cobrêm

Este curso foca em **testes unitários** que rodam localmente. Cada teste demonstra um conceito específico do LangChain4j isoladamente.

<img src="../../../translated_images/pt-BR/testing-pyramid.2dd1079a0481e53e.webp" alt="Pirâmide de Testes" width="800"/>

*Pirâmide de testes mostrando o equilíbrio entre testes unitários (rápidos, isolados), testes de integração (componentes reais) e testes ponta a ponta. Este treinamento cobre testes unitários.*

| Módulo | Testes | Foco | Arquivos Principais |
|--------|--------|-------|---------------------|
| **00 - Início Rápido** | 6 | Modelos de prompt e substituição de variáveis | `SimpleQuickStartTest.java` |
| **01 - Introdução** | 8 | Memória de conversa e chat com estado | `SimpleConversationTest.java` |
| **02 - Engenharia de Prompt** | 12 | Padrões GPT-5.2, níveis de prontidão, saída estruturada | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Ingestão de documentos, embeddings, busca por similaridade | `DocumentServiceTest.java` |
| **04 - Ferramentas** | 12 | Chamada de função e encadeamento de ferramentas | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Protocolo de Contexto de Modelo com transporte Stdio | `SimpleMcpTest.java` |

## Executando os Testes

**Execute todos os testes a partir da raiz:**

**Bash:**
```bash
mvn test
```
  
**PowerShell:**
```powershell
mvn --% test
```
  
**Execute testes para um módulo específico:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Ou a partir da raiz
mvn test -pl 01-introduction
```
  
**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Ou a partir da raiz
mvn --% test -pl 01-introduction
```
  
**Execute uma única classe de teste:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```
  
**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```
  
**Execute um método de teste específico:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#deveManterHistoricoDaConversa
```
  
**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#deveManterHistoricoDeConversa
```
  
## Executando Testes no VS Code

Se você usa Visual Studio Code, o Test Explorer oferece uma interface gráfica para executar e depurar os testes.

<img src="../../../translated_images/pt-BR/vscode-testing.f02dd5917289dced.webp" alt="Explorador de Testes do VS Code" width="800"/>

*Explorador de Testes do VS Code mostrando árvore de testes com todas as classes Java e métodos de teste individuais*

**Para executar testes no VS Code:**

1. Abra o Test Explorer clicando no ícone do béquer na Barra de Atividades  
2. Expanda a árvore de testes para ver todos os módulos e classes de teste  
3. Clique no botão de play ao lado de qualquer teste para executá-lo individualmente  
4. Clique em "Run All Tests" para executar toda a suíte  
5. Clique com o botão direito em qualquer teste e selecione "Debug Test" para definir pontos de parada e depurar passo a passo  

O Test Explorer exibe marcas verdes para testes aprovados e fornece mensagens detalhadas de falha quando um teste não passa.

## Padrões de Teste

### Padrão 1: Testando Modelos de Prompt

O padrão mais simples testa templates de prompt sem chamar nenhum modelo de IA. Você verifica se a substituição de variáveis funciona corretamente e se os prompts são formatados como esperado.

<img src="../../../translated_images/pt-BR/prompt-template-testing.b902758ddccc8dee.webp" alt="Testando Modelos de Prompt" width="800"/>

*Testando templates de prompt mostrando o fluxo de substituição de variáveis: template com espaços reservados → valores aplicados → saída formatada verificada*

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
  
Este teste está em `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Execute-o:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testarFormataçãoDoModeloDePrompt
```
  
**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testarFormataçãoDePromptTemplate
```
  
### Padrão 2: Simulando Modelos de Linguagem

Ao testar a lógica de conversação, use Mockito para criar modelos falsos que retornam respostas pré-determinadas. Isso torna os testes rápidos, gratuitos e determinísticos.

<img src="../../../translated_images/pt-BR/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Comparação Mock vs API Real" width="800"/>

*Comparação mostrando por que mocks são preferidos para testes: são rápidos, gratuitos, determinísticos e não requerem chaves de API*

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
        assertThat(history).hasSize(6); // 3 mensagens do usuário + 3 mensagens de IA
    }
}
```
  
Este padrão aparece em `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. O mock garante comportamento consistente para que você possa verificar se o gerenciamento de memória funciona corretamente.

### Padrão 3: Testando Isolamento de Conversas

A memória da conversa deve manter múltiplos usuários separados. Este teste verifica que as conversas não misturam contextos.

<img src="../../../translated_images/pt-BR/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Isolamento de Conversa" width="800"/>

*Testando isolamento de conversas mostrando memórias separadas para diferentes usuários para evitar mistura de contexto*

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
  
Cada conversa mantém seu próprio histórico independente. Em sistemas de produção, esse isolamento é crítico para aplicações multiusuário.

### Padrão 4: Testando Ferramentas de Forma Independente

Ferramentas são funções que a IA pode chamar. Teste-as diretamente para garantir que funcionem corretamente independentemente das decisões da IA.

<img src="../../../translated_images/pt-BR/tools-testing.3e1706817b0b3924.webp" alt="Testando Ferramentas" width="800"/>

*Testando ferramentas independentemente, mostrando execução de ferramenta mock sem chamadas de IA para verificar lógica de negócio*

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
  
Estes testes do `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` validam a lógica das ferramentas sem envolvimento da IA. O exemplo de encadeamento mostra como a saída de uma ferramenta alimenta a entrada da outra.

### Padrão 5: Testando RAG na Memória

Sistemas RAG tradicionalmente requerem bancos de dados vetoriais e serviços de embedding. O padrão em memória permite testar toda a pipeline sem dependências externas.

<img src="../../../translated_images/pt-BR/rag-testing.ee7541b1e23934b1.webp" alt="Testando RAG na Memória" width="800"/>

*Fluxo de trabalho de testes RAG em memória mostrando parsing de documentos, armazenamento de embeddings e busca por similaridade sem precisar de banco de dados*

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
  
Este teste do `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` cria um documento na memória e verifica o chunking e manipulação de metadados.

### Padrão 6: Testes de Integração MCP

O módulo MCP testa a integração do Protocolo de Contexto de Modelo usando transporte stdio. Estes testes verificam que sua aplicação pode iniciar e se comunicar com servidores MCP como subprocessos.

Os testes em `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` validam o comportamento do cliente MCP.

**Execute-os:**

**Bash:**
```bash
cd 05-mcp && mvn test
```
  
**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```
  
## Filosofia de Testes

Teste seu código, não a IA. Seus testes devem validar o código que você escreve, verificando como os prompts são construídos, como a memória é gerenciada e como as ferramentas são executadas. Respostas da IA variam e não devem fazer parte das asserções dos testes. Pergunte-se se seu template de prompt substitui variáveis corretamente, não se a IA dá a resposta certa.

Use mocks para modelos de linguagem. São dependências externas que são lentas, caras e não determinísticas. Simular torna os testes rápidos — com milissegundos em vez de segundos, gratuitos — sem custo de API, e determinísticos — com o mesmo resultado todas as vezes.

Mantenha os testes independentes. Cada teste deve configurar seus próprios dados, não depender de outros testes e limpar o que usou. Os testes devem passar independentemente da ordem de execução.

Teste casos de borda além do caminho feliz. Experimente entradas vazias, muito grandes, caracteres especiais, parâmetros inválidos e condições de limite. Frequentemente eles revelam bugs que o uso normal não expõe.

Use nomes descritivos. Compare `shouldMaintainConversationHistoryAcrossMultipleMessages()` com `test1()`. O primeiro diz exatamente o que está sendo testado, facilitando muito a depuração de falhas.

## Próximos Passos

Agora que você entende os padrões de teste, aprofunde-se em cada módulo:

- **[00 - Início Rápido](../00-quick-start/README.md)** - Comece com o básico de templates de prompt  
- **[01 - Introdução](../01-introduction/README.md)** - Aprenda sobre gerenciamento de memória de conversa  
- **[02 - Engenharia de Prompt](../02-prompt-engineering/README.md)** - Domine padrões de prompting GPT-5.2  
- **[03 - RAG](../03-rag/README.md)** - Construa sistemas de geração aumentada por recuperação  
- **[04 - Ferramentas](../04-tools/README.md)** - Implemente chamadas de funções e encadeamento de ferramentas  
- **[05 - MCP](../05-mcp/README.md)** - Integre o Protocolo de Contexto de Modelo  

O README de cada módulo fornece explicações detalhadas dos conceitos testados aqui.

---

**Navegação:** [← Voltar ao Principal](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido usando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela precisão, por favor, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, é recomendada a tradução profissional feita por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->