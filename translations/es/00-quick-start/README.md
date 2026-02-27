# Módulo 00: Inicio Rápido

## Tabla de Contenidos

- [Introducción](../../../00-quick-start)
- [¿Qué es LangChain4j?](../../../00-quick-start)
- [Dependencias de LangChain4j](../../../00-quick-start)
- [Requisitos Previos](../../../00-quick-start)
- [Configuración](../../../00-quick-start)
  - [1. Obtén tu Token de GitHub](../../../00-quick-start)
  - [2. Configura tu Token](../../../00-quick-start)
- [Ejecuta los Ejemplos](../../../00-quick-start)
  - [1. Chat Básico](../../../00-quick-start)
  - [2. Patrones de Prompt](../../../00-quick-start)
  - [3. Llamada a Funciones](../../../00-quick-start)
  - [4. Preguntas y Respuestas de Documentos (Easy RAG)](../../../00-quick-start)
  - [5. IA Responsable](../../../00-quick-start)
- [Qué Muestra Cada Ejemplo](../../../00-quick-start)
- [Próximos Pasos](../../../00-quick-start)
- [Solución de Problemas](../../../00-quick-start)

## Introducción

Este inicio rápido está diseñado para que empieces a trabajar con LangChain4j lo más pronto posible. Cubre lo absoluto básico para construir aplicaciones de IA con LangChain4j y los Modelos de GitHub. En los siguientes módulos usarás Azure OpenAI con LangChain4j para construir aplicaciones más avanzadas.

## ¿Qué es LangChain4j?

LangChain4j es una librería Java que simplifica la construcción de aplicaciones impulsadas por IA. En lugar de lidiar con clientes HTTP y el análisis de JSON, trabajas con APIs limpias en Java.

La "cadena" en LangChain se refiere a encadenar múltiples componentes; podrías encadenar un prompt a un modelo y luego a un parser, o encadenar múltiples llamadas de IA donde una salida alimenta la siguiente entrada. Este inicio rápido se enfoca en los fundamentos antes de explorar cadenas más complejas.

<img src="../../../translated_images/es/langchain-concept.ad1fe6cf063515e1.webp" alt="Concepto de Encadenamiento en LangChain4j" width="800"/>

*Encadenando componentes en LangChain4j: bloques de construcción que se conectan para crear potentes flujos de trabajo de IA*

Usaremos tres componentes clave:

**ChatModel** - La interfaz para interacciones con el modelo de IA. Llama a `model.chat("prompt")` y recibe una cadena de respuesta. Usamos `OpenAiOfficialChatModel` que funciona con endpoints compatibles con OpenAI como los Modelos de GitHub.

**AiServices** - Crea interfaces de servicio IA con tipado seguro. Define métodos, anótalos con `@Tool` y LangChain4j se encarga de la orquestación. La IA llama automáticamente tus métodos Java cuando es necesario.

**MessageWindowChatMemory** - Mantiene el historial de conversación. Sin esto, cada petición es independiente. Con esto, la IA recuerda mensajes previos y mantiene el contexto a través de múltiples turnos.

<img src="../../../translated_images/es/architecture.eedc993a1c576839.webp" alt="Arquitectura de LangChain4j" width="800"/>

*Arquitectura de LangChain4j: componentes clave trabajando juntos para potenciar tus aplicaciones de IA*

## Dependencias de LangChain4j

Este inicio rápido usa tres dependencias Maven en el [`pom.xml`](../../../00-quick-start/pom.xml):

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```
  
El módulo `langchain4j-open-ai-official` proporciona la clase `OpenAiOfficialChatModel` que se conecta a APIs compatibles con OpenAI. Los Modelos de GitHub usan el mismo formato de API, así que no se necesita un adaptador especial, solo apunta la URL base a `https://models.github.ai/inference`.

El módulo `langchain4j-easy-rag` brinda división automática de documentos, creación de embeddings y recuperación para que puedas construir aplicaciones RAG sin configurar manualmente cada paso.

## Requisitos Previos

**¿Usando el Contenedor de Desarrollo?** Java y Maven ya están instalados. Solo necesitas un Token de Acceso Personal de GitHub.

**Desarrollo Local:**
- Java 21+, Maven 3.9+
- Token de Acceso Personal de GitHub (instrucciones abajo)

> **Nota:** Este módulo usa `gpt-4.1-nano` de los Modelos de GitHub. No modifiques el nombre del modelo en el código; está configurado para funcionar con los modelos disponibles en GitHub.

## Configuración

### 1. Obtén tu Token de GitHub

