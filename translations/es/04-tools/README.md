# Módulo 04: Agentes de IA con Herramientas

## Tabla de Contenidos

- [Recorrido en Video](../../../04-tools)
- [Lo que Aprenderás](../../../04-tools)
- [Prerrequisitos](../../../04-tools)
- [Entendiendo los Agentes de IA con Herramientas](../../../04-tools)
- [Cómo Funciona la Llamada a Herramientas](../../../04-tools)
  - [Definiciones de Herramientas](../../../04-tools)
  - [Toma de Decisiones](../../../04-tools)
  - [Ejecución](../../../04-tools)
  - [Generación de Respuestas](../../../04-tools)
  - [Arquitectura: Auto-Conexión en Spring Boot](../../../04-tools)
- [Encadenamiento de Herramientas](../../../04-tools)
- [Ejecutar la Aplicación](../../../04-tools)
- [Uso de la Aplicación](../../../04-tools)
  - [Prueba de Uso Simple de Herramientas](../../../04-tools)
  - [Prueba de Encadenamiento de Herramientas](../../../04-tools)
  - [Ver Flujo de Conversación](../../../04-tools)
  - [Experimentar con Diferentes Solicitudes](../../../04-tools)
- [Conceptos Clave](../../../04-tools)
  - [Patrón ReAct (Razonar y Actuar)](../../../04-tools)
  - [Importancia de las Descripciones de las Herramientas](../../../04-tools)
  - [Gestión de Sesiones](../../../04-tools)
  - [Manejo de Errores](../../../04-tools)
- [Herramientas Disponibles](../../../04-tools)
- [Cuándo Usar Agentes Basados en Herramientas](../../../04-tools)
- [Herramientas vs RAG](../../../04-tools)
- [Próximos Pasos](../../../04-tools)

## Recorrido en Video

Mira esta sesión en vivo que explica cómo comenzar con este módulo:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="Agentes de IA con Herramientas y MCP - Sesión en Vivo" width="800"/></a>

## Lo que Aprenderás

Hasta ahora, has aprendido a mantener conversaciones con IA, estructurar indicaciones de manera efectiva y fundamentar respuestas en tus documentos. Pero todavía hay una limitación fundamental: los modelos de lenguaje solo pueden generar texto. No pueden consultar el clima, realizar cálculos, consultar bases de datos o interactuar con sistemas externos.

Las herramientas cambian esto. Al darle al modelo acceso a funciones que puede llamar, lo transformas de un generador de texto a un agente que puede tomar acciones. El modelo decide cuándo necesita una herramienta, cuál usar y qué parámetros pasar. Tu código ejecuta la función y devuelve el resultado. El modelo incorpora ese resultado en su respuesta.

## Prerrequisitos

- Completar [Módulo 01 - Introducción](../01-introduction/README.md) (recursos Azure OpenAI desplegados)
- Se recomiendan los módulos previos completados (este módulo refiere a [conceptos de RAG del Módulo 03](../03-rag/README.md) en la comparación Herramientas vs RAG)
- Archivo `.env` en el directorio raíz con credenciales de Azure (creado con `azd up` en el Módulo 01)

> **Nota:** Si no has completado el Módulo 01, sigue primero las instrucciones de despliegue allí.

## Entendiendo los Agentes de IA con Herramientas

> **📝 Nota:** El término "agentes" en este módulo se refiere a asistentes de IA mejorados con capacidades de llamada a herramientas. Esto es diferente de los patrones **Agentic AI** (agentes autónomos con planificación, memoria y razonamiento en múltiples pasos) que abordaremos en [Módulo 05: MCP](../05-mcp/README.md).

Sin herramientas, un modelo de lenguaje solo puede generar texto basado en sus datos de entrenamiento. Pregúntale el clima actual y tiene que adivinar. Dale herramientas, y puede llamar a una API del clima, realizar cálculos o consultar una base de datos — luego entretejer esos resultados reales en su respuesta.

<img src="../../../translated_images/es/what-are-tools.724e468fc4de64da.webp" alt="Sin Herramientas vs Con Herramientas" width="800"/>

*Sin herramientas, el modelo solo puede adivinar — con herramientas puede llamar APIs, realizar cálculos y devolver datos en tiempo real.*

