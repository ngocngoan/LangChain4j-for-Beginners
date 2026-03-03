# Módulo 05: Protocolo de Contexto del Modelo (MCP)

## Tabla de Contenidos

- [Lo que aprenderás](../../../05-mcp)
- [¿Qué es MCP?](../../../05-mcp)
- [Cómo funciona MCP](../../../05-mcp)
- [El Módulo Agéntico](../../../05-mcp)
- [Ejecutando los ejemplos](../../../05-mcp)
  - [Requisitos previos](../../../05-mcp)
- [Inicio rápido](../../../05-mcp)
  - [Operaciones de archivo (Stdio)](../../../05-mcp)
  - [Agente Supervisor](../../../05-mcp)
    - [Ejecutando la demostración](../../../05-mcp)
    - [Cómo funciona el Supervisor](../../../05-mcp)
    - [Cómo FileAgent descubre las herramientas MCP en tiempo de ejecución](../../../05-mcp)
    - [Estrategias de respuesta](../../../05-mcp)
    - [Entendiendo la salida](../../../05-mcp)
    - [Explicación de características del módulo agéntico](../../../05-mcp)
- [Conceptos clave](../../../05-mcp)
- [¡Felicidades!](../../../05-mcp)
  - [¿Qué sigue?](../../../05-mcp)

## Lo que aprenderás

Has creado IA conversacional, dominado los prompts, fundamentado respuestas en documentos y creado agentes con herramientas. Pero todas esas herramientas fueron creadas a medida para tu aplicación específica. ¿Y si pudieras dar acceso a tu IA a un ecosistema estandarizado de herramientas que cualquiera puede crear y compartir? En este módulo, aprenderás a hacer precisamente eso con el Protocolo de Contexto del Modelo (MCP) y el módulo agéntico de LangChain4j. Primero mostramos un lector de archivos MCP simple y luego demostramos cómo se integra fácilmente en flujos de trabajo agénticos avanzados usando el patrón Agente Supervisor.

## ¿Qué es MCP?

El Protocolo de Contexto del Modelo (MCP) proporciona precisamente eso: una forma estándar para que las aplicaciones de IA descubran y usen herramientas externas. En lugar de escribir integraciones personalizadas para cada fuente de datos o servicio, te conectas a servidores MCP que exponen sus capacidades en un formato consistente. Tu agente de IA puede entonces descubrir y usar estas herramientas automáticamente.

El siguiente diagrama muestra la diferencia: sin MCP, cada integración requiere un cableado personalizado punto a punto; con MCP, un único protocolo conecta tu aplicación con cualquier herramienta:

<img src="../../../translated_images/es/mcp-comparison.9129a881ecf10ff5.webp" alt="Comparación MCP" width="800"/>

*Antes de MCP: Integraciones complejas punto a punto. Después de MCP: Un protocolo, infinitas posibilidades.*

MCP resuelve un problema fundamental en el desarrollo de IA: cada integración es personalizada. ¿Quieres acceder a GitHub? Código personalizado. ¿Quieres leer archivos? Código personalizado. ¿Quieres consultar una base de datos? Código personalizado. Y ninguna de estas integraciones funciona con otras aplicaciones de IA.

MCP estandariza esto. Un servidor MCP expone herramientas con descripciones claras y esquemas. Cualquier cliente MCP puede conectarse, descubrir las herramientas disponibles y usarlas. Construye una vez, usa en cualquier lugar.

El siguiente diagrama ilustra esta arquitectura: un único cliente MCP (tu aplicación de IA) se conecta a múltiples servidores MCP, cada uno exponiendo su propio conjunto de herramientas a través del protocolo estándar:

<img src="../../../translated_images/es/mcp-architecture.b3156d787a4ceac9.webp" alt="Arquitectura MCP" width="800"/>

*Arquitectura del Protocolo de Contexto del Modelo - descubrimiento y ejecución estandarizados de herramientas*

