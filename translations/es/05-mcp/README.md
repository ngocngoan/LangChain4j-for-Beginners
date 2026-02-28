# Module 05: Protocolo de Contexto del Modelo (MCP)

## Tabla de Contenidos

- [Lo que Aprenderás](../../../05-mcp)
- [¿Qué es MCP?](../../../05-mcp)
- [Cómo Funciona MCP](../../../05-mcp)
- [El Módulo Agente](../../../05-mcp)
- [Ejecutando los Ejemplos](../../../05-mcp)
  - [Requisitos Previos](../../../05-mcp)
- [Inicio Rápido](../../../05-mcp)
  - [Operaciones con Archivos (Stdio)](../../../05-mcp)
  - [Agente Supervisor](../../../05-mcp)
    - [Ejecutando la Demostración](../../../05-mcp)
    - [Cómo Funciona el Supervisor](../../../05-mcp)
    - [Estrategias de Respuesta](../../../05-mcp)
    - [Comprendiendo la Salida](../../../05-mcp)
    - [Explicación de las Funciones del Módulo Agente](../../../05-mcp)
- [Conceptos Clave](../../../05-mcp)
- [¡Felicidades!](../../../05-mcp)
  - [¿Qué Sigue?](../../../05-mcp)

## Lo que Aprenderás

Has construido IA conversacional, dominado los prompts, fundamentado respuestas en documentos y creado agentes con herramientas. Pero todas esas herramientas fueron diseñadas a medida para tu aplicación específica. ¿Y si pudieras darle a tu IA acceso a un ecosistema estandarizado de herramientas que cualquiera pueda crear y compartir? En este módulo, aprenderás justamente eso con el Protocolo de Contexto del Modelo (MCP) y el módulo agente de LangChain4j. Primero mostramos un lector de archivos MCP sencillo y luego cómo se integra fácilmente en flujos avanzados agenticos usando el patrón Agente Supervisor.

## ¿Qué es MCP?

El Protocolo de Contexto del Modelo (MCP) ofrece exactamente eso: una manera estándar para que las aplicaciones de IA descubran y usen herramientas externas. En lugar de escribir integraciones personalizadas para cada fuente de datos o servicio, te conectas a servidores MCP que exponen sus capacidades en un formato consistente. Tu agente IA puede entonces descubrir y usar esas herramientas automáticamente.

<img src="../../../translated_images/es/mcp-comparison.9129a881ecf10ff5.webp" alt="Comparación MCP" width="800"/>

*Antes de MCP: integraciones complejas punto a punto. Después de MCP: un protocolo, posibilidades infinitas.*

MCP resuelve un problema fundamental en el desarrollo de IA: cada integración es personalizada. ¿Quieres acceder a GitHub? Código personalizado. ¿Quieres leer archivos? Código personalizado. ¿Quieres consultar una base de datos? Código personalizado. Y ninguna de estas integraciones funciona con otras aplicaciones de IA.

MCP estandariza esto. Un servidor MCP expone herramientas con descripciones claras y esquemas. Cualquier cliente MCP puede conectarse, descubrir herramientas disponibles y usarlas. Construye una vez, usa en todas partes.

<img src="../../../translated_images/es/mcp-architecture.b3156d787a4ceac9.webp" alt="Arquitectura MCP" width="800"/>

*Arquitectura del Protocolo de Contexto del Modelo - descubrimiento y ejecución estandarizados de herramientas*

## Cómo Funciona MCP

<img src="../../../translated_images/es/mcp-protocol-detail.01204e056f45308b.webp" alt="Detalle Protocolo MCP" width="800"/>

*Cómo funciona MCP internamente: los clientes descubren herramientas, intercambian mensajes JSON-RPC, y ejecutan operaciones a través de una capa de transporte.*

**Arquitectura Cliente-Servidor**

MCP utiliza un modelo cliente-servidor. Los servidores proveen herramientas — lectura de archivos, consultas a bases de datos, llamadas a APIs. Los clientes (tu aplicación IA) se conectan a servidores y usan sus herramientas.

Para usar MCP con LangChain4j, añade esta dependencia Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Descubrimiento de Herramientas**

Cuando tu cliente se conecta a un servidor MCP, pregunta "¿Qué herramientas tienes?" El servidor responde con una lista de herramientas disponibles, cada una con descripciones y esquemas de parámetros. Tu agente IA puede entonces decidir qué herramientas usar según las solicitudes del usuario.

