# Módulo 03: RAG (Generación Aumentada por Recuperación)

## Tabla de Contenidos

- [Recorrido en Video](../../../03-rag)
- [Qué Aprenderás](../../../03-rag)
- [Requisitos Previos](../../../03-rag)
- [Entendiendo RAG](../../../03-rag)
  - [¿Qué Enfoque de RAG Usa Este Tutorial?](../../../03-rag)
- [Cómo Funciona](../../../03-rag)
  - [Procesamiento de Documentos](../../../03-rag)
  - [Creación de Embeddings](../../../03-rag)
  - [Búsqueda Semántica](../../../03-rag)
  - [Generación de Respuestas](../../../03-rag)
- [Ejecuta la Aplicación](../../../03-rag)
- [Usando la Aplicación](../../../03-rag)
  - [Sube un Documento](../../../03-rag)
  - [Haz Preguntas](../../../03-rag)
  - [Consulta las Referencias de Origen](../../../03-rag)
  - [Experimenta con Preguntas](../../../03-rag)
- [Conceptos Clave](../../../03-rag)
  - [Estrategia de Segmentación](../../../03-rag)
  - [Puntajes de Similitud](../../../03-rag)
  - [Almacenamiento en Memoria](../../../03-rag)
  - [Gestión de la Ventana de Contexto](../../../03-rag)
- [Cuándo Importa RAG](../../../03-rag)
- [Próximos Pasos](../../../03-rag)

## Recorrido en Video

Mira esta sesión en vivo que explica cómo comenzar con este módulo:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG con LangChain4j - Sesión en Vivo" width="800"/></a>

## Qué Aprenderás

En los módulos anteriores, aprendiste cómo tener conversaciones con IA y estructurar tus indicaciones efectivamente. Pero hay una limitación fundamental: los modelos de lenguaje sólo conocen lo que aprendieron durante el entrenamiento. No pueden responder preguntas sobre las políticas de tu empresa, la documentación de tu proyecto o cualquier información para la que no fueron entrenados.

RAG (Generación Aumentada por Recuperación) resuelve este problema. En lugar de intentar enseñar al modelo tu información (lo cual es costoso e impráctico), le das la capacidad de buscar en tus documentos. Cuando alguien hace una pregunta, el sistema encuentra la información relevante e la incluye en la indicación. Luego el modelo responde basado en ese contexto recuperado.

Piensa en RAG como darle al modelo una biblioteca de referencia. Cuando haces una pregunta, el sistema:

1. **Consulta del Usuario** - Haces una pregunta  
2. **Embedding** - Convierte tu pregunta en un vector  
3. **Búsqueda Vectorial** - Encuentra fragmentos de documentos similares  
4. **Ensamblaje del Contexto** - Añade fragmentos relevantes a la indicación  
5. **Respuesta** - El LLM genera una respuesta basada en el contexto  

Esto fundamenta las respuestas del modelo en tus datos reales en lugar de depender sólo del conocimiento de entrenamiento o inventar respuestas.

## Requisitos Previos

- Haber completado [Módulo 00 - Inicio Rápido](../00-quick-start/README.md) (para el ejemplo Easy RAG mencionado arriba)
- Haber completado [Módulo 01 - Introducción](../01-introduction/README.md) (recursos de Azure OpenAI desplegados, incluido el modelo de embedding `text-embedding-3-small`)
- Archivo `.env` en el directorio raíz con credenciales de Azure (creado con `azd up` en Módulo 01)

> **Nota:** Si no has completado el Módulo 01, sigue primero las instrucciones de despliegue ahí. El comando `azd up` despliega tanto el modelo de chat GPT como el modelo de embedding usado en este módulo.

## Entendiendo RAG

El diagrama a continuación ilustra el concepto central: en vez de depender sólo de los datos de entrenamiento del modelo, RAG le da una biblioteca de referencia con tus documentos para consultar antes de generar cada respuesta.

<img src="../../../translated_images/es/what-is-rag.1f9005d44b07f2d8.webp" alt="Qué es RAG" width="800"/>

*Este diagrama muestra la diferencia entre un LLM estándar (que adivina a partir de los datos de entrenamiento) y un LLM mejorado con RAG (que primero consulta tus documentos).*

Así es como las partes se conectan de extremo a extremo. La pregunta del usuario pasa por cuatro etapas — embedding, búsqueda vectorial, ensamblaje del contexto y generación de respuestas — cada una construyendo sobre la anterior:

<img src="../../../translated_images/es/rag-architecture.ccb53b71a6ce407f.webp" alt="Arquitectura RAG" width="800"/>

