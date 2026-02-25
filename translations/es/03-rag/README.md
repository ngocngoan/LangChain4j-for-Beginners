# Módulo 03: RAG (Generación con Recuperación Aumentada)

## Tabla de Contenidos

- [Lo Que Aprenderás](../../../03-rag)
- [Comprendiendo RAG](../../../03-rag)
- [Prerrequisitos](../../../03-rag)
- [Cómo Funciona](../../../03-rag)
  - [Procesamiento de Documentos](../../../03-rag)
  - [Creación de Embeddings](../../../03-rag)
  - [Búsqueda Semántica](../../../03-rag)
  - [Generación de Respuestas](../../../03-rag)
- [Ejecutar la Aplicación](../../../03-rag)
- [Usar la Aplicación](../../../03-rag)
  - [Subir un Documento](../../../03-rag)
  - [Hacer Preguntas](../../../03-rag)
  - [Revisar Referencias de Fuente](../../../03-rag)
  - [Experimentar con Preguntas](../../../03-rag)
- [Conceptos Clave](../../../03-rag)
  - [Estrategia de División en Fragmentos](../../../03-rag)
  - [Puntajes de Similitud](../../../03-rag)
  - [Almacenamiento en Memoria](../../../03-rag)
  - [Gestión de la Ventana de Contexto](../../../03-rag)
- [Cuándo es Importante RAG](../../../03-rag)
- [Próximos Pasos](../../../03-rag)

## Lo Que Aprenderás

En los módulos anteriores, aprendiste cómo tener conversaciones con IA y estructurar tus prompts de forma efectiva. Pero hay una limitación fundamental: los modelos de lenguaje solo saben lo que aprendieron durante el entrenamiento. No pueden responder preguntas sobre las políticas de tu empresa, la documentación de tu proyecto, o cualquier información con la que no fueron entrenados.

RAG (Generación con Recuperación Aumentada) resuelve este problema. En vez de intentar enseñar al modelo tu información (lo cual es costoso e impráctico), le das la capacidad de buscar a través de tus documentos. Cuando alguien hace una pregunta, el sistema encuentra información relevante y la incluye en el prompt. El modelo entonces responde basándose en ese contexto recuperado.

Piensa en RAG como darle al modelo una biblioteca de referencia. Cuando haces una pregunta, el sistema:

1. **Consulta del Usuario** - Haces una pregunta  
2. **Embedding** - Convierte tu pregunta en un vector  
3. **Búsqueda Vectorial** - Encuentra fragmentos de documentos similares  
4. **Ensamblaje de Contexto** - Añade fragmentos relevantes al prompt  
5. **Respuesta** - El LLM genera una respuesta basada en el contexto  

Esto fundamenta las respuestas del modelo en tus datos reales en lugar de depender solo del conocimiento del entrenamiento o inventarse respuestas.

## Comprendiendo RAG

El diagrama a continuación ilustra el concepto central: en lugar de depender únicamente de los datos de entrenamiento del modelo, RAG le proporciona una biblioteca de referencia con tus documentos para consultar antes de generar cada respuesta.

<img src="../../../translated_images/es/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

Aquí se muestra cómo las piezas se conectan de principio a fin. La pregunta de un usuario fluye a través de cuatro etapas — embedding, búsqueda vectorial, ensamblaje de contexto y generación de respuestas — cada una construyendo sobre la anterior:

<img src="../../../translated_images/es/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

El resto de este módulo explica cada etapa en detalle, con código que puedes ejecutar y modificar.

## Prerrequisitos

- Haber completado el Módulo 01 (recursos Azure OpenAI desplegados)  
- Archivo `.env` en el directorio raíz con credenciales de Azure (creado por `azd up` en el Módulo 01)

> **Nota:** Si no has completado el Módulo 01, primero sigue las instrucciones de despliegue ahí.

## Cómo Funciona

### Procesamiento de Documentos

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Cuando subes un documento, el sistema lo analiza (PDF o texto simple), adjunta metadatos como el nombre del archivo, y luego lo divide en fragmentos — pedazos más pequeños que caben cómodamente en la ventana de contexto del modelo. Estos fragmentos se solapan ligeramente para que no se pierda contexto en las fronteras.