<img src="../../../translated_images/es/tool-discovery.07760a8a301a7832.webp" alt="Descubrimiento de Herramientas MCP" width="800"/>

*La IA descubre las herramientas disponibles al inicio — ahora sabe qué capacidades están disponibles y puede decidir cuáles usar.*

**Mecanismos de Transporte**

MCP soporta diferentes mecanismos de transporte. Este módulo muestra el transporte Stdio para procesos locales:

<img src="../../../translated_images/es/transport-mechanisms.2791ba7ee93cf020.webp" alt="Mecanismos de Transporte" width="800"/>

*Mecanismos de transporte MCP: HTTP para servidores remotos, Stdio para procesos locales*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Para procesos locales. Tu aplicación inicia un servidor como subproceso y comunica vía entrada/salida estándar. Útil para acceso al sistema de archivos o herramientas de línea de comandos.

```java
McpTransport stdioTransport = new StdioMcpTransport.Builder()
    .command(List.of(
        npmCmd, "exec",
        "@modelcontextprotocol/server-filesystem@2025.12.18",
        resourcesDir
    ))
    .logEvents(false)
    .build();
```

<img src="../../../translated_images/es/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Flujo Transporte Stdio" width="800"/>

*Transporte Stdio en acción — tu aplicación inicia el servidor MCP como proceso hijo y comunica vía pipes stdin/stdout.*