## Cómo funciona MCP

Por debajo, MCP usa una arquitectura en capas. Tu aplicación Java (el cliente MCP) descubre herramientas disponibles, envía solicitudes JSON-RPC a través de una capa de transporte (Stdio o HTTP), y el servidor MCP ejecuta las operaciones y devuelve resultados. El siguiente diagrama desglosa cada capa de este protocolo:

<img src="../../../translated_images/es/mcp-protocol-detail.01204e056f45308b.webp" alt="Detalle del Protocolo MCP" width="800"/>

*Cómo funciona MCP por debajo — los clientes descubren herramientas, intercambian mensajes JSON-RPC y ejecutan operaciones mediante una capa de transporte.*

**Arquitectura Cliente-Servidor**

MCP utiliza un modelo cliente-servidor. Los servidores proveen herramientas — lectura de archivos, consultas a bases de datos, llamadas a APIs. Los clientes (tu aplicación IA) se conectan a los servidores y usan sus herramientas.

Para usar MCP con LangChain4j, añade esta dependencia Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Descubrimiento de herramientas**

Cuando tu cliente se conecta a un servidor MCP, pregunta "¿Qué herramientas tienes?" El servidor responde con una lista de herramientas disponibles, cada una con descripciones y esquemas de parámetros. Tu agente de IA entonces decide qué herramientas usar basándose en las solicitudes del usuario. El diagrama siguiente muestra este saludo — el cliente envía una solicitud `tools/list` y el servidor devuelve sus herramientas disponibles con descripciones y esquemas de parámetros:

<img src="../../../translated_images/es/tool-discovery.07760a8a301a7832.webp" alt="Descubrimiento de herramientas MCP" width="800"/>

*La IA descubre las herramientas disponibles al iniciar — ahora sabe qué capacidades tiene y puede decidir cuáles usar.*

**Mecanismos de transporte**

MCP soporta diferentes mecanismos de transporte. Las dos opciones son Stdio (para comunicación local entre procesos) y HTTP transmitible (para servidores remotos). Este módulo demuestra el transporte Stdio:

<img src="../../../translated_images/es/transport-mechanisms.2791ba7ee93cf020.webp" alt="Mecanismos de Transporte" width="800"/>

*Mecanismos de transporte MCP: HTTP para servidores remotos, Stdio para procesos locales*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Para procesos locales. Tu aplicación lanza un servidor como proceso hijo y se comunica mediante la entrada/salida estándar. Útil para acceso al sistema de archivos o herramientas de línea de comandos.

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

El servidor `@modelcontextprotocol/server-filesystem` expone las siguientes herramientas, todas confinadas a los directorios que especifiques:

| Herramienta | Descripción |
|-------------|-------------|
| `read_file` | Leer el contenido de un solo archivo |
| `read_multiple_files` | Leer múltiples archivos en una sola llamada |
| `write_file` | Crear o sobrescribir un archivo |
| `edit_file` | Realizar ediciones específicas de buscar y reemplazar |
| `list_directory` | Listar archivos y directorios en una ruta |
| `search_files` | Buscar recursivamente archivos que coincidan con un patrón |
| `get_file_info` | Obtener metadatos del archivo (tamaño, fechas, permisos) |
| `create_directory` | Crear un directorio (incluidos directorios padre) |
| `move_file` | Mover o renombrar un archivo o directorio |

El siguiente diagrama muestra cómo funciona el transporte Stdio en tiempo de ejecución — tu aplicación Java lanza el servidor MCP como proceso hijo y se comunican a través de pipes stdin/stdout, sin involucrar red ni HTTP:

<img src="../../../translated_images/es/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Flujo del Transporte Stdio" width="800"/>

*Transporte Stdio en acción — tu aplicación lanza el servidor MCP como proceso hijo y se comunica por medio de pipes stdin/stdout.*

