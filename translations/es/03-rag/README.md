# Módulo 03: RAG (Generación Aumentada por Recuperación)

## Tabla de Contenidos

- [Video Tutorial](../../../03-rag)
- [Qué Aprenderás](../../../03-rag)
- [Prerrequisitos](../../../03-rag)
- [Entendiendo RAG](../../../03-rag)
  - [¿Qué Enfoque de RAG Usa Este Tutorial?](../../../03-rag)
- [Cómo Funciona](../../../03-rag)
  - [Procesamiento de Documentos](../../../03-rag)
  - [Creación de Embeddings](../../../03-rag)
  - [Búsqueda Semántica](../../../03-rag)
  - [Generación de Respuestas](../../../03-rag)
- [Ejecutar la Aplicación](../../../03-rag)
- [Usar la Aplicación](../../../03-rag)
  - [Subir un Documento](../../../03-rag)
  - [Hacer Preguntas](../../../03-rag)
  - [Verificar Referencias de Fuente](../../../03-rag)
  - [Experimentar con Preguntas](../../../03-rag)
- [Conceptos Clave](../../../03-rag)
  - [Estrategia de Fragmentación](../../../03-rag)
  - [Puntajes de Similitud](../../../03-rag)
  - [Almacenamiento en Memoria](../../../03-rag)
  - [Gestión de la Ventana de Contexto](../../../03-rag)
- [Cuándo Importa RAG](../../../03-rag)
- [Próximos Pasos](../../../03-rag)

## Video Tutorial

