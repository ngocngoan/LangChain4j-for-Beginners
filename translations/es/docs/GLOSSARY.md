# Glosario de LangChain4j

## Tabla de Contenidos

- [Conceptos Básicos](../../../docs)
- [Componentes de LangChain4j](../../../docs)
- [Conceptos de IA/ML](../../../docs)
- [Guardrails](../../../docs)
- [Ingeniería de Prompts](../../../docs)
- [RAG (Generación Aumentada con Recuperación)](../../../docs)
- [Agentes y Herramientas](../../../docs)
- [Módulo Agente](../../../docs)
- [Protocolo de Contexto de Modelo (MCP)](../../../docs)
- [Servicios de Azure](../../../docs)
- [Pruebas y Desarrollo](../../../docs)

Referencia rápida de términos y conceptos utilizados en todo el curso.

## Conceptos Básicos

**Agente de IA** - Sistema que usa IA para razonar y actuar de forma autónoma. [Módulo 04](../04-tools/README.md)

**Cadena** - Secuencia de operaciones donde la salida alimenta el siguiente paso.

**Fragmentación** - Dividir documentos en partes más pequeñas. Típico: 300-500 tokens con solapamiento. [Módulo 03](../03-rag/README.md)

**Ventana de Contexto** - Máximo de tokens que un modelo puede procesar. GPT-5.2: 400K tokens (hasta 272K entrada, 128K salida).

**Embeddings** - Vectores numéricos que representan el significado del texto. [Módulo 03](../03-rag/README.md)

**Llamada de Función** - Modelo genera solicitudes estructuradas para llamar a funciones externas. [Módulo 04](../04-tools/README.md)

**Alucinación** - Cuando los modelos generan información incorrecta pero plausible.

**Prompt** - Entrada de texto para un modelo de lenguaje. [Módulo 02](../02-prompt-engineering/README.md)

**Búsqueda Semántica** - Búsqueda por significado usando embeddings, no por palabras clave. [Módulo 03](../03-rag/README.md)

**Con estado vs Sin estado** - Sin estado: sin memoria. Con estado: mantiene historial de conversación. [Módulo 01](../01-introduction/README.md)

**Tokens** - Unidades básicas de texto que los modelos procesan. Afecta costos y límites. [Módulo 01](../01-introduction/README.md)

**Encadenamiento de Herramientas** - Ejecución secuencial de herramientas donde la salida informa la siguiente llamada. [Módulo 04](../04-tools/README.md)

## Componentes de LangChain4j

**AiServices** - Crea interfaces de servicio AI con tipado seguro.

**OpenAiOfficialChatModel** - Cliente unificado para modelos OpenAI y Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Crea embeddings usando el cliente oficial de OpenAI (soporta OpenAI y Azure OpenAI).

**ChatModel** - Interfaz principal para modelos de lenguaje.

**ChatMemory** - Mantiene el historial de conversación.

**ContentRetriever** - Encuentra fragmentos relevantes de documentos para RAG.

**DocumentSplitter** - Divide documentos en fragmentos.

**EmbeddingModel** - Convierte texto en vectores numéricos.

**EmbeddingStore** - Almacena y recupera embeddings.

**MessageWindowChatMemory** - Mantiene ventana deslizante de mensajes recientes.

**PromptTemplate** - Crea prompts reutilizables con marcadores `{{variable}}`.

**TextSegment** - Fragmento de texto con metadatos. Usado en RAG.

**ToolExecutionRequest** - Representa la solicitud de ejecución de una herramienta.

**UserMessage / AiMessage / SystemMessage** - Tipos de mensajes en conversación.

## Conceptos de IA/ML

**Aprendizaje con pocos ejemplos** - Proveer ejemplos en los prompts. [Módulo 02](../02-prompt-engineering/README.md)

**Modelo de Lenguaje Grande (LLM)** - Modelos AI entrenados con grandes cantidades de texto.

**Esfuerzo de Razonamiento** - Parámetro GPT-5.2 que controla la profundidad del razonamiento. [Módulo 02](../02-prompt-engineering/README.md)

**Temperatura** - Controla la aleatoriedad de la salida. Bajo=determinista, alto=creativo.

**Base de Datos Vectorial** - Base de datos especializada para embeddings. [Módulo 03](../03-rag/README.md)

**Aprendizaje sin ejemplos** - Realizar tareas sin ejemplos previos. [Módulo 02](../02-prompt-engineering/README.md)

## Guardrails - [Módulo 00](../00-quick-start/README.md)

**Defensa en Profundidad** - Enfoque de seguridad multicapa que combina guardrails a nivel de aplicación con filtros de seguridad del proveedor.

**Bloqueo Duro** - El proveedor lanza error HTTP 400 por violaciones graves de contenido.

**InputGuardrail** - Interfaz de LangChain4j para validar la entrada del usuario antes de que llegue al LLM. Ahorra costo y latencia bloqueando prompts dañinos de forma temprana.

**InputGuardrailResult** - Tipo de retorno para validación guardrail: `success()` o `fatal("razón")`.

**OutputGuardrail** - Interfaz para validar respuestas AI antes de devolverlas a usuarios.

**Filtros de Seguridad del Proveedor** - Filtros de contenido incorporados de proveedores AI (p. ej., GitHub Models) que detectan violaciones a nivel API.

**Rechazo Suave** - El modelo se niega educadamente a responder sin lanzar error.

## Ingeniería de Prompts - [Módulo 02](../02-prompt-engineering/README.md)

**Cadena de Pensamiento** - Razonamiento paso a paso para mejor precisión.

**Salida Restringida** - Imposición de formato o estructura específica.

