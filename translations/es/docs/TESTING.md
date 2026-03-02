# Pruebas de Aplicaciones LangChain4j

## Tabla de Contenidos

- [Inicio Rápido](../../../docs)
- [Qué Cubren las Pruebas](../../../docs)
- [Ejecutar las Pruebas](../../../docs)
- [Ejecutar Pruebas en VS Code](../../../docs)
- [Patrones de Prueba](../../../docs)
- [Filosofía de Pruebas](../../../docs)
- [Próximos Pasos](../../../docs)

Esta guía te lleva a través de las pruebas que demuestran cómo probar aplicaciones de IA sin requerir claves API ni servicios externos.

## Inicio Rápido

Ejecuta todas las pruebas con un solo comando:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Cuando todas las pruebas pasen, deberías ver una salida como la captura de pantalla a continuación — pruebas ejecutadas con cero fallos.

<img src="../../../translated_images/es/test-results.ea5c98d8f3642043.webp" alt="Resultados Exitosos de Pruebas" width="800"/>

*Ejecución exitosa de pruebas mostrando todas las pruebas pasando sin fallos*

## Qué Cubren las Pruebas

Este curso se enfoca en **pruebas unitarias** que se ejecutan localmente. Cada prueba demuestra un concepto específico de LangChain4j de forma aislada. La pirámide de pruebas a continuación muestra dónde encajan las pruebas unitarias — forman la base rápida y confiable sobre la que se construye el resto de tu estrategia de pruebas.

<img src="../../../translated_images/es/testing-pyramid.2dd1079a0481e53e.webp" alt="Pirámide de Pruebas" width="800"/>

*Pirámide de pruebas mostrando el balance entre pruebas unitarias (rápidas, aisladas), pruebas de integración (componentes reales) y pruebas end-to-end. Esta capacitación cubre pruebas unitarias.*

| Módulo | Pruebas | Enfoque | Archivos Clave |
|--------|---------|---------|----------------|
| **00 - Inicio Rápido** | 6 | Plantillas de prompts y sustitución de variables | `SimpleQuickStartTest.java` |
| **01 - Introducción** | 8 | Memoria de conversación y chat con estado | `SimpleConversationTest.java` |
| **02 - Ingeniería de Prompts** | 12 | Patrones GPT-5.2, niveles de eagerness, salida estructurada | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Ingesta de documentos, embeddings, búsqueda por similitud | `DocumentServiceTest.java` |
| **04 - Herramientas** | 12 | Llamado de funciones y encadenamiento de herramientas | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Protocolo de Contexto de Modelo con transporte stdio | `SimpleMcpTest.java` |

## Ejecutar las Pruebas

**Ejecutar todas las pruebas desde la raíz:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Ejecutar pruebas para un módulo específico:**

**Bash:**
```bash
cd 01-introduction && mvn test
# O desde la raíz
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# O desde raíz
mvn --% test -pl 01-introduction
```

**Ejecutar una sola clase de prueba:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Ejecutar un método de prueba específico:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#deberíaMantenerHistorialDeConversación
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#deberíaMantenerHistorialDeConversación
```

## Ejecutar Pruebas en VS Code

Si usas Visual Studio Code, el Explorador de Pruebas ofrece una interfaz gráfica para ejecutar y depurar pruebas.

<img src="../../../translated_images/es/vscode-testing.f02dd5917289dced.webp" alt="Explorador de Pruebas VS Code" width="800"/>

*Explorador de Pruebas de VS Code mostrando el árbol de pruebas con todas las clases de prueba Java y métodos individuales de prueba*

**Para ejecutar pruebas en VS Code:**

1. Abre el Explorador de Pruebas haciendo clic en el ícono de matraz en la Barra de Actividades
2. Expande el árbol de pruebas para ver todos los módulos y clases de prueba
3. Haz clic en el botón de reproducir junto a cualquier prueba para ejecutarla individualmente
4. Haz clic en "Run All Tests" para ejecutar todo el conjunto
5. Haz clic derecho en cualquier prueba y selecciona "Debug Test" para establecer puntos de ruptura y depurar el código

El Explorador de Pruebas muestra marcas verdes para las pruebas que pasan y provee mensajes detallados de fallo cuando las pruebas fallan.

## Patrones de Prueba

### Patrón 1: Prueba de Plantillas de Prompt

El patrón más simple prueba plantillas de prompts sin llamar a ningún modelo de IA. Verificas que la sustitución de variables funcione correctamente y que los prompts estén formateados como se espera.

<img src="../../../translated_images/es/prompt-template-testing.b902758ddccc8dee.webp" alt="Prueba de Plantilla de Prompt" width="800"/>

*Pruebas de plantillas de prompt mostrando el flujo de sustitución de variables: plantilla con marcadores → valores aplicados → salida formateada verificada*

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

Esta prueba vive en `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Ejecutarla:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#formatoDePlantillaDeSolicitudDePrueba
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#pruebaDeFormatoDePlantillaDeSolicitud
```

### Patrón 2: Mocking de Modelos de Lenguaje

Al probar la lógica de conversación, usa Mockito para crear modelos falsos que devuelven respuestas predeterminadas. Esto hace que las pruebas sean rápidas, gratuitas y deterministas.

<img src="../../../translated_images/es/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Comparación Mock vs API Real" width="800"/>

*Comparación mostrando por qué se prefieren mocks para pruebas: son rápidos, gratuitos, deterministas y no requieren claves API*

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
        assertThat(history).hasSize(6); // 3 mensajes de usuario + 3 mensajes de IA
    }
}
```