Mira esta sesión en vivo que explica cómo comenzar con este módulo: [RAG con LangChain4j - Sesión en Vivo](https://www.youtube.com/watch?v=_olq75ZH_eY)

## Qué Aprenderás

En los módulos anteriores, aprendiste cómo tener conversaciones con IA y estructurar tus prompts de manera efectiva. Pero hay una limitación fundamental: los modelos de lenguaje solo saben lo que aprendieron durante el entrenamiento. No pueden responder preguntas sobre las políticas de tu empresa, la documentación de tu proyecto, ni ninguna información que no hayan visto durante el entrenamiento.

RAG (Generación Aumentada por Recuperación) soluciona este problema. En lugar de intentar enseñar al modelo tu información (lo cual es costoso e impráctico), le das la capacidad de buscar en tus documentos. Cuando alguien hace una pregunta, el sistema encuentra información relevante e incluye esa información en el prompt. Entonces el modelo responde basado en ese contexto recuperado.

Piensa en RAG como darle al modelo una biblioteca de referencia. Cuando haces una pregunta, el sistema:

1. **Consulta del Usuario** – Haces una pregunta
2. **Embedding** – Convierte tu pregunta en un vector
3. **Búsqueda Vectorial** – Encuentra fragmentos de documentos similares
4. **Ensamblaje del Contexto** – Añade fragmentos relevantes al prompt
5. **Respuesta** – El modelo LLM genera una respuesta basada en el contexto

Esto fundamenta las respuestas del modelo en tus datos reales en lugar de confiar solo en su conocimiento de entrenamiento o inventar respuestas.

## Prerrequisitos

- Haber completado [Módulo 00 – Inicio Rápido](../00-quick-start/README.md) (para el ejemplo Easy RAG mencionado arriba)
- Haber completado [Módulo 01 – Introducción](../01-introduction/README.md) (recursos de Azure OpenAI desplegados, incluido el modelo de embedding `text-embedding-3-small`)
- Archivo `.env` en el directorio raíz con credenciales de Azure (creado por `azd up` en el Módulo 01)

> **Nota:** Si no has completado el Módulo 01, sigue primero las instrucciones de despliegue ahí. El comando `azd up` despliega tanto el modelo de chat GPT como el modelo de embedding usado en este módulo.

## Entendiendo RAG

El diagrama a continuación ilustra el concepto principal: en lugar de depender únicamente de los datos de entrenamiento del modelo, RAG le da una biblioteca de referencia con tus documentos para consultar antes de generar cada respuesta.

<img src="../../../translated_images/es/what-is-rag.1f9005d44b07f2d8.webp" alt="Qué es RAG" width="800"/>

*Este diagrama muestra la diferencia entre un LLM estándar (que adivina en base a los datos de entrenamiento) y un LLM mejorado con RAG (que consulta primero tus documentos).*

Así es como las piezas se conectan de extremo a extremo. La pregunta de un usuario pasa por cuatro etapas — embedding, búsqueda vectorial, ensamblaje del contexto y generación de respuesta — cada una construyendo sobre la anterior:

<img src="../../../translated_images/es/rag-architecture.ccb53b71a6ce407f.webp" alt="Arquitectura RAG" width="800"/>

*Este diagrama muestra la tubería completa de RAG — una consulta de usuario pasa por embedding, búsqueda vectorial, ensamblaje de contexto y generación de respuesta.*

El resto de este módulo detalla cada etapa con código que puedes ejecutar y modificar.

### ¿Qué Enfoque de RAG Usa Este Tutorial?

LangChain4j ofrece tres maneras de implementar RAG, cada una con distinto nivel de abstracción. El diagrama abajo las compara lado a lado:

<img src="../../../translated_images/es/rag-approaches.5b97fdcc626f1447.webp" alt="Tres Enfoques RAG en LangChain4j" width="800"/>

*Este diagrama compara los tres enfoques RAG de LangChain4j — Easy, Native y Advanced — mostrando sus componentes clave y cuándo usar cada uno.*

| Enfoque | Qué Hace | Compromiso |
|---|---|---|
| **Easy RAG** | Conecta todo automáticamente mediante `AiServices` y `ContentRetriever`. Anotas una interfaz, adjuntas un recuperador, y LangChain4j maneja el embedding, búsqueda y ensamblaje de prompt tras bambalinas. | Código mínimo, pero no ves qué sucede en cada paso. |
| **Native RAG** | Llamas al modelo de embedding, buscas en el almacén, construyes el prompt y generas la respuesta tú mismo — un paso explícito a la vez. | Más código, pero cada etapa es visible y modificable. |
| **Advanced RAG** | Usa el framework `RetrievalAugmentor` con transformadores de consulta, enrutadores, reordenadores e inyectores de contenido conectables para pipelines de nivel producción. | Máxima flexibilidad, pero con mucha más complejidad. |

**Este tutorial usa el enfoque Native.** Cada paso de la tubería RAG — embedder la consulta, buscar en el almacén vectorial, ensamblar el contexto y generar la respuesta — está explícitamente escrito en [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Esto es intencional: como recurso de aprendizaje, es más importante que veas y comprendas cada etapa que minimizar el código. Cuando te sientas cómodo con cómo encajan las piezas, puedes avanzar a Easy RAG para prototipos rápidos o Advanced RAG para sistemas de producción.

> **💡 ¿Ya viste Easy RAG en acción?** El [módulo Inicio Rápido](../00-quick-start/README.md) incluye un ejemplo de preguntas y respuestas sobre documentos ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) que usa el enfoque Easy RAG — LangChain4j maneja automáticamente embedding, búsqueda y ensamblaje de prompt. Este módulo da el siguiente paso abriendo esa tubería para que puedas ver y controlar cada etapa tú mismo.

<img src="../../../translated_images/es/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Pipeline Easy RAG - LangChain4j" width="800"/>

*Este diagrama muestra la tubería Easy RAG de `SimpleReaderDemo.java`. Compáralo con el enfoque Native usado en este módulo: Easy RAG oculta el embedding, recuperación y ensamblaje del prompt tras `AiServices` y `ContentRetriever` — cargas un documento, adjuntas un recuperador y obtienes respuestas. El enfoque Native de este módulo abre esa tubería para que llames cada etapa (embed, buscar, ensamblar contexto, generar) tú mismo, dándote total visibilidad y control.*

## Cómo Funciona

La tubería RAG de este módulo se divide en cuatro etapas que se ejecutan secuencialmente cada vez que un usuario hace una pregunta. Primero, un documento subido es **analizado y fragmentado** en piezas manejables. Esas piezas se convierten en **embeddings vectoriales** y se almacenan para poder compararlas matemáticamente. Cuando llega una consulta, el sistema realiza una **búsqueda semántica** para encontrar los fragmentos más relevantes, y finalmente los pasa como contexto al LLM para la **generación de la respuesta**. Las secciones siguientes recorren cada etapa con código real y diagramas. Veamos el primer paso.

### Procesamiento de Documentos

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Cuando subes un documento, el sistema lo analiza (PDF o texto plano), adjunta metadatos como el nombre del archivo, y luego lo divide en fragmentos — pedazos más pequeños que caben cómodamente en la ventana de contexto del modelo. Estos fragmentos se solapan ligeramente para no perder contexto en los bordes.

```java
// Analiza el archivo subido y envuélvelo en un Documento de LangChain4j
Document document = Document.from(content, metadata);

// Divide en fragmentos de 300 tokens con una superposición de 30 tokens
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

El diagrama a continuación muestra cómo funciona esto visualmente. Nota cómo cada fragmento comparte algunos tokens con los vecinos — el solapamiento de 30 tokens asegura que no se pierda contexto importante entre fragmentos:

<img src="../../../translated_images/es/document-chunking.a5df1dd1383431ed.webp" alt="Fragmentación de Documento" width="800"/>

*Este diagrama muestra un documento dividido en fragmentos de 300 tokens con 30 tokens de solapamiento, preservando el contexto en los límites de los fragmentos.*

> **🤖 Prueba con el Chat de [GitHub Copilot](https://github.com/features/copilot):** Abre [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) y pregunta:
> - "¿Cómo divide LangChain4j los documentos en fragmentos y por qué es importante el solapamiento?"
> - "¿Cuál es el tamaño óptimo del fragmento para diferentes tipos de documentos y por qué?"
> - "¿Cómo manejo documentos en varios idiomas o con formato especial?"

### Creación de Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Cada fragmento se convierte en una representación numérica llamada embedding — esencialmente un convertidor de significado a números. El modelo de embedding no es "inteligente" como un modelo de chat; no puede seguir instrucciones, razonar ni responder preguntas. Lo que sí puede hacer es mapear texto a un espacio matemático donde significados similares quedan cerca — "coche" cerca de "automóvil," "política de reembolso" cerca de "devolver mi dinero." Piensa en un modelo de chat como una persona con quien puedes hablar; un modelo de embedding es un sistema de archivado ultra eficiente.

<img src="../../../translated_images/es/embedding-model-concept.90760790c336a705.webp" alt="Concepto del Modelo de Embedding" width="800"/>

*Este diagrama muestra cómo un modelo de embedding convierte texto en vectores numéricos, colocando significados similares — como "coche" y "automóvil" — cerca uno del otro en el espacio vectorial.*

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

El diagrama de clases a continuación muestra los dos flujos separados en una tubería RAG y las clases de LangChain4j que los implementan. El **flujo de ingestión** (se ejecuta una vez al subir) divide el documento, embebe los fragmentos y los almacena vía `.addAll()`. El **flujo de consulta** (se ejecuta cada vez que un usuario pregunta) embebe la pregunta, busca en el almacén vía `.search()`, y pasa el contexto seleccionado al modelo de chat. Ambos flujos se conectan a través de la interfaz común `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/es/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="Clases RAG LangChain4j" width="800"/>

*Este diagrama muestra los dos flujos en una tubería RAG — ingestión y consulta — y cómo se conectan mediante un EmbeddingStore compartido.*

Una vez almacenados los embeddings, el contenido similar naturalmente se agrupa en el espacio vectorial. La visualización abajo muestra cómo documentos sobre temas relacionados terminan como puntos cercanos, lo que hace posible la búsqueda semántica:

<img src="../../../translated_images/es/vector-embeddings.2ef7bdddac79a327.webp" alt="Espacio de Embeddings Vectoriales" width="800"/>

*Esta visualización muestra cómo documentos relacionados se agrupan juntos en un espacio vectorial 3D, con temas como Documentación Técnica, Reglas de Negocio y FAQs formando grupos distintos.*

Cuando un usuario realiza una búsqueda, el sistema sigue cuatro pasos: embebe los documentos una vez, embebe la consulta en cada búsqueda, compara el vector de consulta contra todos los vectores almacenados usando similitud coseno, y devuelve los top-K fragmentos con mayor puntuación. El diagrama abajo explica cada paso y las clases de LangChain4j involucradas:

<img src="../../../translated_images/es/embedding-search-steps.f54c907b3c5b4332.webp" alt="Pasos de Búsqueda con Embeddings" width="800"/>

*Este diagrama muestra el proceso de búsqueda en cuatro pasos: embebido de documentos, embebido de la consulta, comparación de vectores con similitud coseno y devolución de los top-K resultados.*

### Búsqueda Semántica

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Cuando haces una pregunta, tu pregunta también se convierte en un embedding. El sistema compara el embedding de tu pregunta contra los embeddings de todos los fragmentos del documento. Encuentra los fragmentos con significados más similares — no solo coincide con palabras clave, sino similitud semántica real.

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

El diagrama a continuación contrasta la búsqueda semántica con la búsqueda tradicional por palabras clave. Una búsqueda por palabra clave de "vehículo" no encuentra un fragmento sobre "autos y camiones," pero la búsqueda semántica entiende que significan lo mismo y lo devuelve como una coincidencia de alta puntuación:

<img src="../../../translated_images/es/semantic-search.6b790f21c86b849d.webp" alt="Búsqueda Semántica" width="800"/>

*Este diagrama compara la búsqueda basada en palabras clave con la búsqueda semántica, mostrando cómo la búsqueda semántica recupera contenido conceptualmente relacionado incluso cuando las palabras clave exactas difieren.*

Detrás de escena, la similitud se mide usando la similitud coseno — básicamente preguntando "¿apuntan estas dos flechas en la misma dirección?" Dos fragmentos pueden usar palabras completamente diferentes, pero si significan lo mismo, sus vectores apuntan igual y la puntuación está cerca de 1.0:

<img src="../../../translated_images/es/cosine-similarity.9baeaf3fc3336abb.webp" alt="Similitud Coseno" width="800"/>

*Este diagrama ilustra la similitud coseno como el ángulo entre vectores de embedding — vectores más alineados puntúan más cerca de 1.0, indicando mayor similitud semántica.*
> **🤖 Prueba con [GitHub Copilot](https://github.com/features/copilot) Chat:** Abre [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) y pregunta:
> - "¿Cómo funciona la búsqueda por similitud con embeddings y qué determina la puntuación?"
> - "¿Qué umbral de similitud debería usar y cómo afecta a los resultados?"
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

El diagrama a continuación muestra este ensamblaje en acción — los fragmentos con mayor puntuación del paso de búsqueda se inyectan en la plantilla del prompt, y el `OpenAiOfficialChatModel` genera una respuesta fundamentada:

<img src="../../../translated_images/es/context-assembly.7e6dd60c31f95978.webp" alt="Montaje de contexto" width="800"/>

*Este diagrama muestra cómo los fragmentos con mayor puntuación se ensamblan en un prompt estructurado, permitiendo que el modelo genere una respuesta fundamentada a partir de tus datos.*

## Ejecutar la Aplicación

**Verificar el despliegue:**

Asegúrate de que el archivo `.env` exista en el directorio raíz con las credenciales de Azure (creado durante el Módulo 01):

**Bash:**
```bash
cat ../.env  # Debe mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Debe mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Iniciar la aplicación:**

> **Nota:** Si ya iniciaste todas las aplicaciones usando `./start-all.sh` desde el Módulo 01, este módulo ya está funcionando en el puerto 8081. Puedes omitir los comandos de inicio a continuación y acceder directamente a http://localhost:8081.

**Opción 1: Usando Spring Boot Dashboard (Recomendado para usuarios de VS Code)**

El contenedor de desarrollo incluye la extensión Spring Boot Dashboard, que proporciona una interfaz visual para gestionar todas las aplicaciones Spring Boot. Puedes encontrarla en la Barra de Actividades a la izquierda de VS Code (busca el ícono de Spring Boot).

Desde el Spring Boot Dashboard, puedes:
- Ver todas las aplicaciones Spring Boot disponibles en el espacio de trabajo
- Iniciar/detener aplicaciones con un solo clic
- Ver los registros de la aplicación en tiempo real
- Monitorizar el estado de la aplicación

Simplemente haz clic en el botón de reproducción junto a "rag" para iniciar este módulo, o inicia todos los módulos a la vez.

<img src="../../../translated_images/es/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Esta captura de pantalla muestra el Spring Boot Dashboard en VS Code, donde puedes iniciar, detener y monitorizar aplicaciones visualmente.*

**Opción 2: Usando scripts de shell**

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

Ambos scripts cargan automáticamente las variables de entorno desde el archivo `.env` raíz y compilarán los JAR si no existen.

> **Nota:** Si prefieres compilar todos los módulos manualmente antes de iniciar:
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

## Uso de la Aplicación

La aplicación proporciona una interfaz web para subir documentos y hacer preguntas.

<a href="images/rag-homepage.png"><img src="../../../translated_images/es/rag-homepage.d90eb5ce1b3caa94.webp" alt="Interfaz de la aplicación RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Esta captura de pantalla muestra la interfaz de la aplicación RAG donde subes documentos y haces preguntas.*

### Subir un Documento

Comienza subiendo un documento - los archivos TXT funcionan mejor para pruebas. Se proporciona un `sample-document.txt` en este directorio que contiene información sobre las características de LangChain4j, implementación RAG, y mejores prácticas - perfecto para probar el sistema.

El sistema procesa tu documento, lo divide en fragmentos, y crea embeddings para cada fragmento. Esto sucede automáticamente al subir.

### Hacer Preguntas

Ahora formula preguntas específicas sobre el contenido del documento. Prueba algo factual que esté claramente expresado en el documento. El sistema busca fragmentos relevantes, los incluye en el prompt, y genera una respuesta.

### Verifica las Referencias de Fuente

Observa que cada respuesta incluye referencias de fuente con puntuaciones de similitud. Estas puntuaciones (de 0 a 1) muestran qué tan relevante fue cada fragmento para tu pregunta. Puntuaciones más altas significan mejores coincidencias. Esto te permite verificar la respuesta contra el material fuente.

<a href="images/rag-query-results.png"><img src="../../../translated_images/es/rag-query-results.6d69fcec5397f355.webp" alt="Resultados de Consulta RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Esta captura muestra los resultados de la consulta con la respuesta generada, referencias de fuente y puntuaciones de relevancia para cada fragmento recuperado.*

### Experimenta con Preguntas

Prueba diferentes tipos de preguntas:
- Hechos específicos: "¿Cuál es el tema principal?"
- Comparaciones: "¿Cuál es la diferencia entre X y Y?"
- Resúmenes: "Resume los puntos clave sobre Z"

Observa cómo cambian las puntuaciones de relevancia según qué tan bien tu pregunta coincida con el contenido del documento.

## Conceptos Clave

### Estrategia de Fragmentación

Los documentos se dividen en fragmentos de 300 tokens con 30 tokens de solapamiento. Este balance asegura que cada fragmento tenga suficiente contexto para ser significativo mientras se mantiene lo suficientemente pequeño para incluir múltiples fragmentos en un prompt.

### Puntuaciones de Similitud

Cada fragmento recuperado viene con una puntuación de similitud entre 0 y 1 que indica cuán estrechamente coincide con la pregunta del usuario. El diagrama a continuación visualiza los rangos de puntuación y cómo el sistema los usa para filtrar resultados:

<img src="../../../translated_images/es/similarity-scores.b0716aa911abf7f0.webp" alt="Puntuaciones de Similitud" width="800"/>

*Este diagrama muestra rangos de puntuación de 0 a 1, con un umbral mínimo de 0.5 que filtra fragmentos irrelevantes.*

Las puntuaciones van de 0 a 1:
- 0.7-1.0: Altamente relevante, coincidencia exacta
- 0.5-0.7: Relevante, buen contexto
- Por debajo de 0.5: Filtrado, demasiado disímil

El sistema solo recupera fragmentos por encima del umbral mínimo para garantizar calidad.

Los embeddings funcionan bien cuando los significados están claramente agrupados, pero tienen puntos ciegos. El diagrama siguiente muestra modos comunes de fallo — fragmentos demasiado grandes producen vectores confusos, fragmentos demasiado pequeños carecen de contexto, términos ambiguos apuntan a múltiples agrupaciones, y búsquedas por coincidencia exacta (IDs, números de parte) no funcionan con embeddings en absoluto:

<img src="../../../translated_images/es/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Modos de Fallo de Embeddings" width="800"/>

*Este diagrama muestra modos comunes de fallo de embeddings: fragmentos demasiado grandes, fragmentos demasiado pequeños, términos ambiguos que apuntan a múltiples agrupaciones, y búsquedas por coincidencia exacta como IDs.*

### Almacenamiento en Memoria

Este módulo usa almacenamiento en memoria por simplicidad. Cuando reinicias la aplicación, los documentos subidos se pierden. Los sistemas en producción usan bases de datos vectoriales persistentes como Qdrant o Azure AI Search.

### Gestión de la Ventana de Contexto

Cada modelo tiene un máximo de ventana de contexto. No puedes incluir todos los fragmentos de un documento grande. El sistema recupera los N fragmentos más relevantes (por defecto 5) para mantenerse dentro de límites mientras provee suficiente contexto para respuestas precisas.

## Cuándo Importa RAG

RAG no siempre es el enfoque adecuado. La guía de decisión siguiente te ayuda a determinar cuándo RAG agrega valor vs. cuándo enfoques más simples — como incluir contenido directamente en el prompt o confiar en el conocimiento incorporado del modelo — son suficientes:

<img src="../../../translated_images/es/when-to-use-rag.1016223f6fea26bc.webp" alt="Cuándo usar RAG" width="800"/>

*Este diagrama muestra una guía de decisión para cuándo RAG agrega valor y cuándo enfoques más simples son suficientes.*

**Usa RAG cuando:**
- Se responden preguntas sobre documentos propietarios
- La información cambia frecuentemente (políticas, precios, especificaciones)
- La precisión requiere atribución de fuentes
- El contenido es demasiado grande para caber en un solo prompt
- Necesitas respuestas verificables y fundamentadas

**No uses RAG cuando:**
- Las preguntas requieren conocimiento general que el modelo ya tiene
- Se necesitan datos en tiempo real (RAG trabaja sobre documentos subidos)
- El contenido es lo suficientemente pequeño para incluirlo directamente en prompts

## Próximos Pasos

**Siguiente Módulo:** [04-tools - Agentes de IA con Herramientas](../04-tools/README.md)

---

**Navegación:** [← Anterior: Módulo 02 - Ingeniería de Prompts](../02-prompt-engineering/README.md) | [Volver al Inicio](../README.md) | [Siguiente: Módulo 04 - Herramientas →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso legal**:  
Este documento ha sido traducido utilizando el servicio de traducción por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por la precisión, tenga en cuenta que las traducciones automatizadas pueden contener errores o inexactitudes. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda una traducción profesional realizada por humanos. No nos responsabilizamos por malentendidos o interpretaciones erróneas derivadas del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->