```java
// Analizar el archivo subido y envolverlo en un Documento LangChain4j
Document document = Document.from(content, metadata);

// Dividir en fragmentos de 300 tokens con una superposición de 30 tokens
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
El diagrama a continuación muestra cómo funciona esto visualmente. Observa cómo cada fragmento comparte algunos tokens con sus vecinos — la superposición de 30 tokens asegura que ningún contexto importante quede fuera:

<img src="../../../translated_images/es/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

> **🤖 Prueba con [GitHub Copilot](https://github.com/features/copilot) Chat:** Abre [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) y pregunta:  
> - "¿Cómo divide LangChain4j los documentos en fragmentos y por qué es importante el solapamiento?"  
> - "¿Cuál es el tamaño óptimo de fragmento para diferentes tipos de documentos y por qué?"  
> - "¿Cómo manejo documentos en varios idiomas o con formato especial?"

### Creación de Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Cada fragmento se convierte en una representación numérica llamada embedding - básicamente una huella matemática que captura el significado del texto. Textos similares producen embeddings similares.

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
  
El diagrama de clases abajo muestra cómo se conectan estos componentes de LangChain4j. `OpenAiOfficialEmbeddingModel` convierte texto en vectores, `InMemoryEmbeddingStore` almacena los vectores junto con sus datos originales de `TextSegment`, y `EmbeddingSearchRequest` controla parámetros de recuperación como `maxResults` y `minScore`:

<img src="../../../translated_images/es/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

Una vez almacenados los embeddings, el contenido similar naturalmente se agrupa en el espacio vectorial. La visualización a continuación muestra cómo documentos sobre temas relacionados se representan como puntos cercanos, lo que permite la búsqueda semántica:

<img src="../../../translated_images/es/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

### Búsqueda Semántica

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Cuando haces una pregunta, tu pregunta también se convierte en un embedding. El sistema compara el embedding de tu pregunta con los embeddings de todos los fragmentos del documento. Encuentra los fragmentos con significados más similares - no solo coincidencias por palabras clave, sino similitud semántica real.

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
  
El diagrama abajo contrasta la búsqueda semántica con la búsqueda tradicional por palabras clave. Una búsqueda por palabra clave para "vehículo" no encuentra un fragmento sobre "autos y camiones," pero la búsqueda semántica entiende que significan lo mismo y lo devuelve como una coincidencia con alta puntuación:

<img src="../../../translated_images/es/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

> **🤖 Prueba con [GitHub Copilot](https://github.com/features/copilot) Chat:** Abre [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) y pregunta:  
> - "¿Cómo funciona la búsqueda por similitud con embeddings y qué determina la puntuación?"  
> - "¿Qué umbral de similitud debería usar y cómo afecta los resultados?"  
> - "¿Cómo manejo casos donde no se encuentran documentos relevantes?"

### Generación de Respuestas

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Los fragmentos más relevantes se ensamblan en un prompt estructurado que incluye instrucciones explícitas, el contexto recuperado y la pregunta del usuario. El modelo lee esos fragmentos específicos y responde basándose en esa información — solo puede usar lo que tiene delante, lo que previene alucinaciones.

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
  
El diagrama a continuación muestra este ensamblaje en acción — los fragmentos con mejor puntuación de la búsqueda se inyectan en la plantilla de prompt, y el `OpenAiOfficialChatModel` genera una respuesta fundamentada:

<img src="../../../translated_images/es/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

## Ejecutar la Aplicación

**Verificar despliegue:**

Asegúrate que el archivo `.env` existe en el directorio raíz con las credenciales de Azure (creado durante el Módulo 01):  
```bash
cat ../.env  # Debe mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Iniciar la aplicación:**

> **Nota:** Si ya iniciaste todas las aplicaciones usando `./start-all.sh` del Módulo 01, este módulo ya está corriendo en el puerto 8081. Puedes saltarte los comandos de inicio a continuación e ir directamente a http://localhost:8081.

**Opción 1: Usar el Spring Boot Dashboard (Recomendado para usuarios de VS Code)**

El contenedor de desarrollo incluye la extensión Spring Boot Dashboard, que provee una interfaz visual para gestionar todas las aplicaciones Spring Boot. Puedes encontrarla en la Barra de Actividad al lado izquierdo de VS Code (busca el ícono de Spring Boot).

Desde el Spring Boot Dashboard puedes:  
- Ver todas las aplicaciones Spring Boot disponibles en el espacio de trabajo  
- Iniciar/detener aplicaciones con un solo clic  
- Ver los logs de la aplicación en tiempo real  
- Monitorear el estado de la aplicación  

Simplemente haz clic en el botón de play junto a "rag" para iniciar este módulo, o inicia todos los módulos a la vez.

<img src="../../../translated_images/es/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

**Opción 2: Usar scripts shell**

Inicia todas las aplicaciones web (módulos 01-04):

**Bash:**  
```bash
cd ..  # Desde el directorio raíz
./start-all.sh
```
  
**PowerShell:**  
```powershell
cd ..  # Desde el directorio raíz
.\start-all.ps1
```
  
O inicia solo este módulo:

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
  
Ambos scripts cargan automáticamente variables de entorno desde el archivo `.env` raíz y construirán los JAR si no existen.

> **Nota:** Si prefieres compilar todos los módulos manualmente antes de iniciar:  
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
  
Abre http://localhost:8081 en tu navegador.

**Para detener:**

**Bash:**  
```bash
./stop.sh  # Solo este módulo
# O
cd .. && ./stop-all.sh  # Todos los módulos
```
  
**PowerShell:**  
```powershell
.\stop.ps1  # Solo este módulo
# O
cd ..; .\stop-all.ps1  # Todos los módulos
```


## Usar la Aplicación

