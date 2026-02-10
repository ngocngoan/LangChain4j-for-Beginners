# Glosario de LangChain4j

## Tabla de Contenidos

- [Conceptos Básicos](../../../docs)
- [Componentes de LangChain4j](../../../docs)
- [Conceptos de IA/ML](../../../docs)
- [Guardrails](../../../docs)
- [Ingeniería de Prompts](../../../docs)
- [RAG (Generación Aumentada con Recuperación)](../../../docs)
- [Agentes y Herramientas](../../../docs)
- [Módulo Agéntico](../../../docs)
- [Protocolo de Contexto de Modelo (MCP)](../../../docs)
- [Servicios de Azure](../../../docs)
- [Pruebas y Desarrollo](../../../docs)

Referencia rápida para términos y conceptos usados a lo largo del curso.

## Conceptos Básicos

**Agente de IA** - Sistema que usa IA para razonar y actuar autónomamente. [Módulo 04](../04-tools/README.md)

**Cadena** - Secuencia de operaciones donde la salida alimenta el siguiente paso.

**Fragmentación** - Dividir documentos en piezas más pequeñas. Típico: 300-500 tokens con solapamiento. [Módulo 03](../03-rag/README.md)

**Ventana de Contexto** - Máximo tokens que un modelo puede procesar. GPT-5.2: 400K tokens.

**Embeddings** - Vectores numéricos que representan el significado del texto. [Módulo 03](../03-rag/README.md)

**Llamada a Función** - El modelo genera solicitudes estructuradas para llamar funciones externas. [Módulo 04](../04-tools/README.md)

**Alucinación** - Cuando los modelos generan información incorrecta pero plausible.

**Prompt** - Entrada de texto a un modelo de lenguaje. [Módulo 02](../02-prompt-engineering/README.md)

**Búsqueda Semántica** - Buscar por significado usando embeddings, no por palabras clave. [Módulo 03](../03-rag/README.md)

**Con Estado vs Sin Estado** - Sin estado: sin memoria. Con estado: mantiene historial de conversación. [Módulo 01](../01-introduction/README.md)

**Tokens** - Unidades básicas de texto que los modelos procesan. Afecta costos y límites. [Módulo 01](../01-introduction/README.md)

**Encadenamiento de Herramientas** - Ejecución secuencial de herramientas donde la salida informa la siguiente llamada. [Módulo 04](../04-tools/README.md)

## Componentes de LangChain4j

**AiServices** - Crea interfaces de servicios de IA con tipado seguro.

**OpenAiOfficialChatModel** - Cliente unificado para modelos de OpenAI y Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Crea embeddings usando el cliente Oficial de OpenAI (soporta tanto OpenAI como Azure OpenAI).

**ChatModel** - Interfaz principal para modelos de lenguaje.

**ChatMemory** - Mantiene historial de conversación.

**ContentRetriever** - Encuentra fragmentos relevantes de documentos para RAG.

**DocumentSplitter** - Divide documentos en fragmentos.

**EmbeddingModel** - Convierte texto en vectores numéricos.

**EmbeddingStore** - Almacena y recupera embeddings.

**MessageWindowChatMemory** - Mantiene una ventana deslizante de mensajes recientes.

**PromptTemplate** - Crea prompts reutilizables con marcadores `{{variable}}`.

**TextSegment** - Fragmento de texto con metadatos. Usado en RAG.

**ToolExecutionRequest** - Representa una solicitud de ejecución de herramienta.

**UserMessage / AiMessage / SystemMessage** - Tipos de mensajes de conversación.

## Conceptos de IA/ML

**Aprendizaje de Pocos Ejemplos (Few-Shot Learning)** - Proveer ejemplos en prompts. [Módulo 02](../02-prompt-engineering/README.md)

**Modelo de Lenguaje Grande (LLM)** - Modelos de IA entrenados con grandes cantidades de texto.

