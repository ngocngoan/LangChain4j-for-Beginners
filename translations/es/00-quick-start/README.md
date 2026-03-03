# Module 00: Inicio rápido

## Tabla de contenidos

- [Introducción](../../../00-quick-start)
- [¿Qué es LangChain4j?](../../../00-quick-start)
- [Dependencias de LangChain4j](../../../00-quick-start)
- [Requisitos previos](../../../00-quick-start)
- [Configuración](../../../00-quick-start)
  - [1. Obtén tu token de GitHub](../../../00-quick-start)
  - [2. Configura tu token](../../../00-quick-start)
- [Ejecuta los ejemplos](../../../00-quick-start)
  - [1. Chat básico](../../../00-quick-start)
  - [2. Patrones de solicitud](../../../00-quick-start)
  - [3. Llamada a funciones](../../../00-quick-start)
  - [4. Preguntas y respuestas sobre documentos (Easy RAG)](../../../00-quick-start)
  - [5. IA responsable](../../../00-quick-start)
- [Qué muestra cada ejemplo](../../../00-quick-start)
- [Próximos pasos](../../../00-quick-start)
- [Solución de problemas](../../../00-quick-start)

## Introducción

Este inicio rápido está diseñado para que puedas comenzar a trabajar con LangChain4j lo más rápido posible. Cubre lo básico absoluto para construir aplicaciones de IA con LangChain4j y GitHub Models. En los próximos módulos cambiarás a Azure OpenAI y GPT-5.2 y profundizarás en cada concepto.

## ¿Qué es LangChain4j?

LangChain4j es una biblioteca Java que simplifica la creación de aplicaciones impulsadas por IA. En lugar de lidiar con clientes HTTP y análisis de JSON, trabajas con APIs Java limpias.

La "cadena" en LangChain se refiere a encadenar múltiples componentes: podrías encadenar un prompt a un modelo, luego a un parser, o encadenar múltiples llamadas de IA donde la salida de una alimenta la entrada siguiente. Este inicio rápido se enfoca en los fundamentos antes de explorar cadenas más complejas.

<img src="../../../translated_images/es/langchain-concept.ad1fe6cf063515e1.webp" alt="Concepto de encadenamiento de LangChain4j" width="800"/>

*Encadenar componentes en LangChain4j: bloques de construcción que se conectan para crear flujos de trabajo de IA potentes*

Usaremos tres componentes principales:

**ChatModel** - La interfaz para interacciones con modelos de IA. Llama a `model.chat("prompt")` y recibe una respuesta como cadena. Usamos `OpenAiOfficialChatModel` que funciona con endpoints compatibles con OpenAI como GitHub Models.

**AiServices** - Crea interfaces de servicio AI con tipado seguro. Define métodos, anótalos con `@Tool` y LangChain4j maneja la orquestación. La IA llama automáticamente a tus métodos Java cuando es necesario.

**MessageWindowChatMemory** - Mantiene el historial de conversación. Sin esto, cada petición es independiente. Con él, la IA recuerda mensajes previos y mantiene el contexto a través de múltiples intervenciones.

<img src="../../../translated_images/es/architecture.eedc993a1c576839.webp" alt="Arquitectura de LangChain4j" width="800"/>

*Arquitectura de LangChain4j: componentes principales que trabajan juntos para potenciar tus aplicaciones de IA*

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

El módulo `langchain4j-open-ai-official` provee la clase `OpenAiOfficialChatModel` que conecta con APIs compatibles con OpenAI. GitHub Models usa el mismo formato de API, por lo que no se necesita un adaptador especial — solo apunta la URL base a `https://models.github.ai/inference`.

El módulo `langchain4j-easy-rag` provee división automática de documentos, generación de embeddings y recuperación para que puedas construir aplicaciones RAG sin configurar cada paso manualmente.

## Requisitos previos

**¿Usando el contenedor Dev?** Java y Maven ya están instalados. Solo necesitas un Token de Acceso Personal de GitHub.

**Desarrollo local:**
- Java 21+, Maven 3.9+
- Token de Acceso Personal de GitHub (instrucciones abajo)

> **Nota:** Este módulo usa `gpt-4.1-nano` de GitHub Models. No modifiques el nombre del modelo en el código — está configurado para funcionar con los modelos disponibles de GitHub.

## Configuración

### 1. Obtén tu token de GitHub