> **🤖 Prueba con el Chat de [GitHub Copilot](https://github.com/features/copilot):** Abre [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) y pregunta:
> - "¿Cómo funciona el transporte Stdio y cuándo debo usarlo en lugar de HTTP?"
> - "¿Cómo gestiona LangChain4j el ciclo de vida de los procesos de servidor MCP iniciados?"
> - "¿Cuáles son las implicaciones de seguridad de dar acceso a la IA al sistema de archivos?"

## El Módulo Agente

Mientras que MCP provee herramientas estandarizadas, el **módulo agente** de LangChain4j ofrece una forma declarativa de construir agentes que orquestan esas herramientas. La anotación `@Agent` y `AgenticServices` te permiten definir el comportamiento del agente mediante interfaces en lugar de código imperativo.

En este módulo, explorarás el patrón **Agente Supervisor** — un enfoque avanzado agente donde un agente "supervisor" decide dinámicamente qué sub-agentes invocar según la solicitud del usuario. Combinaremos ambos conceptos dando a uno de nuestros sub-agentes capacidades MCP para acceso a archivos.

Para usar el módulo agente, añade esta dependencia Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ Experimental:** El módulo `langchain4j-agentic` es **experimental** y sujeto a cambios. La forma estable de construir asistentes IA sigue siendo `langchain4j-core` con herramientas personalizadas (Módulo 04).

## Ejecutando los Ejemplos

### Requisitos Previos

- Java 21+, Maven 3.9+
- Node.js 16+ y npm (para servidores MCP)
- Variables de entorno configuradas en el archivo `.env` (desde el directorio raíz):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (igual que en Módulos 01-04)

> **Nota:** Si aún no configuraste tus variables de entorno, consulta [Módulo 00 - Inicio Rápido](../00-quick-start/README.md) para instrucciones, o copia `.env.example` a `.env` en el directorio raíz y completa tus valores.

## Inicio Rápido

**Usando VS Code:** Simplemente haz clic derecho sobre cualquier archivo demo en el Explorador y selecciona **"Run Java"**, o usa las configuraciones de lanzamiento desde el panel Ejecutar y Depurar (asegúrate de haber añadido tu token en el archivo `.env` primero).

**Usando Maven:** Alternativamente, puedes ejecutar desde línea de comandos con los ejemplos a continuación.

### Operaciones con Archivos (Stdio)

Esto demuestra herramientas basadas en subprocesos locales.

**✅ No se requieren prerequisitos** - el servidor MCP se inicia automáticamente.

**Usando los Scripts de Inicio (Recomendado):**

Los scripts de inicio cargan automáticamente las variables de entorno desde el archivo `.env` en la raíz:

**Bash:**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**Usando VS Code:** Haz clic derecho en `StdioTransportDemo.java` y selecciona **"Run Java"** (asegúrate que tu archivo `.env` esté configurado).

La aplicación inicia automáticamente un servidor MCP de sistema de archivos y lee un archivo local. Observa cómo se maneja la gestión del subproceso por ti.

**Salida esperada:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agente Supervisor

El **patrón Agente Supervisor** es una forma **flexible** de IA agentica. Un Supervisor usa un LLM para decidir autónomamente qué agentes invocar según la petición del usuario. En el siguiente ejemplo combinamos acceso a archivos potenciado por MCP con un agente LLM para crear un flujo supervisado de lectura de archivo → reporte.

En la demo, `FileAgent` lee un archivo usando herramientas MCP de sistema de archivos, y `ReportAgent` genera un reporte estructurado con un resumen ejecutivo (1 frase), 3 puntos clave, y recomendaciones. El Supervisor orquesta este flujo automáticamente:

<img src="../../../translated_images/es/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Patrón de Agente Supervisor" width="800"/>

*El Supervisor usa su LLM para decidir qué agentes invocar y en qué orden — no se necesita enrutamiento codificado.*

Así es como luce el flujo concreto para nuestra tubería de archivo a reporte:

<img src="../../../translated_images/es/file-report-workflow.649bb7a896800de9.webp" alt="Flujo de Archivo a Reporte" width="800"/>

*FileAgent lee el archivo vía herramientas MCP, luego ReportAgent transforma el contenido bruto en un reporte estructurado.*

Cada agente guarda su salida en el **Ámbito Agente** (memoria compartida), permitiendo que agentes posteriores accedan a resultados previos. Esto demuestra cómo las herramientas MCP se integran sin problemas en flujos agenticos — el Supervisor no necesita saber *cómo* se leen los archivos, solo que `FileAgent` puede hacerlo.

#### Ejecutando la Demostración

Los scripts de inicio cargan automáticamente variables de entorno desde el archivo `.env` en la raíz:

**Bash:**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**Usando VS Code:** Haz clic derecho en `SupervisorAgentDemo.java` y selecciona **"Run Java"** (asegúrate que tu archivo `.env` esté configurado).

#### Cómo Funciona el Supervisor

```java
// Paso 1: FileAgent lee archivos usando herramientas MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Tiene herramientas MCP para operaciones de archivos
        .build();

// Paso 2: ReportAgent genera informes estructurados
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// El Supervisor orquesta el flujo de trabajo archivo → informe
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Devuelve el informe final
        .build();

// El Supervisor decide qué agentes invocar según la solicitud
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Estrategias de Respuesta

Cuando configuras un `SupervisorAgent`, especificas cómo debe formular su respuesta final al usuario después de que los sub-agentes completan sus tareas.

<img src="../../../translated_images/es/response-strategies.3d0cea19d096bdf9.webp" alt="Estrategias de Respuesta" width="800"/>

*Tres estrategias para cómo el Supervisor formula su respuesta final — elige según si quieres la salida del último agente, un resumen sintetizado o la opción con mejor puntuación.*

Las estrategias disponibles son:

| Estrategia | Descripción |
|------------|-------------|
| **LAST** | El supervisor devuelve la salida del último sub-agente o herramienta llamada. Esto es útil cuando el agente final en el flujo está diseñado específicamente para producir la respuesta final completa (por ejemplo, un "Agente Resumen" en una tubería de investigación). |
| **SUMMARY** | El supervisor usa su propio Modelo de Lenguaje interno (LLM) para sintetizar un resumen de toda la interacción y todas las salidas de sub-agentes, luego devuelve ese resumen como respuesta final. Esto provee una respuesta agregada y limpia al usuario. |
| **SCORED** | El sistema usa un LLM interno para puntuar tanto la respuesta LAST como el SUMMARY de la interacción respecto a la solicitud original del usuario, devolviendo la salida que reciba la puntuación más alta. |

Consulta [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) para la implementación completa.

> **🤖 Prueba con el Chat de [GitHub Copilot](https://github.com/features/copilot):** Abre [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) y pregunta:
> - "¿Cómo decide el Supervisor qué agentes invocar?"
> - "¿Cuál es la diferencia entre el Supervisor y los patrones de flujo Secuencial?"
> - "¿Cómo puedo personalizar el comportamiento de planificación del Supervisor?"

#### Comprendiendo la Salida

Cuando ejecutes la demo, verás un recorrido estructurado de cómo el Supervisor orquesta múltiples agentes. Esto es lo que significa cada sección:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**El encabezado** introduce el concepto del flujo: una tubería enfocada de lectura de archivos a generación de reporte.

```
--- WORKFLOW ---------------------------------------------------------
  ┌─────────────┐      ┌──────────────┐
  │  FileAgent  │ ───▶ │ ReportAgent  │
  │ (MCP tools) │      │  (pure LLM)  │
  └─────────────┘      └──────────────┘
   outputKey:           outputKey:
   'fileContent'        'report'

--- AVAILABLE AGENTS -------------------------------------------------
  [FILE]   FileAgent   - Reads files via MCP → stores in 'fileContent'
  [REPORT] ReportAgent - Generates structured report → stores in 'report'
```

**Diagrama del Flujo** muestra el flujo de datos entre agentes. Cada agente tiene un rol específico:
- **FileAgent** lee archivos usando herramientas MCP y guarda el contenido bruto en `fileContent`
- **ReportAgent** consume ese contenido y produce un reporte estructurado en `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Solicitud del Usuario** muestra la tarea. El Supervisor la analiza y decide invocar FileAgent → ReportAgent.

```
--- SUPERVISOR ORCHESTRATION -----------------------------------------
  The Supervisor decides which agents to invoke and passes data between them...

  +-- STEP 1: Supervisor chose -> FileAgent (reading file via MCP)
  |
  |   Input: .../file.txt
  |
  |   Result: LangChain4j is an open-source, provider-agnostic Java framework for building LLM...
  +-- [OK] FileAgent (reading file via MCP) completed

  +-- STEP 2: Supervisor chose -> ReportAgent (generating structured report)
  |
  |   Input: LangChain4j is an open-source, provider-agnostic Java framew...
  |
  |   Result: Executive Summary...
  +-- [OK] ReportAgent (generating structured report) completed
```

**Orquestación del Supervisor** muestra el flujo en 2 pasos en acción:
1. **FileAgent** lee el archivo vía MCP y guarda el contenido
2. **ReportAgent** recibe el contenido y genera un reporte estructurado

El Supervisor hizo estas decisiones **autónomamente** basándose en la solicitud del usuario.

```
--- FINAL RESPONSE ---------------------------------------------------
Executive Summary
...

Key Points
...

Recommendations
...

--- AGENTIC SCOPE (Data Flow) ----------------------------------------
  Each agent stores its output for downstream agents to consume:
  * fileContent: LangChain4j is an open-source, provider-agnostic Java framework...
  * report: Executive Summary...
```

#### Explicación de las Funciones del Módulo Agente

El ejemplo demuestra varias funciones avanzadas del módulo agente. Veamos de cerca Ámbito Agente y Escuchas de Agentes.

**Ámbito Agente** muestra la memoria compartida donde los agentes almacenaron sus resultados usando `@Agent(outputKey="...")`. Esto permite:
- Que agentes posteriores accedan a salidas de agentes anteriores
- Que el Supervisor sintetice una respuesta final
- Que inspecciones qué produjo cada agente

<img src="../../../translated_images/es/agentic-scope.95ef488b6c1d02ef.webp" alt="Memoria Compartida de Ámbito Agente" width="800"/>

*Ámbito Agente actúa como memoria compartida — FileAgent escribe `fileContent`, ReportAgent lo lee y escribe `report`, y tu código lee el resultado final.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Datos en bruto del archivo de FileAgent
String report = scope.readState("report");            // Informe estructurado de ReportAgent
```

**Escuchas de Agentes** permiten monitorear y depurar la ejecución del agente. La salida paso a paso que ves en la demo proviene de un AgentListener que se engancha en cada invocación de agente:
- **beforeAgentInvocation** - Se llama cuando el Supervisor selecciona un agente, permitiéndote ver qué agente fue elegido y por qué
- **afterAgentInvocation** - Se llama cuando un agente termina, mostrando su resultado
- **inheritedBySubagents** - Cuando es true, el listener monitorea todos los agentes en la jerarquía

<img src="../../../translated_images/es/agent-listeners.784bfc403c80ea13.webp" alt="Ciclo de vida de los listeners de agentes" width="800"/>

*Los Agent Listeners se conectan al ciclo de vida de la ejecución — monitorean cuándo los agentes inician, terminan o encuentran errores.*

```java
AgentListener monitor = new AgentListener() {
    private int step = 0;
    
    @Override
    public void beforeAgentInvocation(AgentRequest request) {
        step++;
        System.out.println("  +-- STEP " + step + ": " + request.agentName());
    }
    
    @Override
    public void afterAgentInvocation(AgentResponse response) {
        System.out.println("  +-- [OK] " + response.agentName() + " completed");
    }
    
    @Override
    public boolean inheritedBySubagents() {
        return true; // Propagar a todos los subagentes
    }
};
```

Más allá del patrón Supervisor, el módulo `langchain4j-agentic` ofrece varios patrones de flujo de trabajo potentes y características:

<img src="../../../translated_images/es/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Patrones de flujo de trabajo de agentes" width="800"/>

*Cinco patrones de flujo de trabajo para orquestar agentes — desde simples pipelines secuenciales hasta flujos de aprobación con participación humana.*

| Patrón | Descripción | Caso de Uso |
|---------|-------------|-------------|
| **Secuencial** | Ejecutar agentes en orden, la salida fluye al siguiente | Pipelines: investigación → análisis → reporte |
| **Paralelo** | Ejecutar agentes simultáneamente | Tareas independientes: clima + noticias + bolsa |
| **Bucle** | Iterar hasta que se cumpla la condición | Puntuación de calidad: refinar hasta que la puntuación ≥ 0.8 |
| **Condicional** | Dirigir según condiciones | Clasificar → dirigir al agente especialista |
| **Humano en el Bucle** | Añadir puntos de control humanos | Flujos de aprobación, revisión de contenido |

## Conceptos Clave

Ahora que has explorado MCP y el módulo agentic en acción, resumamos cuándo usar cada enfoque.

<img src="../../../translated_images/es/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="Ecosistema MCP" width="800"/>

*MCP crea un ecosistema de protocolo universal — cualquier servidor compatible con MCP trabaja con cualquier cliente compatible con MCP, permitiendo compartir herramientas entre aplicaciones.*

**MCP** es ideal cuando quieres aprovechar ecosistemas de herramientas existentes, construir herramientas que varias aplicaciones puedan compartir, integrar servicios de terceros con protocolos estándar, o cambiar implementaciones de herramientas sin modificar el código.

**El Módulo Agentic** es mejor cuando quieres definiciones declarativas de agentes con anotaciones `@Agent`, necesitas orquestación de flujos de trabajo (secuencial, bucle, paralelo), prefieres diseño de agentes basado en interfaces en lugar de código imperativo, o estás combinando múltiples agentes que comparten salidas mediante `outputKey`.

**El patrón Supervisor Agent** destaca cuando el flujo de trabajo no es predecible de antemano y quieres que el LLM decida, cuando tienes múltiples agentes especializados que requieren orquestación dinámica, cuando construyes sistemas conversacionales que dirigen a diferentes capacidades, o cuando deseas el comportamiento de agente más flexible y adaptativo.

<img src="../../../translated_images/es/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Herramientas personalizadas vs herramientas MCP" width="800"/>

*Cuándo usar métodos @Tool personalizados vs herramientas MCP — herramientas personalizadas para lógica específica de la aplicación con seguridad total de tipos, herramientas MCP para integraciones estandarizadas que funcionan entre aplicaciones.*

## ¡Felicitaciones!

<img src="../../../translated_images/es/course-completion.48cd201f60ac7570.webp" alt="Finalización del curso" width="800"/>

*Tu recorrido de aprendizaje a través de los cinco módulos — desde chat básico hasta sistemas agentic impulsados por MCP.*

Has completado el curso LangChain4j para principiantes. Has aprendido:

- Cómo construir IA conversacional con memoria (Módulo 01)
- Patrones de ingeniería de prompts para distintas tareas (Módulo 02)
- Fundamentar respuestas en tus documentos con RAG (Módulo 03)
- Crear agentes de IA básicos (asistentes) con herramientas personalizadas (Módulo 04)
- Integrar herramientas estandarizadas con los módulos LangChain4j MCP y Agentic (Módulo 05)

### ¿Qué sigue?

Después de completar los módulos, explora la [Guía de Pruebas](../docs/TESTING.md) para ver conceptos de pruebas en LangChain4j en acción.

**Recursos Oficiales:**
- [Documentación de LangChain4j](https://docs.langchain4j.dev/) - Guías completas y referencia API
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Código fuente y ejemplos
- [Tutoriales LangChain4j](https://docs.langchain4j.dev/tutorials/) - Tutoriales paso a paso para varios casos de uso

¡Gracias por completar este curso!

---

**Navegación:** [← Anterior: Módulo 04 - Herramientas](../04-tools/README.md) | [Volver al Inicio](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso legal**:
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por la precisión, tenga en cuenta que las traducciones automáticas pueden contener errores o inexactitudes. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda una traducción profesional realizada por humanos. No nos hacemos responsables de ningún malentendido o interpretación errónea que resulte del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->