*Este diagrama muestra la pipeline completa de RAG: la consulta del usuario pasa por embedding, búsqueda vectorial, ensamblaje del contexto y generación de respuesta.*

El resto de este módulo explica cada etapa en detalle, con código que puedes ejecutar y modificar.

### ¿Qué Enfoque de RAG Usa Este Tutorial?

LangChain4j ofrece tres formas de implementar RAG, cada una con un nivel diferente de abstracción. El diagrama a continuación las compara lado a lado:

<img src="../../../translated_images/es/rag-approaches.5b97fdcc626f1447.webp" alt="Tres Enfoques de RAG en LangChain4j" width="800"/>

*Este diagrama compara las tres aproximaciones RAG de LangChain4j — Fácil, Nativa y Avanzada — mostrando sus componentes clave y cuándo usar cada una.*

| Enfoque | Qué Hace | Compromiso |
|---|---|---|
| **Easy RAG** | Conecta todo automáticamente mediante `AiServices` y `ContentRetriever`. Anotas una interfaz, enlazas un recuperador, y LangChain4j maneja embedding, búsqueda y ensamblaje de indicaciones tras bambalinas. | Código mínimo, pero no ves qué pasa en cada paso. |
| **Native RAG** | Llamas al modelo de embedding, buscas en la tienda, construyes la indicación y generas la respuesta tú mismo, paso a paso de forma explícita. | Más código, pero cada etapa es visible y modificable. |
| **Advanced RAG** | Usa el framework `RetrievalAugmentor` con transformadores de consultas, enrutadores, reordenadores e inyectores de contenido enchufables para pipelines de producción. | Máxima flexibilidad, pero mucha más complejidad. |

**Este tutorial usa el enfoque Native.** Cada paso del pipeline RAG — embedding de la consulta, búsqueda en el almacén vectorial, ensamblaje del contexto y generación de respuesta — está escrito explícitamente en [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Esto es intencional: como recurso de aprendizaje, es más importante que veas y entiendas cada etapa que minimizar el código. Una vez comprendas cómo encajan las piezas, puedes pasar a Easy RAG para prototipos rápidos o Advanced RAG para sistemas productivos.

> **💡 ¿Ya viste Easy RAG funcionando?** El [módulo de Inicio Rápido](../00-quick-start/README.md) incluye un ejemplo de Preguntas y Respuestas con Documentos ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) que usa el enfoque Easy RAG — LangChain4j maneja embedding, búsqueda y ensamblaje de indicaciones automáticamente. Este módulo da el siguiente paso y abre ese pipeline para que puedas ver y controlar cada etapa tú mismo.

<img src="../../../translated_images/es/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Pipeline Easy RAG - LangChain4j" width="800"/>

*Este diagrama muestra el pipeline Easy RAG de `SimpleReaderDemo.java`. Compara esto con el enfoque Native usado en este módulo: Easy RAG oculta embedding, recuperación y ensamblaje de indicaciones detrás de `AiServices` y `ContentRetriever` — cargas un documento, enlazas un recuperador y obtienes respuestas. El enfoque Native de este módulo abre ese pipeline para que llames cada etapa (incrustar, buscar, ensamblar contexto, generar) tú mismo, dándote visión y control total.*

## Cómo Funciona

El pipeline RAG en este módulo se divide en cuatro etapas que se ejecutan en secuencia cada vez que un usuario hace una pregunta. Primero, un documento subido es **analizado y segmentado** en piezas manejables. Estos fragmentos se convierten en **embeddings vectoriales** y se almacenan para poder compararse matemáticamente. Cuando llega una consulta, el sistema realiza una **búsqueda semántica** para encontrar los fragmentos más relevantes, y finalmente los pasa como contexto al LLM para la **generación de respuestas**. Las secciones siguientes explican cada etapa con código real y diagramas. Veamos el primer paso.

### Procesamiento de Documentos

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Cuando subes un documento, el sistema lo analiza (PDF o texto plano), adjunta metadatos como el nombre de archivo, y luego lo divide en fragmentos — piezas más pequeñas que caben cómodamente en la ventana de contexto del modelo. Estos fragmentos se superponen ligeramente para no perder contexto en los límites.