1. Ve a [Configuración de GitHub → Tokens de acceso personal](https://github.com/settings/personal-access-tokens)
2. Haz clic en "Generate new token" (Generar nuevo token)
3. Asigna un nombre descriptivo (p. ej., "Demo LangChain4j")
4. Configura la expiración (7 días recomendado)
5. En "Permisos de la cuenta", busca "Models" y ponlo en "Solo lectura"
6. Haz clic en "Generate token" (Generar token)
7. Copia y guarda tu token — no lo verás de nuevo

### 2. Configura tu token

**Opción 1: Usando VS Code (Recomendado)**

Si usas VS Code, agrega tu token al archivo `.env` en la raíz del proyecto:

Si el archivo `.env` no existe, copia `.env.example` a `.env` o crea un archivo `.env` nuevo en la raíz del proyecto.

**Ejemplo de archivo `.env`:**
```bash
# En /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Luego simplemente haz clic derecho en cualquier archivo demo (p. ej., `BasicChatDemo.java`) en el Explorador y selecciona **"Run Java"** o usa las configuraciones de ejecución desde el panel Ejecutar y Depurar.

**Opción 2: Usando Terminal**

Configura el token como variable de entorno:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Ejecuta los ejemplos

**Usando VS Code:** Simplemente haz clic derecho en cualquier archivo demo en el Explorador y selecciona **"Run Java"**, o usa las configuraciones desde el panel Ejecutar y Depurar (asegúrate de haber agregado tu token al archivo `.env` primero).

**Usando Maven:** Alternativamente, puedes ejecutar desde línea de comandos:

### 1. Chat básico

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Patrones de solicitud

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Muestra zero-shot, few-shot, cadena de pensamiento y solicitudes basadas en roles.

### 3. Llamada a funciones

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

La IA llama automáticamente a tus métodos Java cuando es necesario.

### 4. Preguntas y respuestas sobre documentos (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Haz preguntas sobre tus documentos usando Easy RAG con embedding y recuperación automáticos.

### 5. IA responsable

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Ve cómo los filtros de seguridad de IA bloquean contenido dañino.

## Qué muestra cada ejemplo

**Chat básico** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Comienza aquí para ver LangChain4j en su forma más simple. Crearás un `OpenAiOfficialChatModel`, enviarás un prompt con `.chat()` y recibirás una respuesta. Esto demuestra la base: cómo inicializar modelos con endpoints y claves API personalizadas. Una vez entiendas este patrón, todo lo demás se construye sobre él.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Prueba con [GitHub Copilot](https://github.com/features/copilot) Chat:** Abre [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) y pregunta:
> - "¿Cómo cambiaría de GitHub Models a Azure OpenAI en este código?"
> - "¿Qué otros parámetros puedo configurar en OpenAiOfficialChatModel.builder()?"
> - "¿Cómo agrego respuestas en streaming en lugar de esperar la respuesta completa?"

**Ingeniería de prompts** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Ahora que sabes cómo hablar con un modelo, exploremos qué le dices. Esta demo usa la misma configuración de modelo pero muestra cinco patrones de prompt diferentes. Prueba prompts zero-shot para instrucciones directas, few-shot que aprenden de ejemplos, cadena de pensamiento que revela pasos de razonamiento y prompts basados en roles que establecen contexto. Verás cómo el mismo modelo da resultados muy distintos según cómo plantees la petición.

La demo también muestra plantillas de prompt, que son una forma poderosa de crear prompts reutilizables con variables.
El siguiente ejemplo muestra un prompt usando la `PromptTemplate` de LangChain4j para rellenar variables. La IA responderá basado en el destino y la actividad proporcionados.

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

> **🤖 Prueba con [GitHub Copilot](https://github.com/features/copilot) Chat:** Abre [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) y pregunta:
> - "¿Cuál es la diferencia entre zero-shot y few-shot prompting, y cuándo debería usar cada uno?"
> - "¿Cómo afecta el parámetro temperature las respuestas del modelo?"
> - "¿Cuáles son algunas técnicas para prevenir ataques de inyección de prompt en producción?"
> - "¿Cómo puedo crear objetos PromptTemplate reutilizables para patrones comunes?"

**Integración de herramientas** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Aquí es donde LangChain4j se vuelve potente. Usarás `AiServices` para crear un asistente de IA capaz de llamar a tus métodos Java. Solo anota los métodos con `@Tool("descripción")` y LangChain4j hace el resto — la IA decide automáticamente cuándo usar cada herramienta según lo que el usuario pida. Esto demuestra la llamada a funciones, una técnica clave para construir IA que puede tomar acciones, no solo responder preguntas.

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

> **🤖 Prueba con [GitHub Copilot](https://github.com/features/copilot) Chat:** Abre [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) y pregunta:
> - "¿Cómo funciona la anotación @Tool y qué hace LangChain4j con ella detrás de escena?"
> - "¿Puede la IA llamar a múltiples herramientas en secuencia para resolver problemas complejos?"
> - "¿Qué pasa si una herramienta lanza una excepción - cómo debo manejar errores?"
> - "¿Cómo integraría una API real en lugar de este ejemplo calculadora?"

**Preguntas y respuestas sobre documentos (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Aquí verás RAG (generación aumentada por recuperación) usando el enfoque "Easy RAG" de LangChain4j. Se cargan documentos, se dividen y embeben automáticamente en un almacén en memoria, luego un recuperador de contenido suministra fragmentos relevantes a la IA al consultar. La IA responde basado en tus documentos, no en su conocimiento general.

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

> **🤖 Prueba con [GitHub Copilot](https://github.com/features/copilot) Chat:** Abre [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) y pregunta:
> - "¿Cómo previene RAG las alucinaciones de IA en comparación con usar los datos de entrenamiento del modelo?"
> - "¿Cuál es la diferencia entre este enfoque fácil y una tubería RAG personalizada?"
> - "¿Cómo escalaría esto para manejar múltiples documentos o bases de conocimiento más grandes?"

**IA responsable** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Construye seguridad en IA con defensa en profundidad. Esta demo muestra dos capas de protección trabajando juntas:

**Parte 1: LangChain4j Input Guardrails** - Bloquea prompts peligrosos antes de que lleguen al LLM. Crea guardrails personalizados que revisen palabras clave o patrones prohibidos. Estos corren en tu código, son rápidos y gratuitos.

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

**Parte 2: Filtros de seguridad del proveedor** - GitHub Models tiene filtros integrados que atrapan lo que tus guardrails podrían pasar por alto. Verás bloqueos duros (errores HTTP 400) por violaciones severas y rechazos suaves donde la IA se niega cortésmente.

> **🤖 Prueba con [GitHub Copilot](https://github.com/features/copilot) Chat:** Abre [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) y pregunta:
> - "¿Qué es InputGuardrail y cómo creo el mío?"
> - "¿Cuál es la diferencia entre un bloqueo duro y un rechazo suave?"
> - "¿Por qué usar guardrails y filtros del proveedor juntos?"

## Próximos pasos

**Siguiente módulo:** [01-introduction - Comenzando con LangChain4j](../01-introduction/README.md)

---

**Navegación:** [← Volver al principal](../README.md) | [Siguiente: Módulo 01 - Introducción →](../01-introduction/README.md)

---

## Solución de problemas

### Primera compilación Maven

**Problema**: El comando inicial `mvn clean compile` o `mvn package` tarda mucho (10-15 minutos)

**Causa**: Maven necesita descargar todas las dependencias del proyecto (Spring Boot, librerías LangChain4j, SDKs de Azure, etc.) en la primera compilación.

**Solución**: Este comportamiento es normal. Las compilaciones posteriores serán mucho más rápidas porque las dependencias se almacenan en caché localmente. El tiempo de descarga depende de la velocidad de tu red.

### Sintaxis del comando Maven en PowerShell

**Problema**: Los comandos Maven fallan con el error `Unknown lifecycle phase ".mainClass=..."`
**Causa**: PowerShell interpreta `=` como un operador de asignación de variable, rompiendo la sintaxis de propiedades de Maven

**Solución**: Use el operador de parada de análisis `--%` antes del comando Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

El operador `--%` indica a PowerShell que pase todos los argumentos restantes literalmente a Maven sin interpretarlos.

### Visualización de emojis en Windows PowerShell

**Problema**: Las respuestas de IA muestran caracteres basura (por ejemplo, `????` o `â??`) en lugar de emojis en PowerShell

**Causa**: La codificación predeterminada de PowerShell no soporta emojis UTF-8

**Solución**: Ejecute este comando antes de ejecutar aplicaciones Java:
```cmd
chcp 65001
```

Esto fuerza la codificación UTF-8 en la terminal. Alternativamente, use Windows Terminal que tiene mejor soporte para Unicode.

### Depuración de llamadas a la API

**Problema**: Errores de autenticación, límites de tasa o respuestas inesperadas del modelo de IA

**Solución**: Los ejemplos incluyen `.logRequests(true)` y `.logResponses(true)` para mostrar llamadas a la API en la consola. Esto ayuda a solucionar errores de autenticación, límites de tasa o respuestas inesperadas. Elimine estas banderas en producción para reducir el ruido del registro.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento ha sido traducido utilizando el servicio de traducción AI [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por la exactitud, tenga en cuenta que las traducciones automáticas pueden contener errores o imprecisiones. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda una traducción profesional realizada por humanos. No nos hacemos responsables de malentendidos o interpretaciones erróneas derivadas del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->