Un agente de IA con herramientas sigue un patrón de **Razonamiento y Acción (ReAct)**. El modelo no solo responde — piensa en lo que necesita, actúa llamando a una herramienta, observa el resultado y luego decide si actúa nuevamente o entrega la respuesta final:

1. **Razonar** — El agente analiza la pregunta del usuario y determina qué información necesita
2. **Actuar** — El agente selecciona la herramienta correcta, genera los parámetros adecuados y la llama
3. **Observar** — El agente recibe la salida de la herramienta y evalúa el resultado
4. **Repetir o Responder** — Si se necesita más datos, el agente repite; si no, compone una respuesta en lenguaje natural

<img src="../../../translated_images/es/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Patrón ReAct" width="800"/>

*El ciclo ReAct — el agente razona sobre qué hacer, actúa llamando a una herramienta, observa el resultado y repite hasta poder entregar la respuesta final.*

Esto ocurre automáticamente. Definís las herramientas y sus descripciones. El modelo maneja la toma de decisión sobre cuándo y cómo usarlas.

## Cómo Funciona la Llamada a Herramientas

### Definiciones de Herramientas

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Defines funciones con descripciones claras y especificaciones de parámetros. El modelo ve esas descripciones en su prompt de sistema y comprende lo que hace cada herramienta.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Tu lógica de búsqueda del clima
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// El asistente está conectado automáticamente por Spring Boot con:
// - Bean ChatModel
// - Todos los métodos @Tool de las clases @Component
// - ChatMemoryProvider para la gestión de sesiones
```

El diagrama a continuación desglosa cada anotación y muestra cómo cada parte ayuda a la IA a entender cuándo llamar a la herramienta y qué argumentos pasar:

<img src="../../../translated_images/es/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomía de Definiciones de Herramientas" width="800"/>

*Anatomía de una definición de herramienta — @Tool indica a la IA cuándo usarla, @P describe cada parámetro, y @AiService conecta todo al inicio.*

> **🤖 Prueba con [GitHub Copilot](https://github.com/features/copilot) Chat:** Abre [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) y pregunta:
> - "¿Cómo integraría una API real del clima como OpenWeatherMap en lugar de datos simulados?"
> - "¿Qué hace que una buena descripción de herramienta ayude a la IA a usarla correctamente?"
> - "¿Cómo manejo errores de API y límites de tasa en implementaciones de herramientas?"

### Toma de Decisiones

Cuando un usuario pregunta "¿Cuál es el clima en Seattle?", el modelo no elige una herramienta al azar. Compara la intención del usuario con cada descripción de herramienta a la que tiene acceso, puntúa cada una por relevancia y selecciona la mejor coincidencia. Luego genera una llamada de función estructurada con los parámetros correctos — en este caso, fijando `location` a `"Seattle"`.

Si ninguna herramienta coincide con la solicitud del usuario, el modelo recurre a responder con su propio conocimiento. Si varias herramientas coinciden, elige la más específica.

<img src="../../../translated_images/es/decision-making.409cd562e5cecc49.webp" alt="Cómo la IA Decide Qué Herramienta Usar" width="800"/>

*El modelo evalúa todas las herramientas disponibles contra la intención del usuario y selecciona la mejor coincidencia — por eso es importante escribir descripciones claras y específicas.*

### Ejecución

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot conecta automáticamente la interfaz declarativa `@AiService` con todas las herramientas registradas, y LangChain4j ejecuta las llamadas a herramientas automáticamente. Detrás de escena, una llamada completa a herramienta fluye a través de seis etapas — desde la pregunta en lenguaje natural del usuario hasta la respuesta final en lenguaje natural:

<img src="../../../translated_images/es/tool-calling-flow.8601941b0ca041e6.webp" alt="Flujo de Llamada a Herramientas" width="800"/>

*El flujo de punta a punta — el usuario hace una pregunta, el modelo selecciona una herramienta, LangChain4j la ejecuta y el modelo integra el resultado en una respuesta natural.*

Si ejecutaste el [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) en el Módulo 00, ya viste este patrón en acción — las herramientas `Calculator` eran llamadas de la misma forma. El diagrama de secuencia abajo muestra exactamente qué pasó internamente durante esa demo:

<img src="../../../translated_images/es/tool-calling-sequence.94802f406ca26278.webp" alt="Diagrama de Secuencia de Llamada a Herramientas" width="800"/>

*El ciclo de llamada a herramientas desde la demo de Inicio Rápido — `AiServices` envía tu mensaje y esquemas de herramientas al LLM, el LLM responde con una llamada función como `add(42, 58)`, LangChain4j ejecuta el método `Calculator` localmente y devuelve el resultado para la respuesta final.*

> **🤖 Prueba con [GitHub Copilot](https://github.com/features/copilot) Chat:** Abre [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) y pregunta:
> - "¿Cómo funciona el patrón ReAct y por qué es efectivo para agentes de IA?"
> - "¿Cómo decide el agente qué herramienta usar y en qué orden?"
> - "¿Qué sucede si falla la ejecución de una herramienta? ¿Cómo debo manejar errores de forma robusta?"

### Generación de Respuesta

El modelo recibe los datos del clima y los formatea en una respuesta en lenguaje natural para el usuario.

### Arquitectura: Auto-Conexión en Spring Boot

Este módulo usa la integración de LangChain4j con Spring Boot mediante interfaces declarativas `@AiService`. Al iniciar, Spring Boot descubre cada `@Component` que contiene métodos con `@Tool`, tu bean `ChatModel` y el `ChatMemoryProvider` — luego los conecta todos en una sola interfaz `Assistant` sin código repetitivo.

<img src="../../../translated_images/es/spring-boot-wiring.151321795988b04e.webp" alt="Arquitectura de Auto-Conexión en Spring Boot" width="800"/>

*La interfaz @AiService une el ChatModel, los componentes de herramientas y el proveedor de memoria — Spring Boot gestiona toda la conexión automáticamente.*

Aquí está el ciclo completo de la solicitud en un diagrama de secuencia — desde la solicitud HTTP a través del controlador, servicio y proxy auto-conectado, hasta la ejecución de la herramienta y la respuesta:

<img src="../../../translated_images/es/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Secuencia de Llamada a Herramientas en Spring Boot" width="800"/>

*El ciclo completo de solicitud en Spring Boot — la solicitud HTTP fluye a través del controlador y servicio al proxy Assistant auto-conectado, que orquesta el LLM y las llamadas a herramientas automáticamente.*

Beneficios clave de este enfoque:

- **Auto-conexión de Spring Boot** — ChatModel y herramientas inyectados automáticamente
- **Patrón @MemoryId** — Gestión automática de memoria basada en sesión
- **Instancia única** — Assistant creado una vez y reutilizado para mejor rendimiento
- **Ejecución segura de tipo** — Métodos Java llamados directamente con conversión de tipos
- **Orquestación de múltiples turnos** — Maneja encadenamiento de herramientas automáticamente
- **Cero código repetitivo** — Sin llamadas manuales a `AiServices.builder()` ni mapas de memoria

Enfoques alternativos (manual `AiServices.builder()`) requieren más código y pierden los beneficios de integración con Spring Boot.

## Encadenamiento de Herramientas

**Encadenamiento de Herramientas** — El verdadero poder de los agentes basados en herramientas aparece cuando una sola pregunta requiere múltiples herramientas. Pregunta "¿Cuál es el clima en Seattle en Fahrenheit?" y el agente encadena automáticamente dos herramientas: primero llama a `getCurrentWeather` para obtener la temperatura en Celsius, luego pasa ese valor a `celsiusToFahrenheit` para la conversión — todo en un solo turno de conversación.

<img src="../../../translated_images/es/tool-chaining-example.538203e73d09dd82.webp" alt="Ejemplo de Encadenamiento de Herramientas" width="800"/>

*Encadenamiento de herramientas en acción — el agente llama primero a getCurrentWeather, luego pasa el resultado en Celsius a celsiusToFahrenheit y entrega una respuesta combinada.*

**Fallos Graceful** — Pide el clima en una ciudad que no está en los datos simulados. La herramienta devuelve un mensaje de error y la IA explica que no puede ayudar en lugar de fallar. Las herramientas fallan de forma segura. El diagrama a continuación contrasta ambos enfoques — con manejo adecuado de errores, el agente captura la excepción y responde amablemente, mientras sin él la aplicación entera colapsa:

<img src="../../../translated_images/es/error-handling-flow.9a330ffc8ee0475c.webp" alt="Flujo de Manejo de Errores" width="800"/>

*Cuando una herramienta falla, el agente captura el error y responde con una explicación útil en lugar de fallar.*

Esto sucede en un solo turno de conversación. El agente orquesta múltiples llamadas a herramientas de forma autónoma.

## Ejecutar la Aplicación

**Verificar despliegue:**

Asegúrate de que el archivo `.env` exista en el directorio raíz con las credenciales de Azure (creado durante el Módulo 01). Ejecuta esto desde el directorio del módulo (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Debería mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Debería mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Iniciar la aplicación:**

> **Nota:** Si ya iniciaste todas las aplicaciones usando `./start-all.sh` desde el directorio raíz (como se describió en el Módulo 01), este módulo ya está ejecutándose en el puerto 8084. Puedes omitir los comandos de inicio abajo y acceder directamente a http://localhost:8084.

**Opción 1: Usando el Spring Boot Dashboard (Recomendado para usuarios de VS Code)**

El contenedor de desarrollo incluye la extensión Spring Boot Dashboard, que proporciona una interfaz visual para administrar todas las aplicaciones Spring Boot. La encontrarás en la Barra de Actividad al lado izquierdo de VS Code (busca el ícono de Spring Boot).

Desde el Spring Boot Dashboard, puedes:
- Ver todas las aplicaciones Spring Boot disponibles en el espacio de trabajo
- Iniciar/detener aplicaciones con un solo clic
- Ver logs de aplicaciones en tiempo real
- Monitorear el estado de las aplicaciones
Simplemente haga clic en el botón de reproducir junto a "tools" para comenzar este módulo, o inicie todos los módulos a la vez.

Así es como se ve el Spring Boot Dashboard en VS Code:

<img src="../../../translated_images/es/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*El Spring Boot Dashboard en VS Code — iniciar, detener y monitorear todos los módulos desde un solo lugar*

**Opción 2: Usando scripts de shell**

Inicie todas las aplicaciones web (módulos 01-04):

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

O inicie solo este módulo:

**Bash:**
```bash
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