**Esfuerzo de Razonamiento** - Parámetro de GPT-5.2 que controla la profundidad del pensamiento. [Módulo 02](../02-prompt-engineering/README.md)

**Temperatura** - Controla la aleatoriedad de la salida. Bajo=determinista, alto=creativo.

**Base de Datos Vectorial** - Base especializada para embeddings. [Módulo 03](../03-rag/README.md)

**Aprendizaje de Cero Ejemplos (Zero-Shot Learning)** - Realizar tareas sin ejemplos. [Módulo 02](../02-prompt-engineering/README.md)

## Guardrails - [Módulo 00](../00-quick-start/README.md)

**Defensa en Profundidad** - Enfoque de seguridad multinivel que combina guardrails a nivel de aplicación con filtros de seguridad del proveedor.

**Bloqueo Duro** - El proveedor lanza error HTTP 400 por violaciones graves de contenido.

**InputGuardrail** - Interfaz de LangChain4j para validar la entrada del usuario antes de llegar al LLM. Ahorra costo y latencia bloqueando prompts dañinos tempranamente.

**InputGuardrailResult** - Tipo de retorno para validación de guardrail: `success()` o `fatal("razón")`.

**OutputGuardrail** - Interfaz para validar respuestas de IA antes de devolverlas a los usuarios.

**Filtros de Seguridad del Proveedor** - Filtros de contenido integrados de proveedores de IA (e.g., GitHub Models) que detectan violaciones a nivel de API.

**Rechazo Suave** - El modelo declina educadamente responder sin lanzar error.

## Ingeniería de Prompts - [Módulo 02](../02-prompt-engineering/README.md)

**Cadena de Pensamiento (Chain-of-Thought)** - Razonamiento paso a paso para mayor precisión.

**Salida Restringida** - Aplicar formato o estructura específicos.

**Alta Disposición (High Eagerness)** - Patrón GPT-5.2 para razonamiento exhaustivo.

**Baja Disposición (Low Eagerness)** - Patrón GPT-5.2 para respuestas rápidas.

**Conversación Multiturno** - Mantener contexto entre intercambios.

**Prompting Basado en Roles** - Configurar la persona del modelo mediante mensajes del sistema.

**Autorreflexión** - El modelo evalúa y mejora su salida.

**Análisis Estructurado** - Marco de evaluación fijo.

**Patrón de Ejecución de Tareas** - Planificar → Ejecutar → Resumir.

## RAG (Generación Aumentada con Recuperación) - [Módulo 03](../03-rag/README.md)

**Pipeline de Procesamiento de Documentos** - Cargar → fragmentar → embeber → almacenar.

**Almacenamiento de Embeddings en Memoria** - Almacenamiento no persistente para pruebas.

**RAG** - Combina recuperación con generación para fundamentar respuestas.

**Puntaje de Similitud** - Medida (0-1) de similitud semántica.

**Referencia de Fuente** - Metadatos sobre contenido recuperado.

## Agentes y Herramientas - [Módulo 04](../04-tools/README.md)

**@Tool Annotation** - Marca métodos Java como herramientas accesibles por IA.

**Patrón ReAct** - Razonar → Actuar → Observar → Repetir.

**Gestión de Sesiones** - Contextos separados para diferentes usuarios.

**Herramienta (Tool)** - Función que un agente de IA puede llamar.

**Descripción de Herramienta** - Documentación del propósito y parámetros de la herramienta.

## Módulo Agéntico - [Módulo 05](../05-mcp/README.md)

**@Agent Annotation** - Marca interfaces como agentes de IA con definición declarativa de comportamiento.

**Agent Listener** - Hook para monitorear la ejecución del agente vía `beforeAgentInvocation()` y `afterAgentInvocation()`.

**Ámbito Agéntico** - Memoria compartida donde agentes almacenan salidas usando `outputKey` para que agentes siguientes las consuman.