```java
// Analiza el archivo subido y envuélvelo en un Documento de LangChain4j
Document document = Document.from(content, metadata);

// Divide en fragmentos de 300 tokens con una superposición de 30 tokens
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

El diagrama a continuación muestra cómo funciona visualmente. Observa cómo cada fragmento comparte algunos tokens con los vecinos — la superposición de 30 tokens asegura que ningún contexto importante se pierda entre fragmentos:

<img src="../../../translated_images/es/document-chunking.a5df1dd1383431ed.webp" alt="Segmentación de Documento" width="800"/>

*Este diagrama muestra un documento dividido en fragmentos de 300 tokens con 30 tokens de superposición, preservando el contexto en los límites.*

> **🤖 Prueba con [GitHub Copilot](https://github.com/features/copilot) Chat:** Abre [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) y pregunta:
> - "¿Cómo LangChain4j divide documentos en fragmentos y por qué es importante la superposición?"
> - "¿Cuál es el tamaño óptimo de fragmento para diferentes tipos de documentos y por qué?"
> - "¿Cómo manejar documentos en varios idiomas o con formatos especiales?"

### Creación de Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Cada fragmento se convierte en una representación numérica llamada embedding — esencialmente un convertidor de significado a números. El modelo de embeddings no es "inteligente" como un modelo de chat; no puede seguir instrucciones, razonar o responder directamente preguntas. Lo que puede hacer es mapear texto en un espacio matemático donde significados similares quedan cerca — "carro" cerca de "automóvil," "política de reembolso" cerca de "devolver mi dinero." Piensa en un modelo de chat como una persona con quien hablas; un modelo de embedding es un sistema de archivo ultra eficiente.

<img src="../../../translated_images/es/embedding-model-concept.90760790c336a705.webp" alt="Concepto de Modelo de Embedding" width="800"/>

*Este diagrama muestra cómo un modelo de embedding convierte texto en vectores numéricos, colocando significados similares — como "carro" y "automóvil" — cerca uno del otro en el espacio vectorial.*

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

El diagrama de clases a continuación muestra los dos flujos separados en un pipeline RAG y las clases de LangChain4j que los implementan. El **flujo de ingestión** (que corre una vez al subir) divide el documento, incrusta los fragmentos y los almacena vía `.addAll()`. El **flujo de consulta** (que corre cada vez que un usuario pregunta) incrusta la pregunta, busca en el almacén vía `.search()`, y pasa el contexto encontrado al modelo de chat. Ambos flujos se encuentran en la interfaz compartida `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/es/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="Clases RAG de LangChain4j" width="800"/>

*Este diagrama muestra los dos flujos en un pipeline RAG — ingestión y consulta — y cómo se conectan a través de un EmbeddingStore compartido.*

Una vez almacenados los embeddings, contenido similar naturalmente se agrupa en el espacio vectorial. La visualización a continuación muestra cómo documentos relacionados terminan como puntos cercanos, lo que hace posible la búsqueda semántica:

<img src="../../../translated_images/es/vector-embeddings.2ef7bdddac79a327.webp" alt="Espacio de Embeddings Vectoriales" width="800"/>

*Esta visualización muestra cómo documentos relacionados se agrupan en un espacio vectorial 3D, con temas como Documentos Técnicos, Reglas de Negocio y Preguntas Frecuentes formando grupos distintos.*

Cuando un usuario busca, el sistema sigue cuatro pasos: incrustar los documentos una vez, incrustar la consulta en cada búsqueda, comparar el vector de la consulta con todos los vectores almacenados usando similitud coseno, y devolver los fragmentos mejor calificados (top-K). El diagrama a continuación describe cada paso y las clases de LangChain4j involucradas:

<img src="../../../translated_images/es/embedding-search-steps.f54c907b3c5b4332.webp" alt="Pasos de Búsqueda con Embeddings" width="800"/>

*Este diagrama muestra el proceso de búsqueda con embeddings en cuatro pasos: incrustar documentos, incrustar la consulta, comparar vectores con similitud coseno y devolver los resultados top-K.*

### Búsqueda Semántica

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Cuando haces una pregunta, tu pregunta también se convierte en un embedding. El sistema compara ese embedding con los embeddings de todos los fragmentos del documento. Encuentra los fragmentos con los significados más similares — no sólo coincidencias de palabras clave, sino similitud semántica real.

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

El diagrama a continuación contrasta la búsqueda semántica con la búsqueda tradicional por palabras clave. Una búsqueda por palabra clave de "vehículo" pierde un fragmento sobre "autos y camiones," pero la búsqueda semántica entiende que significan lo mismo y lo devuelve como una coincidencia altamente calificada:

<img src="../../../translated_images/es/semantic-search.6b790f21c86b849d.webp" alt="Búsqueda Semántica" width="800"/>

*Este diagrama compara la búsqueda basada en palabras clave con la búsqueda semántica, mostrando cómo la búsqueda semántica recupera contenido conceptualmente relacionado aun cuando las palabras exactas difieren.*