Ambos scripts cargan automáticamente las variables de entorno desde el archivo `.env` raíz y construirán los JARs si no existen.

> **Nota:** Si prefiere compilar todos los módulos manualmente antes de iniciar:
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

Abra http://localhost:8084 en su navegador.

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

La aplicación ofrece una interfaz web donde puedes interactuar con un agente de IA que tiene acceso a herramientas de clima y conversión de temperatura. Así es como se ve la interfaz — incluye ejemplos rápidos y un panel de chat para enviar solicitudes:

<a href="images/tools-homepage.png"><img src="../../../translated_images/es/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*La interfaz de Herramientas del Agente de IA: ejemplos rápidos e interfaz de chat para interactuar con herramientas*

### Pruebe un Uso Simple de Herramientas

Comience con una solicitud sencilla: "Convertir 100 grados Fahrenheit a Celsius". El agente reconoce que necesita la herramienta de conversión de temperatura, la llama con los parámetros correctos y devuelve el resultado. Note lo natural que se siente — no especificó qué herramienta usar ni cómo llamarla.

### Pruebe la Cadena de Herramientas

Ahora intente algo más complejo: "¿Cuál es el clima en Seattle y conviértalo a Fahrenheit?" Observe cómo el agente procesa esto en pasos. Primero obtiene el clima (que devuelve Celsius), reconoce que debe convertir a Fahrenheit, llama a la herramienta de conversión y combina ambos resultados en una sola respuesta.