**AgenticServices** - Fábrica para crear agentes usando `agentBuilder()` y `supervisorBuilder()`.

**Flujo de Trabajo Condicional** - Ruta basada en condiciones hacia diferentes agentes especialistas.

**Human-in-the-Loop** - Patrón de flujo de trabajo que agrega puntos de control humanos para aprobación o revisión de contenido.

**langchain4j-agentic** - Dependencia Maven para construcción declarativa de agentes (experimental).

**Flujo de Trabajo en Bucle** - Iterar ejecución de agente hasta que se cumpla una condición (e.g., puntaje de calidad ≥ 0.8).

**outputKey** - Parámetro de anotación de agente que especifica dónde se almacenan los resultados en Ámbito Agéntico.

**Flujo de Trabajo Paralelo** - Ejecutar varios agentes simultáneamente para tareas independientes.

**Estrategia de Respuesta** - Cómo el supervisor formula la respuesta final: LAST, SUMMARY o SCORED.

**Flujo de Trabajo Secuencial** - Ejecutar agentes en orden donde la salida fluye al siguiente paso.

**Patrón de Agente Supervisor** - Patrón ágéntico avanzado donde un LLM supervisor decide dinámicamente qué sub-agentes invocar.

## Protocolo de Contexto de Modelo (MCP) - [Módulo 05](../05-mcp/README.md)

**langchain4j-mcp** - Dependencia Maven para integración MCP en LangChain4j.

**MCP** - Protocolo de Contexto de Modelo: estándar para conectar aplicaciones de IA a herramientas externas. Construir una vez, usar en todas partes.

**Cliente MCP** - Aplicación que se conecta a servidores MCP para descubrir y usar herramientas.

**Servidor MCP** - Servicio que expone herramientas vía MCP con descripciones claras y esquemas de parámetros.

**McpToolProvider** - Componente de LangChain4j que envuelve herramientas MCP para uso en servicios y agentes de IA.

**McpTransport** - Interfaz para comunicación MCP. Implementaciones incluyen Stdio y HTTP.

**Transporte Stdio** - Transporte de proceso local vía stdin/stdout. Útil para acceso a sistema de archivos o herramientas de línea de comando.

**StdioMcpTransport** - Implementación de LangChain4j que lanza servidor MCP como subproceso.

**Descubrimiento de Herramientas** - Cliente consulta al servidor por herramientas disponibles con descripciones y esquemas.

## Servicios de Azure - [Módulo 01](../01-introduction/README.md)

**Azure AI Search** - Búsqueda en la nube con capacidades vectoriales. [Módulo 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Despliega recursos de Azure.

**Azure OpenAI** - Servicio de IA empresarial de Microsoft.

**Bicep** - Lenguaje de infraestructura como código de Azure. [Guía de Infraestructura](../01-introduction/infra/README.md)

**Nombre de Despliegue** - Nombre para despliegue de modelo en Azure.

**GPT-5.2** - Último modelo de OpenAI con control de razonamiento. [Módulo 02](../02-prompt-engineering/README.md)

## Pruebas y Desarrollo - [Guía de Pruebas](TESTING.md)

**Dev Container** - Entorno de desarrollo en contenedor. [Configuración](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Playground gratuito de modelos de IA. [Módulo 00](../00-quick-start/README.md)

**Pruebas en Memoria** - Pruebas con almacenamiento en memoria.

**Pruebas de Integración** - Pruebas con infraestructura real.

**Maven** - Herramienta de automatización de construcción para Java.

**Mockito** - Marco de trabajo para mocks en Java.

**Spring Boot** - Framework de aplicaciones Java. [Módulo 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento ha sido traducido utilizando el servicio de traducción por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por lograr precisión, tenga en cuenta que las traducciones automáticas pueden contener errores o imprecisiones. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda una traducción profesional realizada por humanos. No nos hacemos responsables de malentendidos o interpretaciones erróneas derivadas del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->