Este patrón aparece en `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. El mock asegura un comportamiento consistente para que puedas verificar que la gestión de memoria funcione correctamente.

### Patrón 3: Prueba de Aislamiento de Conversación

La memoria de conversación debe mantener usuarios múltiples separados. Esta prueba verifica que las conversaciones no mezclen contextos.

<img src="../../../translated_images/es/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Aislamiento de Conversación" width="800"/>

*Prueba de aislamiento de conversación mostrando almacenes de memoria separados para diferentes usuarios para evitar mezcla de contexto*

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

Cada conversación mantiene su propio historial independiente. En sistemas de producción, este aislamiento es crítico para aplicaciones multiusuario.

### Patrón 4: Prueba Independiente de Herramientas

Las herramientas son funciones que la IA puede llamar. Pruébalas directamente para asegurar que funcionen correctamente independientemente de las decisiones de la IA.

<img src="../../../translated_images/es/tools-testing.3e1706817b0b3924.webp" alt="Pruebas de Herramientas" width="800"/>

*Pruebas independientes de herramientas mostrando ejecución de herramienta mock sin llamadas a IA para verificar lógica de negocio*

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

Estas pruebas de `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` validan la lógica de herramientas sin involucrar IA. El ejemplo de encadenamiento muestra cómo la salida de una herramienta alimenta la entrada de otra.

### Patrón 5: Prueba RAG en Memoria

Los sistemas RAG tradicionalmente requieren bases de datos vectoriales y servicios de embedding. El patrón en memoria te permite probar toda la tubería sin dependencias externas.

<img src="../../../translated_images/es/rag-testing.ee7541b1e23934b1.webp" alt="Prueba RAG en Memoria" width="800"/>

*Flujo de trabajo de prueba RAG en memoria mostrando análisis de documentos, almacenamiento de embeddings y búsqueda por similitud sin requerir base de datos*

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

Esta prueba de `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` crea un documento en memoria y verifica la fragmentación y manejo de metadatos.

### Patrón 6: Pruebas de Integración MCP

El módulo MCP prueba la integración del Protocolo de Contexto de Modelo usando transporte stdio. Estas pruebas verifican que tu aplicación pueda iniciar y comunicarse con servidores MCP como subprocesos.

Las pruebas en `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` validan el comportamiento del cliente MCP.

**Ejecutarlas:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Filosofía de Pruebas

Prueba tu código, no la IA. Tus pruebas deben validar el código que escribes verificando cómo se construyen los prompts, cómo se maneja la memoria y cómo se ejecutan las herramientas. Las respuestas de IA varían y no deberían formar parte de las aserciones de las pruebas. Pregúntate si tu plantilla de prompt sustituye variables correctamente, no si la IA da la respuesta correcta.

Usa mocks para los modelos de lenguaje. Son dependencias externas que son lentas, costosas y no deterministas. Mockear hace las pruebas rápidas con milisegundos en lugar de segundos, gratuitas sin costos de API y deterministas con el mismo resultado cada vez.

Mantén las pruebas independientes. Cada prueba debe configurar sus propios datos, no depender de otras pruebas y limpiarse a sí misma. Las pruebas deben pasar sin importar el orden de ejecución.

Prueba casos límite más allá del camino feliz. Prueba entradas vacías, entradas muy grandes, caracteres especiales, parámetros inválidos y condiciones de límite. Estos a menudo revelan errores que el uso normal no expone.

Usa nombres descriptivos. Compara `shouldMaintainConversationHistoryAcrossMultipleMessages()` con `test1()`. El primero te dice exactamente qué se está probando, haciendo que depurar fallos sea mucho más fácil.

## Próximos Pasos

Ahora que entiendes los patrones de prueba, profundiza en cada módulo:

- **[00 - Inicio Rápido](../00-quick-start/README.md)** - Comienza con lo básico de plantillas de prompt
- **[01 - Introducción](../01-introduction/README.md)** - Aprende gestión de memoria de conversación
- **[02 - Ingeniería de Prompts](../02-prompt-engineering/README.md)** - Domina patrones de prompting GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Construye sistemas de generación aumentada por recuperación
- **[04 - Herramientas](../04-tools/README.md)** - Implementa llamadas a funciones y cadenas de herramientas
- **[05 - MCP](../05-mcp/README.md)** - Integra Protocolo de Contexto de Modelo

Los README de cada módulo proveen explicaciones detalladas de los conceptos aquí probados.

---

**Navegación:** [← Volver al Principal](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso legal**:
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por la exactitud, tenga en cuenta que las traducciones automatizadas pueden contener errores o inexactitudes. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda la traducción profesional realizada por humanos. No nos hacemos responsables de ningún malentendido o interpretación errónea que surja del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->