### Vea el Flujo de la Conversación

La interfaz de chat mantiene el historial de la conversación, permitiendo interacciones multi-turno. Puede ver todas las consultas y respuestas anteriores, facilitando seguir la conversación y entender cómo el agente construye el contexto a lo largo de múltiples intercambios.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/es/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Conversación multi-turno mostrando conversiones simples, consultas meteorológicas y encadenamiento de herramientas*

### Experimente con Diferentes Solicitudes

Pruebe varias combinaciones:
- Consultas meteorológicas: "¿Cuál es el clima en Tokio?"
- Conversiones de temperatura: "¿Cuánto es 25 °C en Kelvin?"
- Consultas combinadas: "Revisa el clima en París y dime si está por encima de 20 °C"

Observe cómo el agente interpreta el lenguaje natural y lo mapea a llamadas apropiadas a herramientas.

## Conceptos Clave

### Patrón ReAct (Razonamiento y Acción)

El agente alterna entre razonar (decidir qué hacer) y actuar (usar herramientas). Este patrón permite la resolución autónoma de problemas en lugar de solo responder a instrucciones.

### Las Descripciones de las Herramientas Importan

La calidad de las descripciones de tus herramientas afecta directamente qué tan bien el agente las utiliza. Descripciones claras y específicas ayudan al modelo a entender cuándo y cómo llamar a cada herramienta.