En el fondo, la similitud se mide usando la similitud coseno — básicamente preguntando "¿apuntan estas dos flechas en la misma dirección?" Dos fragmentos pueden usar palabras completamente diferentes, pero si significan lo mismo sus vectores apuntan igual y obtienen una puntuación cercana a 1.0:

<img src="../../../translated_images/es/cosine-similarity.9baeaf3fc3336abb.webp" alt="Similitud Coseno" width="800"/>
*Este diagrama ilustra la similitud coseno como el ángulo entre vectores de incrustación — los vectores más alineados obtienen un puntaje cercano a 1.0, lo que indica mayor similitud semántica.*

> **🤖 Prueba con [GitHub Copilot](https://github.com/features/copilot) Chat:** Abre [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) y pregunta:
> - "¿Cómo funciona la búsqueda de similitud con embeddings y qué determina el puntaje?"
> - "¿Qué umbral de similitud debo usar y cómo afecta los resultados?"
> - "¿Cómo manejo casos en los que no se encuentran documentos relevantes?"

### Generación de Respuestas

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Los fragmentos más relevantes se ensamblan en un prompt estructurado que incluye instrucciones explícitas, el contexto recuperado y la pregunta del usuario. El modelo lee esos fragmentos específicos y responde basado en esa información — solo puede usar lo que tiene enfrente, lo que previene alucinaciones.

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

El diagrama a continuación muestra este ensamblaje en acción — los fragmentos con la puntuación más alta del paso de búsqueda se inyectan en la plantilla de prompt, y el `OpenAiOfficialChatModel` genera una respuesta fundamentada:

<img src="../../../translated_images/es/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Este diagrama muestra cómo los fragmentos con mayor puntaje se ensamblan en un prompt estructurado, permitiendo que el modelo genere una respuesta fundamentada a partir de tus datos.*

## Ejecutar la Aplicación

**Verificar despliegue:**

Asegúrate de que el archivo `.env` exista en el directorio raíz con credenciales de Azure (creado durante el Módulo 01):

**Bash:**
```bash
cat ../.env  # Debe mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Debe mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Iniciar la aplicación:**

> **Nota:** Si ya iniciaste todas las aplicaciones usando `./start-all.sh` desde el Módulo 01, este módulo ya está ejecutándose en el puerto 8081. Puedes omitir los comandos de inicio a continuación e ir directamente a http://localhost:8081.

**Opción 1: Usando Spring Boot Dashboard (Recomendado para usuarios de VS Code)**

El contenedor de desarrollo incluye la extensión Spring Boot Dashboard, que ofrece una interfaz visual para gestionar todas las aplicaciones Spring Boot. La puedes encontrar en la Barra de Actividades a la izquierda de VS Code (busca el icono de Spring Boot).

Desde Spring Boot Dashboard puedes:
- Ver todas las aplicaciones Spring Boot disponibles en el espacio de trabajo
- Iniciar/detener aplicaciones con un solo clic
- Ver los registros de la aplicación en tiempo real
- Monitorear el estado de las aplicaciones

Simplemente haz clic en el botón de reproducir junto a "rag" para iniciar este módulo, o inicia todos los módulos juntos.

<img src="../../../translated_images/es/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Esta captura muestra Spring Boot Dashboard en VS Code, donde puedes iniciar, detener y monitorear aplicaciones visualmente.*

**Opción 2: Usando scripts shell**

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

Ambos scripts cargan automáticamente variables de entorno desde el archivo `.env` raíz y construirán los JARs si no existen.

> **Nota:** Si prefieres construir todos los módulos manualmente antes de iniciar:
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

La aplicación provee una interfaz web para subir documentos y hacer preguntas.

<a href="images/rag-homepage.png"><img src="../../../translated_images/es/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Esta captura muestra la interfaz de la aplicación RAG donde subes documentos y haces preguntas.*

### Subir un Documento

Comienza subiendo un documento — los archivos TXT funcionan mejor para pruebas. En este directorio hay un `sample-document.txt` que contiene información sobre características de LangChain4j, la implementación RAG y mejores prácticas — perfecto para probar el sistema.

El sistema procesa tu documento, lo divide en fragmentos, y crea embeddings para cada fragmento. Esto sucede automáticamente al subirlo.

### Hacer Preguntas

Ahora haz preguntas específicas sobre el contenido del documento. Intenta algo factual que esté claro en el documento. El sistema busca fragmentos relevantes, los incluye en el prompt y genera una respuesta.

### Ver Referencias de Fuente

Observa que cada respuesta incluye referencias a las fuentes con puntajes de similitud. Estos puntajes (de 0 a 1) muestran qué tan relevante fue cada fragmento para tu pregunta. Puntajes más altos indican mejores coincidencias. Esto te permite verificar la respuesta con el material fuente.

<a href="images/rag-query-results.png"><img src="../../../translated_images/es/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Esta captura muestra resultados de consulta con la respuesta generada, referencias a fuente y puntajes de relevancia para cada fragmento recuperado.*

### Experimenta con Preguntas

Prueba diferentes tipos de preguntas:
- Hechos específicos: "¿Cuál es el tema principal?"
- Comparaciones: "¿Cuál es la diferencia entre X y Y?"
- Resúmenes: "Resume los puntos clave sobre Z"

Observa cómo cambian los puntajes de relevancia según qué tan bien tu pregunta coincida con el contenido del documento.

## Conceptos Clave

### Estrategia de Segmentación

Los documentos se dividen en fragmentos de 300 tokens con 30 tokens de solapamiento. Este balance asegura que cada fragmento tenga suficiente contexto para ser significativo pero lo suficientemente pequeño para incluir múltiples fragmentos en un prompt.

### Puntajes de Similitud

Cada fragmento recuperado viene con un puntaje de similitud entre 0 y 1 que indica qué tan estrechamente coincide con la pregunta del usuario. El diagrama a continuación visualiza los rangos de puntajes y cómo el sistema los usa para filtrar resultados:

<img src="../../../translated_images/es/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Este diagrama muestra rangos de puntaje de 0 a 1, con un umbral mínimo de 0.5 que filtra fragmentos irrelevantes.*

Los puntajes van de 0 a 1:
- 0.7-1.0: Altamente relevante, coincidencia exacta
- 0.5-0.7: Relevante, buen contexto
- Menor a 0.5: Filtrado, demasiado disímil

El sistema solo recupera fragmentos por encima del umbral mínimo para asegurar calidad.

Las incrustaciones funcionan bien cuando el significado forma clusters claros, pero tienen puntos ciegos. El diagrama a continuación muestra modos comunes de fallo — fragmentos demasiado grandes producen vectores confusos, fragmentos muy pequeños carecen de contexto, términos ambiguos apuntan a múltiples clusters, y consultas exactas (IDs, números de parte) no funcionan con embeddings en absoluto:

<img src="../../../translated_images/es/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Este diagrama muestra modos comunes de fallo en embeddings: fragmentos demasiado grandes, fragmentos demasiado pequeños, términos ambiguos que apuntan a múltiples clusters y búsquedas exactas como IDs.*

### Almacenamiento en Memoria

Este módulo usa almacenamiento en memoria por simplicidad. Al reiniciar la aplicación, se pierden los documentos subidos. Los sistemas en producción usan bases de datos vectoriales persistentes como Qdrant o Azure AI Search.

### Gestión de Ventana de Contexto

Cada modelo tiene una ventana máxima de contexto. No puedes incluir cada fragmento de un documento grande. El sistema recupera los N fragmentos más relevantes (por defecto 5) para mantenerse dentro de los límites y proveer suficiente contexto para respuestas precisas.

## Cuándo RAG es Importante

RAG no siempre es el enfoque correcto. La guía de decisión a continuación te ayuda a determinar cuándo RAG aporta valor versus cuándo enfoques más simples — como incluir contenido directamente en el prompt o confiar en el conocimiento incorporado del modelo — son suficientes:

<img src="../../../translated_images/es/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Este diagrama muestra una guía de decisión para cuándo RAG aporta valor frente a cuándo enfoques más simples son suficientes.*

**Usa RAG cuando:**
- Respondas preguntas sobre documentos propietarios
- La información cambia frecuentemente (políticas, precios, especificaciones)
- Se requiere precisión con atribución de fuentes
- El contenido es demasiado grande para caber en un solo prompt
- Necesitas respuestas verificables y fundamentadas

**No uses RAG cuando:**
- Las preguntas requieren conocimiento general que el modelo ya tiene
- Se necesita información en tiempo real (RAG funciona con documentos subidos)
- El contenido es suficientemente pequeño para incluirse directamente en prompts

## Próximos Pasos

**Próximo Módulo:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Navegación:** [← Anterior: Módulo 02 - Ingeniería de Prompt](../02-prompt-engineering/README.md) | [Volver al Inicio](../README.md) | [Siguiente: Módulo 04 - Herramientas →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso legal**:
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por la exactitud, tenga en cuenta que las traducciones automáticas pueden contener errores o inexactitudes. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda una traducción profesional realizada por un humano. No nos hacemos responsables de malentendidos o interpretaciones erróneas derivadas del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->