1. Ve a [Configuración de GitHub → Tokens de Acceso Personal](https://github.com/settings/personal-access-tokens)
2. Haz clic en "Generar nuevo token"
3. Escribe un nombre descriptivo (p. ej., "Demo LangChain4j")
4. Establece la expiración (7 días recomendado)
5. Bajo "Permisos de cuenta", encuentra "Modelos" y ponlo en "Solo lectura"
6. Haz clic en "Generar token"
7. Copia y guarda tu token - no lo verás de nuevo

### 2. Configura tu Token

**Opción 1: Usando VS Code (Recomendado)**

Si usas VS Code, añade tu token al archivo `.env` en la raíz del proyecto:

Si el archivo `.env` no existe, copia `.env.example` a `.env` o crea un archivo `.env` nuevo en la raíz del proyecto.

**Ejemplo de archivo `.env`:**  
```bash
# En /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```
  
Después solo tienes que hacer clic derecho en cualquier archivo de demo (p. ej., `BasicChatDemo.java`) en el Explorador y seleccionar **"Run Java"** o usar las configuraciones de lanzamiento desde el panel de Ejecutar y Depurar.

**Opción 2: Usando el Terminal**

Configura el token como una variable de entorno:

**Bash:**  
```bash
export GITHUB_TOKEN=your_token_here
```
  
**PowerShell:**  
```powershell
$env:GITHUB_TOKEN=your_token_here
```
  
## Ejecuta los Ejemplos

**Usando VS Code:** Haz clic derecho en cualquier archivo demo en el Explorador y selecciona **"Run Java"**, o usa las configuraciones de lanzamiento desde el panel de Ejecutar y Depurar (asegúrate de haber añadido primero tu token al archivo `.env`).

**Usando Maven:** Alternativamente, puedes ejecutar desde la línea de comandos:

### 1. Chat Básico

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```
  
### 2. Patrones de Prompt

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```
  
Muestra prompting zero-shot, few-shot, chain-of-thought, y basado en roles.

### 3. Llamada a Funciones

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
La IA llama automáticamente a tus métodos Java cuando es necesario.

### 4. Preguntas y Respuestas de Documentos (Easy RAG)

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
Haz preguntas sobre tus documentos usando Easy RAG con embedding y recuperación automáticos.

### 5. IA Responsable

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```
  
Mira cómo los filtros de seguridad de IA bloquean contenido dañino.

## Qué Muestra Cada Ejemplo

**Chat Básico** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Comienza aquí para ver LangChain4j en su forma más simple. Crearás un `OpenAiOfficialChatModel`, enviarás un prompt con `.chat()` y recibirás una respuesta. Esto demuestra la base: cómo inicializar modelos con endpoints personalizados y llaves API. Una vez entiendas este patrón, todo lo demás se construye sobre él.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```
  
> **🤖 Prueba con el Chat de [GitHub Copilot](https://github.com/features/copilot):** Abre [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) y pregunta:  
> - "¿Cómo cambiaría de Modelos de GitHub a Azure OpenAI en este código?"  
> - "¿Qué otros parámetros puedo configurar en OpenAiOfficialChatModel.builder()?"  
> - "¿Cómo agrego respuestas en streaming en lugar de esperar la respuesta completa?"

**Ingeniería de Prompt** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Ahora que sabes cómo hablar con un modelo, exploraremos qué le dices. Esta demo usa la misma configuración de modelo pero muestra cinco patrones diferentes de prompting. Prueba zero-shot para instrucciones directas, few-shot que aprende con ejemplos, chain-of-thought que revela pasos de razonamiento, y prompts basados en roles para establecer contexto. Verás cómo el mismo modelo da resultados dramáticamente distintos según cómo formules tu petición.

La demo también muestra plantillas de prompt, una forma potente de crear prompts reutilizables con variables.  
El ejemplo abajo muestra un prompt usando `PromptTemplate` de LangChain4j para llenar variables. La IA responderá basado en el destino y la actividad proporcionados.

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
  
> **🤖 Prueba con el Chat de [GitHub Copilot](https://github.com/features/copilot):** Abre [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) y pregunta:  
> - "¿Cuál es la diferencia entre prompting zero-shot y few-shot, y cuándo debería usar cada uno?"  
> - "¿Cómo afecta el parámetro de 'temperature' las respuestas del modelo?"  
> - "¿Cuáles son algunas técnicas para evitar ataques de inyección de prompt en producción?"  
> - "¿Cómo puedo crear objetos PromptTemplate reutilizables para patrones comunes?"

**Integración de Herramientas** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Aquí es donde LangChain4j se vuelve poderoso. Usarás `AiServices` para crear un asistente de IA que puede llamar a tus métodos Java. Solo anota métodos con `@Tool("descripción")` y LangChain4j hace el resto: la IA decide automáticamente cuándo usar cada herramienta según lo que el usuario pregunte. Esto demuestra llamada a funciones, una técnica clave para crear IA que puede realizar acciones, no solo responder consultas.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```
  
> **🤖 Prueba con el Chat de [GitHub Copilot](https://github.com/features/copilot):** Abre [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) y pregunta:  
> - "¿Cómo funciona la anotación @Tool y qué hace LangChain4j con ella detrás de escena?"  
> - "¿Puede la IA llamar múltiples herramientas en secuencia para resolver problemas complejos?"  
> - "¿Qué pasa si una herramienta lanza una excepción, cómo debería manejar errores?"  
> - "¿Cómo integraría una API real en lugar de este ejemplo de calculadora?"

**Preguntas y Respuestas de Documentos (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Aquí verás RAG (generación aumentada con recuperación) usando el enfoque "Easy RAG" de LangChain4j. Los documentos se cargan, dividen y embeben automáticamente en una memoria interna, luego un recuperador de contenido provee fragmentos relevantes a la IA en tiempo de consulta. La IA responde basado en tus documentos, no en conocimiento general.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```
  
> **🤖 Prueba con el Chat de [GitHub Copilot](https://github.com/features/copilot):** Abre [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) y pregunta:  
> - "¿Cómo evita RAG las alucinaciones de IA comparado con usar los datos de entrenamiento del modelo?"  
> - "¿Cuál es la diferencia entre este enfoque fácil y un pipeline RAG personalizado?"  
> - "¿Cómo escalaría esto para manejar múltiples documentos o bases de conocimiento más grandes?"

**IA Responsable** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Construye seguridad en IA con defensa en profundidad. Esta demo muestra dos capas de protección trabajando juntos:

**Parte 1: Guardias de Entrada de LangChain4j** - Bloquea prompts peligrosos antes de que lleguen al LLM. Crea guardias personalizados que verifican palabras clave o patrones prohibidos. Estos corren en tu código, así que son rápidos y gratuitos.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```
  
**Parte 2: Filtros de Seguridad del Proveedor** - Los Modelos de GitHub tienen filtros integrados que capturan lo que tus guardias podrían pasar por alto. Verás bloqueos duros (errores HTTP 400) por violaciones graves y rechazos suaves donde la IA se niega cortésmente.

> **🤖 Prueba con el Chat de [GitHub Copilot](https://github.com/features/copilot):** Abre [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) y pregunta:  
> - "¿Qué es InputGuardrail y cómo creo el mío propio?"  
> - "¿Cuál es la diferencia entre un bloqueo duro y un rechazo suave?"  
> - "¿Por qué usar guardias y filtros del proveedor juntos?"

## Próximos Pasos

**Siguiente Módulo:** [01-introducción - Primeros pasos con LangChain4j y gpt-5 en Azure](../01-introduction/README.md)

---

**Navegación:** [← Volver al Inicio](../README.md) | [Siguiente: Módulo 01 - Introducción →](../01-introduction/README.md)

---

## Solución de Problemas

### Primera Construcción con Maven

**Problema:** La ejecución inicial de `mvn clean compile` o `mvn package` toma mucho tiempo (10-15 minutos).

**Causa:** Maven necesita descargar todas las dependencias del proyecto (Spring Boot, librerías LangChain4j, SDKs de Azure, etc.) en la primera construcción.

**Solución:** Este comportamiento es normal. Las construcciones siguientes serán mucho más rápidas porque las dependencias estarán en caché localmente. El tiempo de descarga depende de tu velocidad de red.

### Sintaxis del Comando Maven en PowerShell

**Problema:** Los comandos Maven fallan con error `Unknown lifecycle phase ".mainClass=..."`
**Causa**: PowerShell interpreta `=` como un operador de asignación de variables, rompiendo la sintaxis de las propiedades de Maven

**Solución**: Use el operador de parada de análisis `--%` antes del comando Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

El operador `--%` indica a PowerShell que pase todos los argumentos restantes literalmente a Maven sin interpretación.

### Visualización de Emoji en Windows PowerShell

**Problema**: Las respuestas de AI muestran caracteres basura (por ejemplo, `????` o `â??`) en lugar de emojis en PowerShell

**Causa**: La codificación predeterminada de PowerShell no soporta emojis UTF-8

**Solución**: Ejecute este comando antes de ejecutar aplicaciones Java:
```cmd
chcp 65001
```

Esto fuerza la codificación UTF-8 en la terminal. Alternativamente, use Windows Terminal que tiene mejor soporte para Unicode.

### Depuración de llamadas a la API

**Problema**: Errores de autenticación, límites de tasa o respuestas inesperadas del modelo AI

**Solución**: Los ejemplos incluyen `.logRequests(true)` y `.logResponses(true)` para mostrar las llamadas a la API en la consola. Esto ayuda a solucionar errores de autenticación, límites de tasa o respuestas inesperadas. Quite estas banderas en producción para reducir el ruido en los registros.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por lograr precisión, tenga en cuenta que las traducciones automatizadas pueden contener errores o imprecisiones. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda una traducción profesional realizada por un humano. No nos hacemos responsables de cualquier malentendido o interpretación errónea que surja del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->