La aplicación ofrece una interfaz web para subir documentos y realizar preguntas.

<a href="images/rag-homepage.png"><img src="../../../translated_images/es/rag-homepage.d90eb5ce1b3caa94.webp" alt="Interfaz de la Aplicación RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Interfaz de la aplicación RAG - sube documentos y haz preguntas*

### Subir un Documento

Comienza subiendo un documento — los archivos TXT funcionan mejor para pruebas. Se provee un `sample-document.txt` en este directorio que contiene información sobre características de LangChain4j, implementación de RAG y mejores prácticas — perfecto para probar el sistema.

El sistema procesa tu documento, lo divide en fragmentos, y crea embeddings para cada uno. Esto ocurre automáticamente al subirlo.

### Hacer Preguntas

Ahora formula preguntas específicas sobre el contenido del documento. Prueba con algo factual que esté claramente indicado en el documento. El sistema busca fragmentos relevantes, los incluye en el prompt, y genera una respuesta.

### Revisar Referencias de Fuente

Observa que cada respuesta incluye referencias a las fuentes con puntajes de similitud. Estos puntajes (de 0 a 1) muestran qué tan relevante fue cada fragmento para tu pregunta. Puntajes más altos significan mejores coincidencias. Esto te permite verificar la respuesta contra el material fuente.

<a href="images/rag-query-results.png"><img src="../../../translated_images/es/rag-query-results.6d69fcec5397f355.webp" alt="Resultados de Consulta RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Resultados de consulta mostrando respuesta con referencias y puntuaciones de relevancia*

### Experimentar con Preguntas

Prueba diferentes tipos de preguntas:  
- Hechos específicos: "¿Cuál es el tema principal?"  
- Comparaciones: "¿Cuál es la diferencia entre X e Y?"  
- Resúmenes: "Resume los puntos clave sobre Z"

Observa cómo cambian las puntuaciones de relevancia según qué tan bien tu pregunta coincida con el contenido del documento.

## Conceptos Clave

### Estrategia de División en Fragmentos

Los documentos se dividen en fragmentos de 300 tokens con 30 tokens de solapamiento. Este equilibrio asegura que cada fragmento tenga suficiente contexto para ser significativo pero sea lo suficientemente pequeño para incluir múltiples fragmentos en un prompt.

### Puntajes de Similitud

Cada fragmento recuperado viene con un puntaje de similitud entre 0 y 1 que indica qué tan cercano está a la pregunta del usuario. El diagrama abajo visualiza los rangos de puntaje y cómo el sistema los usa para filtrar resultados:

<img src="../../../translated_images/es/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

Los puntajes varían de 0 a 1:  
- 0.7-1.0: Altamente relevante, coincidencia exacta  
- 0.5-0.7: Relevante, buen contexto  
- Menor a 0.5: Filtrado, demasiado disímil

El sistema solo recupera fragmentos por encima del umbral mínimo para asegurar calidad.

### Almacenamiento en Memoria

Este módulo usa almacenamiento en memoria por simplicidad. Al reiniciar la aplicación, los documentos subidos se pierden. Los sistemas en producción usan bases de datos vectoriales persistentes como Qdrant o Azure AI Search.

### Gestión de la Ventana de Contexto

Cada modelo tiene una ventana de contexto máxima. No puedes incluir todos los fragmentos de un documento grande. El sistema recupera los N fragmentos más relevantes (por defecto 5) para mantenerse dentro de los límites mientras provee suficiente contexto para respuestas precisas.

## Cuándo es Importante RAG

RAG no siempre es el enfoque correcto. La guía de decisión abajo te ayuda a determinar cuándo RAG aporta valor versus cuándo enfoques más simples — como incluir contenido directamente en el prompt o confiar en el conocimiento incorporado del modelo — son suficientes:

<img src="../../../translated_images/es/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

**Usa RAG cuando:**
- Responder preguntas sobre documentos propietarios  
- La información cambia con frecuencia (políticas, precios, especificaciones)  
- La exactitud requiere atribución de la fuente  
- El contenido es demasiado grande para encajar en un solo aviso  
- Necesitas respuestas verificables y fundamentadas  

**No uses RAG cuando:**  
- Las preguntas requieren conocimientos generales que el modelo ya posee  
- Se necesitan datos en tiempo real (RAG funciona con documentos subidos)  
- El contenido es lo suficientemente pequeño para incluirse directamente en los avisos  

## Próximos pasos

**Siguiente módulo:** [04-tools - Agentes de IA con herramientas](../04-tools/README.md)

---

**Navegación:** [← Anterior: Módulo 02 - Ingeniería de Prompt](../02-prompt-engineering/README.md) | [Volver al inicio](../README.md) | [Siguiente: Módulo 04 - Herramientas →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso legal**:  
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por la precisión, tenga en cuenta que las traducciones automáticas pueden contener errores o inexactitudes. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda una traducción profesional realizada por humanos. No nos responsabilizamos por malentendidos o interpretaciones erróneas derivadas del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->