> **🤖 Prueba con el Chat de [GitHub Copilot](https://github.com/features/copilot):** Abre [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) y pregunta:
> - "¿Cómo funciona el transporte Stdio y cuándo debo usarlo en lugar de HTTP?"
> - "¿Cómo maneja LangChain4j el ciclo de vida de los procesos MCP lanzados?"
> - "¿Cuáles son las implicaciones de seguridad al dar acceso a la IA al sistema de archivos?"

## El Módulo Agéntico

Mientras MCP proporciona herramientas estandarizadas, el **módulo agéntico** de LangChain4j ofrece una forma declarativa de construir agentes que orquestan esas herramientas. La anotación `@Agent` y `AgenticServices` te permiten definir el comportamiento del agente mediante interfaces en lugar de código imperativo.

En este módulo, explorarás el patrón **Agente Supervisor** — un enfoque avanzado de IA agéntica donde un agente "supervisor" decide dinámicamente qué subagentes invocar según las solicitudes del usuario. Combinaremos ambos conceptos al darle a uno de nuestros subagentes capacidades de acceso a archivos potenciadas por MCP.

Para usar el módulo agéntico, añade esta dependencia Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **Nota:** El módulo `langchain4j-agentic` usa una propiedad de versión separada (`langchain4j.mcp.version`) porque se lanza en un calendario diferente al de las librerías principales de LangChain4j.

> **⚠️ Experimental:** El módulo `langchain4j-agentic` es **experimental** y puede cambiar. La forma estable de construir asistentes IA sigue siendo usar `langchain4j-core` con herramientas personalizadas (Módulo 04).

## Ejecutando los ejemplos

### Requisitos previos

- Haber completado [Módulo 04 - Herramientas](../04-tools/README.md) (este módulo se basa en conceptos de herramientas personalizadas y las compara con herramientas MCP)
- Archivo `.env` en el directorio raíz con credenciales de Azure (creado por `azd up` en el Módulo 01)
- Java 21+, Maven 3.9+
- Node.js 16+ y npm (para servidores MCP)

> **Nota:** Si no has configurado aún tus variables de entorno, consulta [Módulo 01 - Introducción](../01-introduction/README.md) para instrucciones de despliegue (`azd up` crea automáticamente el archivo `.env`), o copia `.env.example` a `.env` en el directorio raíz y llena tus valores.

## Inicio rápido

**Usando VS Code:** Simplemente haz clic derecho sobre cualquier archivo de demostración en el Explorador y selecciona **"Run Java"** o usa las configuraciones de lanzamiento desde el panel Ejecutar y Depurar (asegúrate primero que tu archivo `.env` esté configurado con las credenciales de Azure).

**Usando Maven:** Alternativamente, puedes ejecutar desde la línea de comandos con los ejemplos a continuación.

### Operaciones de archivo (Stdio)

Esto demuestra herramientas basadas en un subproceso local.

**✅ No se necesitan requisitos previos** — el servidor MCP se lanza automáticamente.

**Usando los scripts de inicio (Recomendado):**

Los scripts de inicio cargan automáticamente las variables de entorno desde el archivo `.env` raíz:

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

La aplicación lanza un servidor MCP de sistema de archivos automáticamente y lee un archivo local. Observa cómo la gestión del subproceso se maneja para ti.

**Salida esperada:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agente Supervisor

El **patrón de Agente Supervisor** es una forma **flexible** de IA agéntica. Un Supervisor usa un LLM para decidir de forma autónoma qué agentes invocar según la solicitud del usuario. En el siguiente ejemplo combinamos el acceso a archivos potenciado por MCP con un agente LLM para crear un flujo supervisado de lectura de archivo → reporte.

En la demo, `FileAgent` lee un archivo usando herramientas de sistema de archivos MCP, y `ReportAgent` genera un reporte estructurado con un resumen ejecutivo (1 oración), 3 puntos clave y recomendaciones. El Supervisor orquesta este flujo automáticamente:

<img src="../../../translated_images/es/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Patrón de Agente Supervisor" width="800"/>

*El Supervisor usa su LLM para decidir qué agentes invocar y en qué orden — no se necesita enrutamiento codificado.*

Así es cómo luce el flujo concreto para nuestra tubería de archivo a reporte:

<img src="../../../translated_images/es/file-report-workflow.649bb7a896800de9.webp" alt="Flujo de Archivo a Reporte" width="800"/>

*FileAgent lee el archivo mediante herramientas MCP, luego ReportAgent transforma el contenido crudo en un reporte estructurado.*

El siguiente diagrama de secuencia traza la orquestación completa del Supervisor — desde lanzar el servidor MCP, pasando por la selección autónoma de agentes del Supervisor, hasta las llamadas a herramientas por stdio y el reporte final:

<img src="../../../translated_images/es/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Diagrama de Secuencia Agente Supervisor" width="800"/>

*El Supervisor invoca autónomamente a FileAgent (que llama al servidor MCP por stdio para leer el archivo), luego invoca a ReportAgent para generar un reporte estructurado — cada agente almacena su salida en el ámbito agéntico compartido.*

Cada agente almacena su salida en el **Ámbito Agéntico** (memoria compartida), permitiendo que agentes posteriores accedan a resultados previos. Esto demuestra cómo las herramientas MCP se integran perfectamente en flujos de trabajo agénticos — el Supervisor no necesita saber *cómo* se leen los archivos, solo que `FileAgent` puede hacerlo.

#### Ejecutando la demo

Los scripts de inicio cargan automáticamente las variables de entorno desde el archivo `.env` raíz:

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

#### Cómo funciona el Supervisor

Antes de construir agentes, necesitas conectar el transporte MCP a un cliente y envolverlo como un `ToolProvider`. Así es como las herramientas del servidor MCP se vuelven disponibles para tus agentes:

```java
// Crear un cliente MCP desde el transporte
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Envolver el cliente como un ToolProvider — esto conecta las herramientas MCP con LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Ahora puedes inyectar `mcpToolProvider` en cualquier agente que necesite herramientas MCP:

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

// El Supervisor coordina el flujo de trabajo archivo → informe
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Devuelve el informe final
        .build();

// El Supervisor decide qué agentes invocar según la solicitud
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Cómo FileAgent descubre las herramientas MCP en tiempo de ejecución

Quizá te preguntes: **¿cómo sabe `FileAgent` cómo usar las herramientas de sistema de archivos npm?** La respuesta es que no lo sabe — el **LLM** lo determina en tiempo de ejecución a través de los esquemas de herramientas.

La interfaz `FileAgent` es solo una **definición de prompt**. No tiene conocimiento codificado de `read_file`, `list_directory` u otra herramienta MCP. Esto es lo que sucede de forma integral:
1. **El servidor se inicia:** `StdioMcpTransport` lanza el paquete npm `@modelcontextprotocol/server-filesystem` como un proceso hijo  
2. **Descubrimiento de herramientas:** El `McpClient` envía una solicitud JSON-RPC `tools/list` al servidor, que responde con nombres de herramientas, descripciones y esquemas de parámetros (por ejemplo, `read_file` — *"Leer el contenido completo de un archivo"* — `{ path: string }`)  
3. **Inyección de esquemas:** `McpToolProvider` envuelve estos esquemas descubiertos y los pone a disposición de LangChain4j  
4. **Decisión del LLM:** Cuando se llama a `FileAgent.readFile(path)`, LangChain4j envía el mensaje del sistema, el mensaje del usuario, **y la lista de esquemas de herramientas** al LLM. El LLM lee las descripciones de las herramientas y genera una llamada a herramienta (por ejemplo, `read_file(path="/some/file.txt")`)  
5. **Ejecución:** LangChain4j intercepta la llamada a herramienta, la enruta a través del cliente MCP de vuelta al subproceso de Node.js, obtiene el resultado y lo envía de nuevo al LLM  

Este es el mismo mecanismo de [Descubrimiento de Herramientas](../../../05-mcp) descrito arriba, pero aplicado específicamente al flujo de trabajo del agente. Las anotaciones `@SystemMessage` y `@UserMessage` guían el comportamiento del LLM, mientras que el `ToolProvider` inyectado le da las **capacidades** — el LLM conecta ambos en tiempo de ejecución.

> **🤖 Prueba con [GitHub Copilot](https://github.com/features/copilot) Chat:** Abre [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) y pregunta:  
> - "¿Cómo sabe este agente qué herramienta MCP llamar?"  
> - "¿Qué pasaría si quitara el ToolProvider del constructor del agente?"  
> - "¿Cómo se pasan los esquemas de herramientas al LLM?"  

#### Estrategias de Respuesta

Cuando configuras un `SupervisorAgent`, especificas cómo debería formular su respuesta final al usuario después de que los subagentes hayan completado sus tareas. El diagrama a continuación muestra las tres estrategias disponibles — LAST devuelve directamente la salida del agente final, SUMMARY sintetiza todas las salidas a través de un LLM, y SCORED escoge la que obtenga mejor puntuación en relación con la solicitud original:

<img src="../../../translated_images/es/response-strategies.3d0cea19d096bdf9.webp" alt="Estrategias de Respuesta" width="800"/>

*Tres estrategias para cómo el Supervisor formula su respuesta final—elige según si deseas la salida del último agente, un resumen sintetizado o la opción con mejor puntuación.*

Las estrategias disponibles son:

| Estrategia | Descripción |
|------------|-------------|
| **LAST**  | El supervisor retorna la salida del último subagente o herramienta llamada. Esto es útil cuando el agente final en el flujo está diseñado específicamente para producir la respuesta completa y final (por ejemplo, un "Agente de Resumen" en una canalización de investigación). |
| **SUMMARY** | El supervisor utiliza su propio Modelo de Lenguaje interno (LLM) para sintetizar un resumen de toda la interacción y las salidas de los subagentes, luego retorna ese resumen como respuesta final. Esto proporciona una respuesta limpia y agregada al usuario. |
| **SCORED** | El sistema utiliza un LLM interno para puntuar tanto la respuesta LAST como el SUMMARY de la interacción contra la solicitud original del usuario, retornando la salida con la puntuación más alta. |

Consulta [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) para la implementación completa.

> **🤖 Prueba con [GitHub Copilot](https://github.com/features/copilot) Chat:** Abre [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) y pregunta:  
> - "¿Cómo decide el Supervisor qué agentes invocar?"  
> - "¿Cuál es la diferencia entre los patrones de flujo de trabajo Supervisor y Secuencial?"  
> - "¿Cómo puedo personalizar el comportamiento de planificación del Supervisor?"  

#### Comprendiendo la Salida

Cuando ejecutes la demo, verás una guía estructurada de cómo el Supervisor orquesta múltiples agentes. Esto es lo que significa cada sección:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**El encabezado** introduce el concepto de flujo de trabajo: una canalización enfocada desde la lectura de archivos hasta la generación de informes.

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
  
**Diagrama de Flujo de Trabajo** muestra el flujo de datos entre agentes. Cada agente tiene un rol específico:  
- **FileAgent** lee archivos usando herramientas MCP y almacena el contenido bruto en `fileContent`  
- **ReportAgent** consume ese contenido y produce un informe estructurado en `report`  

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
  
**Orquestación del Supervisor** muestra el flujo de 2 pasos en acción:  
1. **FileAgent** lee el archivo vía MCP y almacena el contenido  
2. **ReportAgent** recibe el contenido y genera un informe estructurado  

El Supervisor tomó estas decisiones **autónomamente** basándose en la solicitud del usuario.

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
  
#### Explicación de las Características del Módulo Agentic

El ejemplo muestra varias características avanzadas del módulo agentic. Vamos a echar un vistazo más detallado a Agentic Scope y a los Agent Listeners.

**Agentic Scope** muestra la memoria compartida donde los agentes almacenaron sus resultados usando `@Agent(outputKey="...")`. Esto permite:  
- Que agentes posteriores accedan a las salidas de agentes anteriores  
- Que el Supervisor sintetice una respuesta final  
- Que inspecciones lo que produjo cada agente  

El diagrama a continuación muestra cómo Agentic Scope funciona como memoria compartida en el flujo de trabajo de archivo a informe — FileAgent escribe su salida bajo la clave `fileContent`, ReportAgent la lee y escribe su propia salida bajo `report`:

<img src="../../../translated_images/es/agentic-scope.95ef488b6c1d02ef.webp" alt="Memoria Compartida de Agentic Scope" width="800"/>

*Agentic Scope actúa como memoria compartida — FileAgent escribe `fileContent`, ReportAgent lo lee y escribe `report`, y tu código lee el resultado final.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Datos en crudo del archivo de FileAgent
String report = scope.readState("report");            // Informe estructurado de ReportAgent
```
  
**Agent Listeners** permiten monitorear y depurar la ejecución de agentes. La salida paso a paso que ves en la demo proviene de un AgentListener que se engancha en cada invocación de agente:  
- **beforeAgentInvocation** - Se llama cuando el Supervisor selecciona un agente, permitiéndote ver cuál agente fue escogido y por qué  
- **afterAgentInvocation** - Se llama cuando un agente termina, mostrando su resultado  
- **inheritedBySubagents** - Cuando es true, el listener monitorea todos los agentes en la jerarquía  

El siguiente diagrama muestra el ciclo de vida completo de un Agent Listener, incluyendo cómo `onError` maneja fallos durante la ejecución del agente:

<img src="../../../translated_images/es/agent-listeners.784bfc403c80ea13.webp" alt="Ciclo de Vida de Agent Listeners" width="800"/>

*Los Agent Listeners se enganchan en el ciclo de vida de ejecución — monitorean cuando los agentes comienzan, terminan o encuentran errores.*

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
  
Más allá del patrón Supervisor, el módulo `langchain4j-agentic` provee varios patrones poderosos de flujo de trabajo. El diagrama a continuación muestra los cinco — desde canalizaciones secuenciales simples hasta flujos de aprobación con intervención humana:

<img src="../../../translated_images/es/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Patrones de Flujo de Trabajo de Agentes" width="800"/>

*Cinco patrones de flujo de trabajo para orquestar agentes — desde canalizaciones secuenciales simples hasta flujos de aprobación con intervención humana.*

| Patrón | Descripción | Caso de Uso |
|--------|-------------|-------------|
| **Secuencial** | Ejecuta agentes en orden, la salida fluye al siguiente | Canalizaciones: investigación → análisis → informe |
| **Paralelo** | Ejecuta agentes simultáneamente | Tareas independientes: clima + noticias + bolsa |
| **Bucle** | Itera hasta cumplir condición | Puntuación de calidad: refinar hasta que la puntuación ≥ 0.8 |
| **Condicional** | Ruta basada en condiciones | Clasificar → dirigir a agente especialista |
| **Humano-en-el-Bucle** | Añade puntos de control humanos | Flujos de aprobación, revisión de contenido |

## Conceptos Clave

Ahora que has explorado MCP y el módulo agentic en acción, resumamos cuándo usar cada enfoque.

Una de las mayores ventajas de MCP es su ecosistema en expansión. El diagrama a continuación muestra cómo un único protocolo universal conecta tu aplicación de IA con una amplia variedad de servidores MCP — desde acceso a sistema de archivos y bases de datos hasta GitHub, correo electrónico, scraping web y más:

<img src="../../../translated_images/es/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="Ecosistema MCP" width="800"/>

*MCP crea un ecosistema de protocolo universal — cualquier servidor compatible con MCP funciona con cualquier cliente compatible con MCP, facilitando el intercambio de herramientas entre aplicaciones.*

**MCP** es ideal cuando quieres aprovechar ecosistemas de herramientas existentes, construir herramientas que varias aplicaciones puedan compartir, integrar servicios de terceros con protocolos estándar, o cambiar implementaciones de herramientas sin modificar código.

**El Módulo Agentic** funciona mejor cuando quieres definiciones declarativas de agentes con anotaciones `@Agent`, necesitas orquestación de flujo de trabajo (secuencial, bucle, paralelo), prefieres diseño de agentes basado en interfaces en lugar de código imperativo, o estás combinando múltiples agentes que comparten salidas vía `outputKey`.

**El patrón Supervisor Agent** destaca cuando el flujo de trabajo no es predecible de antemano y quieres que el LLM decida, cuando tienes múltiples agentes especializados que requieren orquestación dinámica, cuando construyes sistemas conversacionales que enrutan a diferentes capacidades, o cuando quieres el comportamiento de agente más flexible y adaptativo.

Para ayudarte a decidir entre los métodos personalizados `@Tool` del Módulo 04 y las herramientas MCP de este módulo, la siguiente comparación destaca las compensaciones clave — las herramientas personalizadas ofrecen acoplamiento estrecho y seguridad total de tipos para lógica específica de la app, mientras que las herramientas MCP ofrecen integraciones estandarizadas y reutilizables:

<img src="../../../translated_images/es/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Herramientas Personalizadas vs Herramientas MCP" width="800"/>

*Cuándo usar métodos personalizados @Tool vs herramientas MCP — herramientas personalizadas para lógica específica de la app con seguridad total de tipos, herramientas MCP para integraciones estandarizadas que funcionan en distintas aplicaciones.*

## ¡Felicidades!

¡Has completado los cinco módulos del curso LangChain4j para Principiantes! Aquí tienes una vista del camino de aprendizaje completo que has recorrido — desde chat básico hasta sistemas agentic impulsados por MCP:

<img src="../../../translated_images/es/course-completion.48cd201f60ac7570.webp" alt="Finalización del Curso" width="800"/>

*Tu camino de aprendizaje a través de los cinco módulos — desde chat básico a sistemas agentic impulsados por MCP.*

Has completado el curso LangChain4j para Principiantes. Has aprendido:

- Cómo construir IA conversacional con memoria (Módulo 01)  
- Patrones de ingeniería de prompts para diferentes tareas (Módulo 02)  
- Fundamentos para basar respuestas en tus documentos con RAG (Módulo 03)  
- Crear agentes básicos de IA (asistentes) con herramientas personalizadas (Módulo 04)  
- Integrar herramientas estandarizadas con los módulos LangChain4j MCP y Agentic (Módulo 05)  

### ¿Qué Sigue?

Después de completar los módulos, explora la [Guía de Pruebas](../docs/TESTING.md) para ver conceptos de testeo en LangChain4j en acción.

**Recursos Oficiales:**  
- [Documentación LangChain4j](https://docs.langchain4j.dev/) - Guías completas y referencia API  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Código fuente y ejemplos  
- [Tutoriales LangChain4j](https://docs.langchain4j.dev/tutorials/) - Tutoriales paso a paso para varios casos de uso  

¡Gracias por completar este curso!

---

**Navegación:** [← Anterior: Módulo 04 - Herramientas](../04-tools/README.md) | [Volver al Inicio](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por la precisión, tenga en cuenta que las traducciones automáticas pueden contener errores o inexactitudes. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda la traducción profesional realizada por personas. No nos responsabilizamos por malentendidos o interpretaciones erróneas derivadas del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->