**Alta Disposición** - Patrón GPT-5.2 para razonamiento minucioso.

**Baja Disposición** - Patrón GPT-5.2 para respuestas rápidas.

**Conversación Multi-Turno** - Mantener contexto a lo largo de intercambios.

**Prompts Basados en Rol** - Establecer persona del modelo vía mensajes del sistema.

**Autorreflexión** - El modelo evalúa y mejora su propia salida.

**Análisis Estructurado** - Marco fijo de evaluación.

**Patrón de Ejecución de Tareas** - Planificar → Ejecutar → Resumir.

## RAG (Generación Aumentada con Recuperación) - [Módulo 03](../03-rag/README.md)

**Pipeline de Procesamiento de Documentos** - Cargar → fragmentar → embeber → almacenar.

**Almacenamiento de Embeddings en Memoria** - Almacenamiento no persistente para pruebas.

**RAG** - Combina recuperación con generación para fundamentar respuestas.

**Puntuación de Similitud** - Medida (0-1) de similitud semántica.

**Referencia de Fuente** - Metadatos sobre contenido recuperado.

## Agentes y Herramientas - [Módulo 04](../04-tools/README.md)

**Anotación @Tool** - Marca métodos Java como herramientas accesibles para IA.

**Patrón ReAct** - Razonar → Actuar → Observar → Repetir.

**Gestión de Sesiones** - Contextos separados para usuarios diferentes.

**Herramienta** - Función que un agente de IA puede llamar.

**Descripción de Herramienta** - Documentación del propósito y parámetros de la herramienta.

## Módulo Agente - [Módulo 05](../05-mcp/README.md)

**Anotación @Agent** - Marca interfaces como agentes AI con definición declarativa de comportamiento.

**Agent Listener** - Hook para monitorear ejecución del agente vía `beforeAgentInvocation()` y `afterAgentInvocation()`.

**Alcance Agente** - Memoria compartida donde agentes almacenan salidas usando `outputKey` para que otros agentes las consuman.

**AgenticServices** - Factoría para crear agentes usando `agentBuilder()` y `supervisorBuilder()`.

**Flujo Condicional** - Ruta basada en condiciones hacia diferentes agentes especialistas.

**Humano en el Bucle** - Patrón de flujo que añade puntos de aprobación o revisión de contenido humana.

**langchain4j-agentic** - Dependencia Maven para construcción declarativa de agentes (experimental).

**Flujo en Bucle** - Iterar ejecución de agente hasta que se cumpla condición (p. ej., puntuación de calidad ≥ 0.8).

**outputKey** - Parámetro de anotación de agente que especifica dónde se guardan resultados en Alcance Agente.

**Flujo Paralelo** - Ejecutar múltiples agentes simultáneamente para tareas independientes.

**Estrategia de Respuesta** - Cómo el supervisor formula la respuesta final: LAST, SUMMARY o SCORED.

**Flujo Secuencial** - Ejecutar agentes en orden donde la salida fluye al siguiente paso.

**Patrón de Agente Supervisor** - Patrón agente avanzado donde un LLM supervisor decide dinámicamente qué sub-agentes invocar.

## Protocolo de Contexto de Modelo (MCP) - [Módulo 05](../05-mcp/README.md)

**langchain4j-mcp** - Dependencia Maven para integración MCP en LangChain4j.

**MCP** - Protocolo de Contexto de Modelo: estándar para conectar apps AI con herramientas externas. Construir una vez, usar en todas partes.

**Cliente MCP** - Aplicación que se conecta a servidores MCP para descubrir y usar herramientas.

**Servidor MCP** - Servicio que expone herramientas vía MCP con descripciones claras y esquemas de parámetros.

**McpToolProvider** - Componente LangChain4j que envuelve herramientas MCP para uso en servicios AI y agentes.

**McpTransport** - Interfaz para comunicación MCP. Implementaciones incluyen Stdio y HTTP.

**Transporte Stdio** - Transporte de proceso local vía stdin/stdout. Útil para acceso a sistema de archivos o herramientas línea de comandos.

**StdioMcpTransport** - Implementación LangChain4j que lanza servidor MCP como subproceso.

**Descubrimiento de Herramientas** - Cliente consulta al servidor por herramientas disponibles con descripciones y esquemas.

## Servicios de Azure - [Módulo 01](../01-introduction/README.md)

**Azure AI Search** - Búsqueda en la nube con capacidades vectoriales. [Módulo 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Despliega recursos de Azure.

**Azure OpenAI** - Servicio AI empresarial de Microsoft.

**Bicep** - Lenguaje de infraestructura como código para Azure. [Guía de Infraestructura](../01-introduction/infra/README.md)

**Nombre de Despliegue** - Nombre para despliegue de modelo en Azure.

**GPT-5.2** - Último modelo OpenAI con control de razonamiento. [Módulo 02](../02-prompt-engineering/README.md)

## Pruebas y Desarrollo - [Guía de Pruebas](TESTING.md)

**Dev Container** - Entorno de desarrollo conteinerizado. [Configuración](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Área de pruebas AI gratuita. [Módulo 00](../00-quick-start/README.md)

**Pruebas en Memoria** - Pruebas con almacenamiento en memoria.

**Pruebas de Integración** - Pruebas con infraestructura real.

**Maven** - Herramienta de automatización de construcción Java.

**Mockito** - Framework Java para mocks.

**Spring Boot** - Framework de aplicaciones Java. [Módulo 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso legal**:
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por la precisión, tenga en cuenta que las traducciones automáticas pueden contener errores o inexactitudes. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda la traducción profesional realizada por humanos. No nos hacemos responsables de cualquier malentendido o interpretación errónea derivada del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->