### Gestión de Sesiones

La anotación `@MemoryId` habilita la gestión automática de memoria basada en sesiones. Cada ID de sesión obtiene su propia instancia `ChatMemory` gestionada por el bean `ChatMemoryProvider`, de modo que múltiples usuarios pueden interactuar con el agente simultáneamente sin que sus conversaciones se mezclen. El siguiente diagrama muestra cómo múltiples usuarios se dirigen a almacenes de memoria aislados según sus IDs de sesión:

<img src="../../../translated_images/es/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Cada ID de sesión se asigna a un historial de conversación aislado — los usuarios nunca ven los mensajes de otros.*

### Manejo de Errores

Las herramientas pueden fallar — las API pueden agotar tiempo, los parámetros pueden ser inválidos, los servicios externos pueden caer. Los agentes en producción necesitan manejo de errores para que el modelo pueda explicar problemas o intentar alternativas en lugar de colapsar toda la aplicación. Cuando una herramienta lanza una excepción, LangChain4j la captura y envía el mensaje de error al modelo, que entonces puede explicar el problema en lenguaje natural.

## Herramientas Disponibles

El diagrama a continuación muestra el amplio ecosistema de herramientas que puedes construir. Este módulo demuestra herramientas de clima y temperatura, pero el mismo patrón `@Tool` funciona para cualquier método Java — desde consultas a bases de datos hasta procesamiento de pagos.

<img src="../../../translated_images/es/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Cada método Java anotado con @Tool queda disponible para la IA — el patrón se extiende a bases de datos, APIs, correo electrónico, operaciones de archivos y más.*

## Cuándo Usar Agentes Basados en Herramientas

No todas las solicitudes necesitan herramientas. La decisión depende de si la IA necesita interactuar con sistemas externos o puede responder con su propio conocimiento. La siguiente guía resume cuándo las herramientas añaden valor y cuándo no son necesarias:

<img src="../../../translated_images/es/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*Una guía rápida para decidir — las herramientas son para datos en tiempo real, cálculos y acciones; el conocimiento general y tareas creativas no las necesitan.*

## Herramientas vs RAG

Los módulos 03 y 04 amplían lo que la IA puede hacer, pero de formas fundamentalmente diferentes. RAG le da al modelo acceso a **conocimiento** recuperando documentos. Las Herramientas le dan al modelo la capacidad de tomar **acciones** llamando funciones. El diagrama a continuación compara estos dos enfoques lado a lado — desde cómo opera cada flujo de trabajo hasta las compensaciones entre ellos:

<img src="../../../translated_images/es/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG recupera información de documentos estáticos — Las Herramientas ejecutan acciones y obtienen datos dinámicos en tiempo real. Muchos sistemas de producción combinan ambos.*

En la práctica, muchos sistemas de producción combinan ambos enfoques: RAG para fundamentar respuestas en tu documentación y Herramientas para obtener datos en vivo o realizar operaciones.

## Próximos Pasos

**Próximo Módulo:** [05-mcp - Protocolo de Contexto del Modelo (MCP)](../05-mcp/README.md)

---

**Navegación:** [← Anterior: Módulo 03 - RAG](../03-rag/README.md) | [Volver al Inicio](../README.md) | [Siguiente: Módulo 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Descargo de responsabilidad**:
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por la precisión, tenga en cuenta que las traducciones automáticas pueden contener errores o inexactitudes. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda una traducción profesional realizada por humanos. No nos responsabilizamos por malentendidos o interpretaciones erróneas derivadas del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->